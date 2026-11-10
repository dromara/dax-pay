package cn.daxpay.open.payment.device.terminal.service;

import cn.daxpay.open.payment.device.terminal.dao.ChannelTerminalManager;
import cn.daxpay.open.payment.device.terminal.dao.TerminalChannelBindManager;
import cn.daxpay.open.payment.device.terminal.dao.TerminalDeviceManager;
import cn.daxpay.open.payment.device.terminal.entity.DeviceChannelTerminal;
import cn.daxpay.open.payment.device.terminal.entity.DeviceTerminalChannelBind;
import cn.daxpay.open.payment.device.terminal.entity.DeviceTerminal;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.enums.pay.channel.ChannelTerminalStatusEnum;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/// # 支付终端校验与解析服务
///
/// 下单上送系统终端号时的两道处理(宽松策略):
/// - 校验: 终端号不存在/不属于当前商户/已停用 → 一律拦截下单(传错号属配置错误, 放行会静默丢失终端维度);
/// - 解析: 通道路由落定后按绑定关系解析通道侧终端号(登记状态=已报送且通道终端号非空的第一条),
///   解析不到返回 null 不拦截(宽松策略, 按无报备继续), 只有校验失败才拦截。
///
/// 未上送终端号但携带门店号的订单, 先按门店默认终端([#resolveDefaultTerminalNo])回落补齐;
/// 既无终端号也无默认终端时放行(存量客户端不传号零影响)。
///
/// 挂点: [cn.daxpay.open.payment.trade.runtime.service.pay.normal.NormalPayService#payHandle]
/// 中通道路由([cn.daxpay.open.payment.route.service.runtime.PayRouteService#resolve])之后,
/// 建单落库之前。
@Slf4j
@Service
@RequiredArgsConstructor
public class TerminalResolveService {

    private final TerminalDeviceManager terminalDeviceManager;
    private final TerminalChannelBindManager terminalChannelBindManager;
    private final ChannelTerminalManager channelTerminalManager;

    /// 校验系统终端(存在性/归属/启用), 失败抛异常拦截下单
    public void validateTerminal(String terminalNo, String mchNo) {
        DeviceTerminal device = terminalDeviceManager.findByTerminalNo(terminalNo)
                // 终端: 系统终端不存在
                .orElseThrow(() -> new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                        "error.device.terminal.notFound", terminalNo));
        if (!Objects.equals(device.getMchNo(), mchNo)) {
            // 终端: 系统终端不属于当前商户
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "error.device.terminal.mchNotMatch", terminalNo);
        }
        if (!Boolean.TRUE.equals(device.getEnable())) {
            // 终端: 系统终端已停用
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "error.device.terminal.disabled", terminalNo);
        }
    }

    /// 解析门店默认终端号(支付回落): 按商户+门店查启用中的默认终端, 无则返回 null
    ///
    /// 支付请求未显式指定终端号但携带门店号时, 按门店默认终端回落补齐终端维度;
    /// 未配置默认终端或默认终端已停用时返回 null(维持无终端号, 存量行为不变)。
    public String resolveDefaultTerminalNo(String mchNo, String storeNo) {
        if (StrUtil.isBlank(storeNo)) {
            return null;
        }
        return terminalDeviceManager.findDefaultByStoreNo(mchNo, storeNo)
                .map(DeviceTerminal::getTerminalNo)
                .orElse(null);
    }

    /// 解析通道侧终端号: 系统终端号 + 通道商户号 定位绑定, 取登记状态=已报送且通道终端号非空的第一条
    ///
    /// 宽松策略: 查不到返回 null(无绑定/未报送/终端号为空), 不拦截下单
    public String resolveOutTerminalNo(String systemTerminalNo, String channelMchNo) {
        if (StrUtil.isBlank(channelMchNo)) {
            return null;
        }
        // 绑定关系冗余列单表定位 → 关联通道终端中已报送且通道终端号有效的第一条
        List<DeviceTerminalChannelBind> binds =
                terminalChannelBindManager.findBySystemTerminalNoAndChannelMchNo(systemTerminalNo, channelMchNo);
        Optional<DeviceChannelTerminal> submitted = binds.stream()
                .map(DeviceTerminalChannelBind::getChannelTerminalId)
                .map(channelTerminalManager::findById)
                .flatMap(Optional::stream)
                .filter(t -> ChannelTerminalStatusEnum.SUBMIT.getCode().equals(t.getStatus()))
                .filter(t -> StrUtil.isNotBlank(t.getOutTerminalNo()))
                .findFirst();
        if (submitted.isEmpty()) {
            // 宽松策略: 解析不到不拦截, 记告警便于排查终端配置
            log.warn("通道侧终端号解析失败(按无报备继续): systemTerminalNo={}, channelMchNo={}",
                    systemTerminalNo, channelMchNo);
            return null;
        }
        return submitted.get().getOutTerminalNo();
    }
}
