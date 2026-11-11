package cn.daxpay.open.payment.admin.service.device;

import cn.daxpay.open.payment.admin.param.device.TerminalChannelBindParam;
import cn.daxpay.open.payment.admin.param.device.TerminalDeviceParam;
import cn.daxpay.open.payment.device.terminal.dao.ChannelTerminalManager;
import cn.daxpay.open.payment.device.terminal.dao.TerminalChannelBindManager;
import cn.daxpay.open.payment.device.terminal.dao.TerminalDeviceManager;
import cn.daxpay.open.payment.device.terminal.entity.DeviceChannelTerminal;
import cn.daxpay.open.payment.device.terminal.entity.DeviceTerminalChannelBind;
import cn.daxpay.open.payment.device.terminal.entity.DeviceTerminal;
import cn.daxpay.open.payment.device.terminal.param.TerminalBindStoreParam;
import cn.daxpay.open.payment.device.terminal.param.TerminalDeviceQuery;
import cn.daxpay.open.payment.device.terminal.result.ChannelTerminalResult;
import cn.daxpay.open.payment.device.terminal.result.TerminalDeviceResult;
import cn.daxpay.open.payment.device.terminal.service.TerminalChannelFillSupport;
import cn.daxpay.open.payment.merchant.dao.info.MerchantInfoManager;
import cn.daxpay.open.payment.merchant.dao.store.MchStoreInfoManager;
import cn.daxpay.open.payment.merchant.entity.store.MchStoreInfo;
import cn.daxpay.open.platform.common.mybatisplus.util.MpUtil;
import cn.daxpay.open.platform.common.translate.service.TransService;
import cn.daxpay.open.platform.core.code.CommonCode;
import cn.daxpay.open.platform.core.exception.BizException;
import cn.daxpay.open.platform.core.exception.BizInfoException;
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

/// # 商户终端管理(运营端)
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
    private final TerminalChannelFillSupport terminalChannelFillSupport;
    private final TransService transService;

    /// 新增商户终端
    @Transactional(rollbackFor = Exception.class)
    public void add(TerminalDeviceParam param) {
        // 校验商户存在
        merchantInfoManager.findByMchNo(param.getMchNo())
                // 商户: 商户不存在
                .orElseThrow(() -> new BizException(CommonCode.FAIL_CODE, "error.payment.merchant.mchNotExist"));
        // 门店可空, 有值则校验同商户
        String storeNo = resolveStoreNo(param.getStoreNo(), param.getMchNo());
        DeviceTerminal entity = new DeviceTerminal();
        // 运营端写 MchBaseEntity 必须显式 setMchNo
        entity.setMchNo(param.getMchNo());
        entity.setTerminalNo(generateTerminalNo());
        entity.setName(param.getName());
        entity.setStoreNo(storeNo);
        entity.setEnable(param.getEnable());
        entity.setRemark(param.getRemark());
        terminalDeviceManager.save(entity);
    }

    /// 修改商户终端
    @Transactional(rollbackFor = Exception.class)
    public void update(TerminalDeviceParam param) {
        DeviceTerminal entity = terminalDeviceManager.findById(param.getId())
                // 终端: 商户终端不存在
                .orElseThrow(() -> new DataNotExistException("error.device.terminal.systemNotFound"));
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

    /// 分页
    public PageResult<TerminalDeviceResult> page(PageParam pageParam, TerminalDeviceQuery query) {
        PageResult<TerminalDeviceResult> pageResult = MpUtil.toPageResult(terminalDeviceManager.page(pageParam, query));
        transService.translate(pageResult);
        return pageResult;
    }

    /// 商户终端总览分页(运营端「设备管理 → 商户终端」只读视图)
    ///
    /// 与商户管理下的商户终端页语义隔离: 不分商户维度收口, 批量带出每个终端关联的通道终端,
    /// 不改动 [#page] 的既有出参。
    public PageResult<TerminalDeviceResult> overviewPage(PageParam pageParam, TerminalDeviceQuery query) {
        PageResult<TerminalDeviceResult> pageResult = MpUtil.toPageResult(terminalDeviceManager.page(pageParam, query));
        terminalChannelFillSupport.fill(pageResult.getRecords());
        transService.translate(pageResult);
        return pageResult;
    }

    /// 详情
    public TerminalDeviceResult findById(Long id) {
        TerminalDeviceResult result = terminalDeviceManager.findById(id)
                .map(DeviceTerminal::toResult)
                // 终端: 商户终端不存在
                .orElseThrow(() -> new DataNotExistException("error.device.terminal.systemNotFound"));
        transService.translate(result);
        return result;
    }

    /// 商户下商户终端列表(绑定下拉用)
    public List<TerminalDeviceResult> listByMchNo(String mchNo) {
        return terminalDeviceManager.findAllByMchNo(mchNo).stream()
                .map(DeviceTerminal::toResult)
                .collect(Collectors.toList());
    }

    /// 删除商户终端(级联解绑)
    ///
    /// 门店默认终端禁止删除(对齐默认门店禁删), 需先取消默认或转交其他终端。
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        DeviceTerminal entity = terminalDeviceManager.findById(id)
                // 终端: 商户终端不存在
                .orElseThrow(() -> new DataNotExistException("error.device.terminal.systemNotFound"));
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
        DeviceTerminal entity = terminalDeviceManager.findById(id)
                // 终端: 商户终端不存在
                .orElseThrow(() -> new DataNotExistException("error.device.terminal.systemNotFound"));
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
        DeviceTerminal entity = terminalDeviceManager.findById(id)
                // 终端: 商户终端不存在
                .orElseThrow(() -> new DataNotExistException("error.device.terminal.systemNotFound"));
        entity.setDefaultTerminal(false);
        terminalDeviceManager.updateById(entity);
    }

    /// 批量绑定门店(门店工作台快捷绑定; 单事务一次更新)
    ///
    /// 门店归属只校验一次, 任一终端不存在则整批拒绝; 终端须同属一个商户。
    /// 迁移的终端若带默认标记, 一并清除——默认终端同门店唯一由 [setDefault] 先清后设保证,
    /// 门店变更后原标记必须失效(否则原门店悬挂默认/新门店多默认)。
    @Transactional(rollbackFor = Exception.class)
    public void bindStoreBatch(TerminalBindStoreParam param) {
        List<DeviceTerminal> terminals = terminalDeviceManager.findAllByIds(param.getTerminalIds());
        if (terminals.size() != param.getTerminalIds().stream().distinct().count()) {
            // 终端: 部分商户终端不存在
            throw new DataNotExistException("error.device.terminal.batchSystemNotFound");
        }
        String mchNo = terminals.getFirst().getMchNo();
        for (DeviceTerminal terminal : terminals) {
            if (!Objects.equals(terminal.getMchNo(), mchNo)) {
                // 终端: 商户终端须属于同一商户
                throw new OperationFailException(CommonCode.FAIL_CODE, "error.device.terminal.batchMchMismatch");
            }
        }
        String storeNo = resolveStoreNo(param.getStoreNo(), mchNo);
        terminalDeviceManager.lambdaUpdate()
                .set(DeviceTerminal::getStoreNo, storeNo)
                .set(DeviceTerminal::getDefaultTerminal, false)
                .in(DeviceTerminal::getId, param.getTerminalIds())
                .update();
    }

    /// 绑定通道终端
    @Transactional(rollbackFor = Exception.class)
    public void bind(TerminalChannelBindParam param) {
        DeviceTerminal system = terminalDeviceManager.findByTerminalNo(param.getSystemTerminalNo())
                // 终端: 商户终端不存在
                .orElseThrow(() -> new DataNotExistException("error.device.terminal.systemNotFound"));
        DeviceChannelTerminal channel = channelTerminalManager.findById(param.getChannelTerminalId())
                // 终端: 通道终端不存在
                .orElseThrow(() -> new DataNotExistException("error.device.terminal.channelNotFound"));
        if (!Objects.equals(system.getMchNo(), channel.getMchNo())) {
            // 终端: 商户终端与通道终端须属于同一商户
            throw new OperationFailException(CommonCode.FAIL_CODE, "error.device.terminal.mchMismatch");
        }
        if (terminalChannelBindManager.existsBind(param.getSystemTerminalNo(), param.getChannelTerminalId())) {
            // 终端: 绑定关系已存在
            throw new OperationFailException(CommonCode.FAIL_CODE, "error.device.terminal.bindExists");
        }
        DeviceTerminalChannelBind bind = new DeviceTerminalChannelBind();
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

    /// 查询商户终端已绑定的通道终端列表
    public List<ChannelTerminalResult> listBoundChannel(String terminalNo) {
        DeviceTerminal system = terminalDeviceManager.findByTerminalNo(terminalNo)
                // 终端: 商户终端不存在
                .orElseThrow(() -> new DataNotExistException("error.device.terminal.systemNotFound"));
        List<Long> channelIds = terminalChannelBindManager.findBySystemTerminalNo(system.getTerminalNo()).stream()
                .map(DeviceTerminalChannelBind::getChannelTerminalId)
                .toList();
        if (channelIds.isEmpty()) {
            return List.of();
        }
        List<ChannelTerminalResult> results = channelTerminalManager.findAllByIds(channelIds).stream()
                .map(DeviceChannelTerminal::toResult)
                .collect(Collectors.toList());
        transService.translate(results);
        return results;
    }

    /// 生成商户终端号 D + 16 位数字
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
