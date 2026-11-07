package cn.daxpay.open.payment.merchant.service.device;

import cn.daxpay.open.payment.common.context.PaymentContext;
import cn.daxpay.open.payment.device.terminal.dao.ChannelTerminalManager;
import cn.daxpay.open.payment.device.terminal.dao.TerminalChannelBindManager;
import cn.daxpay.open.payment.device.terminal.dao.TerminalDeviceManager;
import cn.daxpay.open.payment.device.terminal.entity.ChannelTerminal;
import cn.daxpay.open.payment.device.terminal.entity.TerminalChannelBind;
import cn.daxpay.open.payment.device.terminal.entity.TerminalDevice;
import cn.daxpay.open.payment.device.terminal.param.TerminalBindStoreParam;
import cn.daxpay.open.payment.device.terminal.param.TerminalDeviceQuery;
import cn.daxpay.open.payment.device.terminal.result.ChannelTerminalResult;
import cn.daxpay.open.payment.device.terminal.result.TerminalDeviceResult;
import cn.daxpay.open.payment.device.terminal.service.TerminalChannelFillSupport;
import cn.daxpay.open.payment.merchant.dao.store.MchStoreInfoManager;
import cn.daxpay.open.payment.merchant.entity.store.MchStoreInfo;
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
    private final TerminalChannelFillSupport terminalChannelFillSupport;
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
        String oldStoreNo = entity.getStoreNo();
        String storeNo = resolveStoreNo(param.getStoreNo(), entity.getMchNo());
        entity.setName(param.getName());
        entity.setStoreNo(storeNo);
        entity.setEnable(param.getEnable());
        entity.setRemark(param.getRemark());
        // 门店变更后默认标记必须失效(默认终端同门店唯一, 由 setDefault 先清后设保证)
        if (!Objects.equals(StrUtil.blankToDefault(oldStoreNo, null), StrUtil.blankToDefault(storeNo, null))) {
            entity.setDefaultTerminal(false);
        }
        terminalDeviceManager.updateById(entity);
    }

    /// 分页（强制当前商户）
    public PageResult<TerminalDeviceResult> page(PageParam pageParam, TerminalDeviceQuery query) {
        query.setMchNo(requireMchNo());
        PageResult<TerminalDeviceResult> pageResult = MpUtil.toPageResult(terminalDeviceManager.page(pageParam, query));
        // 批量回填关联通道终端(一次查绑定、一次查通道终端, 避免 N+1)
        terminalChannelFillSupport.fill(pageResult.getRecords());
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
    ///
    /// 门店默认终端禁止删除(对齐默认门店禁删), 需先取消默认或转交其他终端。
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        TerminalDevice entity = terminalDeviceManager.findById(id)
                // 终端: 系统终端不存在
                .orElseThrow(() -> new DataNotExistException("error.device.terminal.systemNotFound"));
        this.checkTerminal(entity);
        if (Boolean.TRUE.equals(entity.getDefaultTerminal())) {
            // 终端: 门店默认终端不可删除, 请先取消默认或转交其他终端
            throw new BizInfoException(CommonCode.FAIL_CODE, "error.device.terminal.defaultCannotDelete");
        }
        terminalChannelBindManager.deleteBySystemTerminalNo(entity.getTerminalNo());
        terminalDeviceManager.deleteById(id);
    }

    /// 设为门店默认终端(同门店先清后设)
    ///
    /// 仅已挂载门店的终端可设默认; 对齐 [cn.daxpay.open.payment.merchant.service.store.MchStoreInfoService#setDefault] 范式。
    @Transactional(rollbackFor = Exception.class)
    public void setDefault(Long id) {
        TerminalDevice entity = terminalDeviceManager.findById(id)
                // 终端: 系统终端不存在
                .orElseThrow(() -> new DataNotExistException("error.device.terminal.systemNotFound"));
        this.checkTerminal(entity);
        if (StrUtil.isBlank(entity.getStoreNo())) {
            // 终端: 终端未挂载门店, 无法设为默认
            throw new BizInfoException(CommonCode.FAIL_CODE, "error.device.terminal.defaultStoreNotBound");
        }
        terminalDeviceManager.clearDefaultByStoreNo(entity.getStoreNo());
        // clearDefault 后重新加载, 避免 version 冲突
        entity = terminalDeviceManager.findById(id)
                .orElseThrow(() -> new DataNotExistException("error.device.terminal.systemNotFound"));
        entity.setDefaultTerminal(true);
        terminalDeviceManager.updateById(entity);
    }

    /// 取消门店默认终端
    @Transactional(rollbackFor = Exception.class)
    public void clearDefault(Long id) {
        TerminalDevice entity = terminalDeviceManager.findById(id)
                // 终端: 系统终端不存在
                .orElseThrow(() -> new DataNotExistException("error.device.terminal.systemNotFound"));
        this.checkTerminal(entity);
        entity.setDefaultTerminal(false);
        terminalDeviceManager.updateById(entity);
    }

    /// 批量绑定门店(门店工作台勾选多台; 单事务一次更新)
    ///
    /// 逻辑对齐运营端 [TerminalDeviceAdminService#bindStoreBatch], 但归属校验换成当前登录商户:
    /// 每台终端均须属于当前商户(逐台校验), 门店归属由 [resolveStoreNo] 校验一次;
    /// 任一终端不存在则整批拒绝。迁移的终端若带默认标记一并清除——默认终端同门店唯一,
    /// 门店变更后原标记必须失效(否则原门店悬挂默认/新门店多默认)。
    @Transactional(rollbackFor = Exception.class)
    public void bindStoreBatch(TerminalBindStoreParam param) {
        List<TerminalDevice> terminals = terminalDeviceManager.findAllByIds(param.getTerminalIds());
        if (terminals.size() != param.getTerminalIds().stream().distinct().count()) {
            // 终端: 部分系统终端不存在
            throw new DataNotExistException("error.device.terminal.batchSystemNotFound");
        }
        for (TerminalDevice terminal : terminals) {
            this.checkTerminal(terminal);
        }
        String mchNo = requireMchNo();
        String storeNo = resolveStoreNo(param.getStoreNo(), mchNo);
        terminalDeviceManager.lambdaUpdate()
                .set(TerminalDevice::getStoreNo, storeNo)
                .set(TerminalDevice::getDefaultTerminal, false)
                .in(TerminalDevice::getId, param.getTerminalIds())
                .update();
    }

    /// 已绑定的通道终端列表(只读)
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
