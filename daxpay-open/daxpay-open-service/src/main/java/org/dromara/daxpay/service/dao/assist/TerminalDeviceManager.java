package org.dromara.daxpay.service.dao.assist;

import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.common.mybatisplus.query.generator.QueryGenerator;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.core.rest.param.PageParam;
import org.dromara.daxpay.service.entity.assist.TerminalDevice;
import org.dromara.daxpay.service.param.termina.TerminalDeviceQuery;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 支付终端设备管理
 * @author xxm
 * @since 2025/3/7
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class TerminalDeviceManager extends BaseManager<TerminalDeviceMapper, TerminalDevice> {

    /**
     * 分页
     */
    public Page<TerminalDevice> page(PageParam pageParam, TerminalDeviceQuery query){
        var mpPage = MpUtil.getMpPage(pageParam, TerminalDevice.class);
        QueryWrapper<TerminalDevice> generator = QueryGenerator.generator(query);
        return this.page(mpPage,generator);
    }

    /**
     * 根据编号查询终端信息
     */
    public Optional<TerminalDevice> findByNo(String terminalNo){
        return this.lambdaQuery()
                .eq(TerminalDevice::getTerminalNo,terminalNo)
                .oneOpt();
    }
}
