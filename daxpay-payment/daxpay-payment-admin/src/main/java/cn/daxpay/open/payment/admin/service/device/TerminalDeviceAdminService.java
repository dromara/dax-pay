package cn.daxpay.open.payment.admin.service.device;

import cn.daxpay.open.payment.admin.param.device.TerminalChannelBindParam;
import cn.daxpay.open.payment.admin.param.device.TerminalDeviceParam;
import cn.daxpay.open.payment.device.terminal.dao.ChannelTerminalManager;
import cn.daxpay.open.payment.device.terminal.dao.TerminalChannelBindManager;
import cn.daxpay.open.payment.device.terminal.dao.TerminalDeviceManager;
import cn.daxpay.open.payment.device.terminal.entity.ChannelTerminal;
import cn.daxpay.open.payment.device.terminal.entity.TerminalChannelBind;
import cn.daxpay.open.payment.device.terminal.entity.TerminalDevice;
import cn.daxpay.open.payment.device.terminal.param.TerminalDeviceQuery;
import cn.daxpay.open.payment.device.terminal.result.ChannelTerminalResult;
import cn.daxpay.open.payment.device.terminal.result.TerminalDeviceResult;
import cn.daxpay.open.payment.merchant.dao.info.MerchantInfoManager;
import cn.daxpay.open.payment.merchant.dao.store.MchStoreInfoManager;
import cn.daxpay.open.payment.merchant.entity.store.MchStoreInfo;
import cn.daxpay.open.platform.common.mybatisplus.util.MpUtil;
import cn.daxpay.open.platform.common.translate.service.TransService;
import cn.daxpay.open.platform.core.code.CommonCode;
import cn.daxpay.open.platform.core.exception.BizException;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.daxpay.open.platform.core.exception.operation.OperationFailException;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/// # 系统终端管理(运营端)
///
/// 维护系统商户下的逻辑终端台账; 支持可选挂门店(1:N)、与通道终端多对多绑定。
/// 本期不调通道报备接口。
@Slf4j
@Service
@RequiredArgsConstructor
public class TerminalDeviceAdminService {

    private final TerminalDeviceManager terminalDeviceManager;
    private final TerminalChannelBindManager terminalChannelBindManager;
    private final ChannelTerminalManager channelTerminalManager;
    private final MerchantInfoManager merchantInfoManager;
    private final MchStoreInfoManager mchStoreInfoManager;
    private final TransService transService;

    /// 新增系统终端
    @Transactional(rollbackFor = Exception.class)
    public void add(TerminalDeviceParam param) {
        // 校验商户存在
        merchantInfoManager.findByMchNo(param.getMchNo())
                // 商户: 商户不存在
                .orElseThrow(() -> new BizException(CommonCode.FAIL_CODE, "error.payment.merchant.mchNotExist"));
        // 门店可空, 有值则校验同商户
        String storeNo = resolveStoreNo(param.getStoreNo(), param.getMchNo());
        TerminalDevice entity = new TerminalDevice();
        // 运营端写 MchBaseEntity 必须显式 setMchNo
        entity.setMchNo(param.getMchNo());
        entity.setTerminalNo(generateTerminalNo());
        entity.setName(param.getName());
        entity.setStoreNo(storeNo);
        entity.setEnable(param.getEnable());
        entity.setRemark(param.getRemark());
        terminalDeviceManager.save(entity);
    }

    /// 修改系统终端
    @Transactional(rollbackFor = Exception.class)
    public void update(TerminalDeviceParam param) {
        TerminalDevice entity = terminalDeviceManager.findById(param.getId())
                // 终端: 系统终端不存在
                .orElseThrow(() -> new DataNotExistException("error.device.terminal.systemNotFound"));
        String storeNo = resolveStoreNo(param.getStoreNo(), entity.getMchNo());
        entity.setName(param.getName());
        entity.setStoreNo(storeNo);
        entity.setEnable(param.getEnable());
        entity.setRemark(param.getRemark());
        terminalDeviceManager.updateById(entity);
    }

    /// 分页
    public PageResult<TerminalDeviceResult> page(PageParam pageParam, TerminalDeviceQuery query) {
        PageResult<TerminalDeviceResult> pageResult = MpUtil.toPageResult(terminalDeviceManager.page(pageParam, query));
        transService.translate(pageResult);
        return pageResult;
    }

    /// 详情
    public TerminalDeviceResult findById(Long id) {
        TerminalDeviceResult result = terminalDeviceManager.findById(id)
                .map(TerminalDevice::toResult)
                // 终端: 系统终端不存在
                .orElseThrow(() -> new DataNotExistException("error.device.terminal.systemNotFound"));
        transService.translate(result);
        return result;
    }

    /// 商户下系统终端列表(绑定下拉用)
    public List<TerminalDeviceResult> listByMchNo(String mchNo) {
        return terminalDeviceManager.findAllByMchNo(mchNo).stream()
                .map(TerminalDevice::toResult)
                .collect(Collectors.toList());
    }

    /// 删除系统终端(级联解绑)
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        TerminalDevice entity = terminalDeviceManager.findById(id)
                // 终端: 系统终端不存在
                .orElseThrow(() -> new DataNotExistException("error.device.terminal.systemNotFound"));
        terminalChannelBindManager.deleteBySystemTerminalNo(entity.getTerminalNo());
        terminalDeviceManager.deleteById(id);
    }

    /// 绑定通道终端
    @Transactional(rollbackFor = Exception.class)
    public void bind(TerminalChannelBindParam param) {
        TerminalDevice system = terminalDeviceManager.findByTerminalNo(param.getSystemTerminalNo())
                // 终端: 系统终端不存在
                .orElseThrow(() -> new DataNotExistException("error.device.terminal.systemNotFound"));
        ChannelTerminal channel = channelTerminalManager.findById(param.getChannelTerminalId())
                // 终端: 通道终端不存在
                .orElseThrow(() -> new DataNotExistException("error.device.terminal.channelNotFound"));
        if (!Objects.equals(system.getMchNo(), channel.getMchNo())) {
            // 终端: 系统终端与通道终端须属于同一商户
            throw new OperationFailException(CommonCode.FAIL_CODE, "error.device.terminal.mchMismatch");
        }
        if (terminalChannelBindManager.existsBind(param.getSystemTerminalNo(), param.getChannelTerminalId())) {
            // 终端: 绑定关系已存在
            throw new OperationFailException(CommonCode.FAIL_CODE, "error.device.terminal.bindExists");
        }
        TerminalChannelBind bind = new TerminalChannelBind();
        // 运营端写 MchBaseEntity 必须显式 setMchNo
        bind.setMchNo(system.getMchNo());
        bind.setSystemTerminalNo(param.getSystemTerminalNo());
        bind.setChannelTerminalId(param.getChannelTerminalId());
        terminalChannelBindManager.save(bind);
    }

    /// 解绑通道终端
    @Transactional(rollbackFor = Exception.class)
    public void unbind(TerminalChannelBindParam param) {
        if (!terminalChannelBindManager.existsBind(param.getSystemTerminalNo(), param.getChannelTerminalId())) {
            // 终端: 绑定关系不存在
            throw new OperationFailException(CommonCode.FAIL_CODE, "error.device.terminal.bindNotFound");
        }
        terminalChannelBindManager.deleteBind(param.getSystemTerminalNo(), param.getChannelTerminalId());
    }

    /// 查询系统终端已绑定的通道终端列表
    public List<ChannelTerminalResult> listBoundChannel(String terminalNo) {
        TerminalDevice system = terminalDeviceManager.findByTerminalNo(terminalNo)
                // 终端: 系统终端不存在
                .orElseThrow(() -> new DataNotExistException("error.device.terminal.systemNotFound"));
        List<Long> channelIds = terminalChannelBindManager.findBySystemTerminalNo(system.getTerminalNo()).stream()
                .map(TerminalChannelBind::getChannelTerminalId)
                .toList();
        if (channelIds.isEmpty()) {
            return List.of();
        }
        List<ChannelTerminalResult> results = channelTerminalManager.findAllByIds(channelIds).stream()
                .map(ChannelTerminal::toResult)
                .collect(Collectors.toList());
        transService.translate(results);
        return results;
    }

    /// 生成系统终端号 D + 16 位数字
    private String generateTerminalNo() {
        String terminalNo = "D" + RandomUtil.randomNumbers(16);
        for (int i = 0; i < 10; i++) {
            if (!terminalDeviceManager.existsByTerminalNo(terminalNo)) {
                return terminalNo;
            }
            terminalNo = "D" + RandomUtil.randomNumbers(16);
        }
        // 终端: 终端号生成失败
        throw new BizException(CommonCode.FAIL_CODE, "error.device.terminal.terminalNoGenFailed");
    }

    /// 解析门店号: 空返回 null; 非空须存在且归属商户
    private String resolveStoreNo(String storeNo, String mchNo) {
        if (StrUtil.isBlank(storeNo)) {
            return null;
        }
        MchStoreInfo store = mchStoreInfoManager.findByStoreNo(storeNo)
                // 商户: 门店不存在
                .orElseThrow(() -> new DataNotExistException("error.payment.merchant.storeNotFound"));
        if (!Objects.equals(store.getMchNo(), mchNo)) {
            // 商户: 门店不属于当前商户
            throw new OperationFailException(CommonCode.FAIL_CODE, "error.payment.merchant.storeNoMatch");
        }
        return storeNo;
    }
}
