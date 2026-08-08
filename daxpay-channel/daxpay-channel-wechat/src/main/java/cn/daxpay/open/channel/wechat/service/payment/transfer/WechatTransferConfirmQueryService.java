package cn.daxpay.open.channel.wechat.service.payment.transfer;

import cn.daxpay.open.channel.wechat.client.credential.WechatSdkCredential;
import cn.daxpay.open.channel.wechat.dao.direct.WechatTransferConfigManager;
import cn.daxpay.open.channel.wechat.entity.direct.WechatTransferConfig;
import cn.daxpay.open.channel.wechat.result.direct.WechatTransferConfirmResult;
import cn.daxpay.open.channel.wechat.service.direct.WechatDirectConfigAssembler;
import cn.daxpay.open.payment.common.context.MerchantContextLoader;
import cn.daxpay.open.payment.trade.transfer.dao.WechatTransferOrderManager;
import cn.daxpay.open.payment.trade.transfer.entity.WechatTransferOrder;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.code.DaxPayErrorCode;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Set;

/// # 微信转账确认收款查询服务
///
/// 供 C 端收款人(无登录态)凭 transferNo 查询确认收款信息。
/// 跨租户引导读订单([WechatTransferOrderManager#findByTransferNoNotTenant]) →
/// 装载商户上下文 → 通道凭证组装获取 wxMchId/wxAppId → 返回 packageInfo 等拉起参数。
///
/// 凭证组装复用发起转账路径([WechatDirectConfigAssembler#buildTransferConfig]):
/// 查「微信转账配置」取发起应用引用(transferAppRefId) → 加载商户档应用 wxAppId + 密钥,
/// 与 [cn.daxpay.open.channel.wechat.strategy.direct.transfer.WechatTransferStrategy#buildCredential] 完全一致。
///
/// 安全模型: 凭 transferNo 高熵不可猜防枚举, 仅返回该订单的公开收款信息。
@Slf4j
@Service
@RequiredArgsConstructor
public class WechatTransferConfirmQueryService {

    /// 终态状态(不可再确认收款)
    private static final Set<String> TERMINAL_STATES = Set.of("success", "close", "fail");

    private final WechatTransferOrderManager wechatTransferOrderManager;
    private final MerchantContextLoader merchantContextLoader;
    private final WechatDirectConfigAssembler wechatDirectConfigAssembler;
    private final WechatTransferConfigManager wechatTransferConfigManager;

    /// 查询确认收款信息
    ///
    /// @param transferNo 平台转账单号(URL 路径参数)
    /// @return 确认收款信息(mchId/appId/packageInfo 等)
    public WechatTransferConfirmResult queryByTransferNo(String transferNo) {
        // 跨租户查订单(收款人 C 端无商户上下文)
        WechatTransferOrder order = wechatTransferOrderManager.findByTransferNoNotTenant(transferNo)
                .orElseThrow(() -> new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                        "pay.error.transfer.notFound"));
        // 装载商户上下文, 后续通道配置查询走租户隔离
        merchantContextLoader.initMch(order.getMchNo());
        // 复用发起转账的凭证组装路径: 查转账配置 → buildTransferConfig
        WechatTransferConfig transferConfig = wechatTransferConfigManager
                .findByChannelMchNo(order.getChannelMchNo())
                .orElseThrow(() -> new BizInfoException(DaxPayErrorCode.CONFIG_NOT_EXIST,
                        "error.channel.wechat.transferConfigNotConfigured"));
        WechatSdkCredential credential = wechatDirectConfigAssembler.buildTransferConfig(
                order.getChannelMchNo(), transferConfig.getTransferAppRefId());
        boolean terminal = TERMINAL_STATES.contains(order.getStatus());
        return new WechatTransferConfirmResult()
                .setMchId(credential.getWxMchId())
                .setAppId(credential.getWxAppId())
                .setPackageInfo(terminal ? null : order.getTransferBody())
                .setAmount(order.getAmount())
                .setTitle(order.getTitle())
                .setStatus(order.getStatus())
                .setReceived(terminal);
    }
}
