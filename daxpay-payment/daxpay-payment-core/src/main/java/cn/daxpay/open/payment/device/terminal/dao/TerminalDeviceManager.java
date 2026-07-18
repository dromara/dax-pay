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
}
