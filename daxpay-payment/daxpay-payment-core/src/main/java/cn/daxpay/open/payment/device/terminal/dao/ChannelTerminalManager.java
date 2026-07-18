package cn.daxpay.open.payment.device.terminal.dao;

import cn.daxpay.open.payment.device.terminal.entity.ChannelTerminal;
import cn.daxpay.open.payment.device.terminal.param.ChannelTerminalQuery;
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

/// # 通道终端台账管理
@Slf4j
@Repository
@RequiredArgsConstructor
public class ChannelTerminalManager extends BaseManager<ChannelTerminalMapper, ChannelTerminal> {

    /// 分页
    public Page<ChannelTerminal> page(PageParam pageParam, ChannelTerminalQuery query) {
        Page<ChannelTerminal> mpPage = MpUtil.getMpPage(pageParam);
        QueryWrapper<ChannelTerminal> wrapper = QueryGenerator.generator(query);
        return this.page(mpPage, wrapper);
    }

    /// 商户下全部通道终端
    public List<ChannelTerminal> findAllByMchNo(String mchNo) {
        return lambdaQuery()
                .eq(ChannelTerminal::getMchNo, mchNo)
                .list();
    }
}
