package cn.daxpay.open.payment.merchant.service.device;

import cn.daxpay.open.payment.common.context.PaymentContext;
import cn.daxpay.open.payment.device.terminal.dao.ChannelTerminalManager;
import cn.daxpay.open.payment.device.terminal.dao.TerminalChannelBindManager;
import cn.daxpay.open.payment.device.terminal.dao.TerminalDeviceManager;
import cn.daxpay.open.payment.device.terminal.entity.ChannelTerminal;
import cn.daxpay.open.payment.device.terminal.entity.TerminalChannelBind;
import cn.daxpay.open.payment.device.terminal.entity.TerminalDevice;
import cn.daxpay.open.payment.device.terminal.param.TerminalDeviceQuery;
import cn.daxpay.open.payment.device.terminal.result.ChannelTerminalResult;
import cn.daxpay.open.payment.device.terminal.result.TerminalDeviceResult;
import cn.daxpay.open.payment.merchant.dao.store.MchStoreInfoManager;
import cn.daxpay.open.payment.merchant.entity.store.MchStoreInfo;
import cn.daxpay.open.payment.merchant.param.device.TerminalChannelBindParam;
import cn.daxpay.open.payment.merchant.param.device.TerminalDeviceParam;
import cn.daxpay.open.platform.common.mybatisplus.util.MpUtil;
import cn.daxpay.open.platform.common.translate.service.TransService;
import cn.daxpay.open.platform.core.code.CommonCode;
import cn.daxpay.open.platform.core.exception.BizException;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.daxpay.open.platform.core.exception.config.ConfigErrorException;
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

/// # 系统终端管理（商户端）
///
/// 逻辑对齐运营端 [TerminalDeviceAdminService]，商户号强制取自 [PaymentContext]。
@Slf4j
@Service
@RequiredArgsConstructor
public class MchTerminalDeviceService {

    private final TerminalDeviceManager terminalDeviceManager;
    private final TerminalChannelBindManager terminalChannelBindManager;
    private final ChannelTerminalManager channelTerminalManager;
    private final MchStoreInfoManager mchStoreInfoManager;
    private final TransService transService;
    private final PaymentContext paymentContext;

    /// 当前登录商户号
    private String requireMchNo() {
        String mchNo = paymentContext.getMchNo();
        if (StrUtil.isBlank(mchNo)) {
            // 终端: 商户上下文未装载, 无法进行数据隔离操作
            throw new BizInfoException(CommonCode.FAIL_CODE, "pay.error.assist.mchContextMissing");
        }
        return mchNo;
    }

    /// 校验系统终端归属
    private void checkTerminal(TerminalDevice entity) {
        if (!Objects.equals(entity.getMchNo(), requireMchNo())) {
            // 门店/资源不属于当前商户（复用既有文案键）
            throw new ConfigErrorException("error.payment.merchant.storeNoMatch");
        }
    }

    /// 新增系统终端
    @Transactional(rollbackFor = Exception.class)
    public void add(TerminalDeviceParam param) {
        String mchNo = requireMchNo();
        String storeNo = resolveStoreNo(param.getStoreNo(), mchNo);
        TerminalDevice entity = new TerminalDevice();
        // 商户端写 MchBaseEntity 必须显式 setMchNo
        entity.setMchNo(mchNo);
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
        this.checkTerminal(entity);
        String storeNo = resolveStoreNo(param.getStoreNo(), entity.getMchNo());
        entity.setName(param.getName());
        entity.setStoreNo(storeNo);
        entity.setEnable(param.getEnable());
        entity.setRemark(param.getRemark());
        terminalDeviceManager.updateById(entity);
    }

    /// 分页（强制当前商户）
    public PageResult<TerminalDeviceResult> page(PageParam pageParam, TerminalDeviceQuery query) {
        query.setMchNo(requireMchNo());
        PageResult<TerminalDeviceResult> pageResult = MpUtil.toPageResult(terminalDeviceManager.page(pageParam, query));
        transService.translate(pageResult);
        return pageResult;
    }

    /// 详情
    public TerminalDeviceResult findById(Long id) {
        TerminalDevice entity = terminalDeviceManager.findById(id)
                // 终端: 系统终端不存在
                .orElseThrow(() -> new DataNotExistException("error.device.terminal.systemNotFound"));
        this.checkTerminal(entity);
        TerminalDeviceResult result = entity.toResult();
        transService.translate(result);
        return result;
    }

    /// 当前商户系统终端列表
    public List<TerminalDeviceResult> list() {
        return terminalDeviceManager.findAllByMchNo(requireMchNo()).stream()
                .map(TerminalDevice::toResult)
                .collect(Collectors.toList());
    }

    /// 删除系统终端(级联解绑)
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        TerminalDevice entity = terminalDeviceManager.findById(id)
                // 终端: 系统终端不存在
                .orElseThrow(() -> new DataNotExistException("error.device.terminal.systemNotFound"));
        this.checkTerminal(entity);
        terminalChannelBindManager.deleteBySystemTerminalNo(entity.getTerminalNo());
        terminalDeviceManager.deleteById(id);
    }

    /// 绑定通道终端
    @Transactional(rollbackFor = Exception.class)
    public void bind(TerminalChannelBindParam param) {
        TerminalDevice system = terminalDeviceManager.findByTerminalNo(param.getSystemTerminalNo())
                // 终端: 系统终端不存在
                .orElseThrow(() -> new DataNotExistException("error.device.terminal.systemNotFound"));
        this.checkTerminal(system);
        ChannelTerminal channel = channelTerminalManager.findById(param.getChannelTerminalId())
                // 终端: 通道终端不存在
                .orElseThrow(() -> new DataNotExistException("error.device.terminal.channelNotFound"));
        if (!Objects.equals(system.getMchNo(), channel.getMchNo())
                || !Objects.equals(channel.getMchNo(), requireMchNo())) {
            // 终端: 系统终端与通道终端须属于同一商户
            throw new OperationFailException(CommonCode.FAIL_CODE, "error.device.terminal.mchMismatch");
        }
        if (terminalChannelBindManager.existsBind(param.getSystemTerminalNo(), param.getChannelTerminalId())) {
            // 终端: 绑定关系已存在
            throw new OperationFailException(CommonCode.FAIL_CODE, "error.device.terminal.bindExists");
        }
        TerminalChannelBind bind = new TerminalChannelBind();
        bind.setMchNo(system.getMchNo());
        bind.setSystemTerminalNo(param.getSystemTerminalNo());
        bind.setChannelTerminalId(param.getChannelTerminalId());
        terminalChannelBindManager.save(bind);
    }

    /// 解绑通道终端
    @Transactional(rollbackFor = Exception.class)
    public void unbind(TerminalChannelBindParam param) {
        TerminalDevice system = terminalDeviceManager.findByTerminalNo(param.getSystemTerminalNo())
                // 终端: 系统终端不存在
                .orElseThrow(() -> new DataNotExistException("error.device.terminal.systemNotFound"));
        this.checkTerminal(system);
        if (!terminalChannelBindManager.existsBind(param.getSystemTerminalNo(), param.getChannelTerminalId())) {
            // 终端: 绑定关系不存在
            throw new OperationFailException(CommonCode.FAIL_CODE, "error.device.terminal.bindNotFound");
        }
        terminalChannelBindManager.deleteBind(param.getSystemTerminalNo(), param.getChannelTerminalId());
    }

    /// 已绑定的通道终端列表
    public List<ChannelTerminalResult> listBoundChannel(String terminalNo) {
        TerminalDevice system = terminalDeviceManager.findByTerminalNo(terminalNo)
                // 终端: 系统终端不存在
                .orElseThrow(() -> new DataNotExistException("error.device.terminal.systemNotFound"));
        this.checkTerminal(system);
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

    /// 当前商户下通道终端列表（绑定候选）
    public List<ChannelTerminalResult> listChannelTerminal() {
        String mchNo = requireMchNo();
        List<ChannelTerminalResult> results = channelTerminalManager.findAllByMchNo(mchNo).stream()
                .map(ChannelTerminal::toResult)
                .collect(Collectors.toList());
        transService.translate(results);
        return results;
    }

    private String generateTerminalNo() {
        String terminalNo = "D" + RandomUtil.randomNumbers(16);
        for (int i = 0; i < 10; i++) {
            if (!terminalDeviceManager.existsByTerminalNo(terminalNo)) {
                return terminalNo;
            }
            terminalNo = "D" + RandomUtil.randomNumbers(16);
        }
        // 终端: 终端号生成失败（重试 10 次仍冲突）
        throw new BizException(CommonCode.FAIL_CODE, "error.device.terminal.terminalNoGenFailed");
    }

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
