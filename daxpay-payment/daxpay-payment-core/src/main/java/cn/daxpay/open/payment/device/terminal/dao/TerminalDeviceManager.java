package cn.daxpay.open.payment.device.terminal.dao;

import cn.daxpay.open.payment.device.terminal.entity.DeviceTerminal;
import cn.daxpay.open.payment.device.terminal.param.TerminalDeviceQuery;
import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import cn.daxpay.open.platform.common.mybatisplus.query.generator.QueryGenerator;
import cn.daxpay.open.platform.common.mybatisplus.util.MpUtil;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/// # 系统终端管理
@Slf4j
@Repository
@RequiredArgsConstructor
public class TerminalDeviceManager extends BaseManager<TerminalDeviceMapper, DeviceTerminal> {

    /// 分页
    public Page<DeviceTerminal> page(PageParam pageParam, TerminalDeviceQuery query) {
        Page<DeviceTerminal> mpPage = MpUtil.getMpPage(pageParam);
        QueryWrapper<DeviceTerminal> wrapper = QueryGenerator.generator(query);
        return this.page(mpPage, wrapper);
    }

    /// 按终端编码查询
    public Optional<DeviceTerminal> findByTerminalNo(String terminalNo) {
        return findByField(DeviceTerminal::getTerminalNo, terminalNo);
    }

    /// 终端编码是否已存在
    public boolean existsByTerminalNo(String terminalNo) {
        return existedByField(DeviceTerminal::getTerminalNo, terminalNo);
    }

    /// 商户下全部系统终端
    public List<DeviceTerminal> findAllByMchNo(String mchNo) {
        return lambdaQuery()
                .eq(DeviceTerminal::getMchNo, mchNo)
                .list();
    }

    /// 商户下启用的系统终端
    public List<DeviceTerminal> findEnabledByMchNo(String mchNo) {
        return lambdaQuery()
                .eq(DeviceTerminal::getMchNo, mchNo)
                .eq(DeviceTerminal::getEnable, true)
                .list();
    }

    /// 查询门店默认终端(商户 + 门店维度, 仅启用; 支付未指定终端号时回落使用)
    ///
    /// 对齐 [cn.daxpay.open.payment.merchant.dao.store.MchStoreInfoManager#findDefaultByMchNo] 范式;
    /// 默认终端唯一由部分唯一索引 uk_device_terminal_store_default 兜底。
    public Optional<DeviceTerminal> findDefaultByStoreNo(String mchNo, String storeNo) {
        return lambdaQuery()
                .eq(DeviceTerminal::getMchNo, mchNo)
                .eq(DeviceTerminal::getStoreNo, storeNo)
                .eq(DeviceTerminal::getDefaultTerminal, true)
                .eq(DeviceTerminal::getEnable, true)
                .oneOpt();
    }

    /// 查询门店默认终端(不限启用态, 门店概况展示用)
    ///
    /// 与 [#findDefaultByStoreNo](支付回落用, 仅启用终端)区分: 门店工作台展示台账事实, 默认终端停用也如实展示。
    public Optional<DeviceTerminal> findDefaultByStoreNoAnyStatus(String storeNo) {
        return lambdaQuery()
                .eq(DeviceTerminal::getStoreNo, storeNo)
                .eq(DeviceTerminal::getDefaultTerminal, true)
                .oneOpt();
    }

    /// 统计门店下系统终端数(不限启用态; 门店概况聚合用, 逻辑删除自动过滤)
    public long countByStoreNo(String storeNo) {
        return lambdaQuery()
                .eq(DeviceTerminal::getStoreNo, storeNo)
                .count();
    }

    /// 统计商户下系统终端数(存量总数, 不限启用态; 运营端商户工作台磁贴计数用, 逻辑删除自动过滤)
    ///
    /// 显式按 mchNo 过滤, 不依赖租户自动拼接: 运营端无商户上下文(租户拦截整体放开)。
    public long countByMchNo(String mchNo) {
        return lambdaQuery()
                .eq(DeviceTerminal::getMchNo, mchNo)
                .count();
    }

    /// 清除门店下全部默认终端标记(设默认前先清后设, 部分唯一索引兜底)
    public void clearDefaultByStoreNo(String storeNo) {
        lambdaUpdate()
                .eq(DeviceTerminal::getStoreNo, storeNo)
                .set(DeviceTerminal::getDefaultTerminal, false)
                .update();
    }
}
