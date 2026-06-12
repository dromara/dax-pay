package org.dromara.daxpay.payment.pay.dao.masterdata.channel;

import org.dromara.daxpay.platform.common.mybatisplus.impl.BaseManager;
import org.dromara.daxpay.platform.common.mybatisplus.util.MpUtil;
import org.dromara.daxpay.platform.core.rest.param.PageParam;
import org.dromara.daxpay.payment.pay.entity.masterdata.channel.PayChannel;
import org.dromara.daxpay.payment.pay.param.channel.PayChannelQuery;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.dromara.daxpay.platform.common.mybatisplus.query.generator.QueryGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/// # 支付通道
///
@Slf4j
@Repository
@RequiredArgsConstructor
public class PayChannelManager extends BaseManager<PayChannelMapper, PayChannel> {

    /// 根据通道编码查询
    public Optional<PayChannel> findByCode(String code) {
        return lambdaQuery()
                .eq(PayChannel::getCode, code)
                .oneOpt();
    }

    /// 分页
    public Page<PayChannel> page(PageParam pageParam, PayChannelQuery query) {
        Page<PayChannel> mpPage = MpUtil.getMpPage(pageParam);
        QueryWrapper<PayChannel> generator = QueryGenerator.generator(query);
        return page(mpPage, generator);
    }

    /// 查询所有（按排序号升序）
    public List<PayChannel> listAllOrdered() {
        return lambdaQuery()
                .orderByAsc(PayChannel::getSortNo)
                .list();
    }
}