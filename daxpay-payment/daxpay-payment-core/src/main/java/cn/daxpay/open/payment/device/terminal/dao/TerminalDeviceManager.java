package cn.daxpay.open.payment.device.terminal.dao;

import cn.daxpay.open.payment.device.terminal.entity.TerminalDevice;
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
public class TerminalDeviceManager extends BaseManager<TerminalDeviceMapper, TerminalDevice> {

    /// 分页
    public Page<TerminalDevice> page(PageParam pageParam, TerminalDeviceQuery query) {
        Page<TerminalDevice> mpPage = MpUtil.getMpPage(pageParam);
        QueryWrapper<TerminalDevice> wrapper = QueryGenerator.generator(query);
        return this.page(mpPage, wrapper);
    }

    /// 按终端编码查询
    public Optional<TerminalDevice> findByTerminalNo(String terminalNo) {
        return findByField(TerminalDevice::getTerminalNo, terminalNo);
    }

    /// 终端编码是否已存在
    public boolean existsByTerminalNo(String terminalNo) {
        return existedByField(TerminalDevice::getTerminalNo, terminalNo);
    }

    /// 商户下全部系统终端
    public List<TerminalDevice> findAllByMchNo(String mchNo) {
        return lambdaQuery()
                .eq(TerminalDevice::getMchNo, mchNo)
                .list();
    }

    /// 商户下启用的系统终端
    public List<TerminalDevice> findEnabledByMchNo(String mchNo) {
        return lambdaQuery()
                .eq(TerminalDevice::getMchNo, mchNo)
                .eq(TerminalDevice::getEnable, true)
                .list();
    }

    /// 查询门店默认终端(商户 + 门店维度, 仅启用; 支付未指定终端号时回落使用)
    ///
    /// 对齐 [cn.daxpay.open.payment.merchant.dao.store.MchStoreInfoManager#findDefaultByMchNo] 范式;
    /// 默认终端唯一由部分唯一索引 uk_pay_terminal_device_store_default 兜底。
    public Optional<TerminalDevice> findDefaultByStoreNo(String mchNo, String storeNo) {
        return lambdaQuery()
                .eq(TerminalDevice::getMchNo, mchNo)
                .eq(TerminalDevice::getStoreNo, storeNo)
                .eq(TerminalDevice::getDefaultTerminal, true)
                .eq(TerminalDevice::getEnable, true)
                .oneOpt();
    }

    /// 查询门店默认终端(不限启用态, 门店概况展示用)
    ///
    /// 与 [#findDefaultByStoreNo](支付回落用, 仅启用终端)区分: 门店工作台展示台账事实, 默认终端停用也如实展示。
    public Optional<TerminalDevice> findDefaultByStoreNoAnyStatus(String storeNo) {
        return lambdaQuery()
                .eq(TerminalDevice::getStoreNo, storeNo)
                .eq(TerminalDevice::getDefaultTerminal, true)
                .oneOpt();
    }

    /// 统计门店下系统终端数(不限启用态; 门店概况聚合用, 逻辑删除自动过滤)
    public long countByStoreNo(String storeNo) {
        return lambdaQuery()
                .eq(TerminalDevice::getStoreNo, storeNo)
                .count();
    }

    /// 清除门店下全部默认终端标记(设默认前先清后设, 部分唯一索引兜底)
    public void clearDefaultByStoreNo(String storeNo) {
        lambdaUpdate()
                .eq(TerminalDevice::getStoreNo, storeNo)
                .set(TerminalDevice::getDefaultTerminal, false)
                .update();
    }
}
