package cn.daxpay.open.payment.core.trade.dao;

import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import cn.daxpay.open.platform.common.mybatisplus.query.generator.QueryGenerator;
import cn.daxpay.open.platform.common.mybatisplus.util.MpUtil;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.payment.core.trade.entity.PayRefundOrder;
import cn.daxpay.open.payment.core.trade.param.PayRefundOrderQuery;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/// # 退款订单管理器
///
@Repository
public class PayRefundOrderManager extends BaseManager<PayRefundOrderMapper, PayRefundOrder> {

    /// 根据退款号查询
    public Optional<PayRefundOrder> findByRefundNo(String refundNo) {
        return findByField(PayRefundOrder::getRefundNo, refundNo);
    }

    /// 根据原支付订单号查询退款订单数量(用于判断是否已退款)
    public long countByOrderNo(String orderNo) {
        return lambdaQuery()
                .eq(PayRefundOrder::getOrderNo, orderNo)
                .count();
    }

    /// 分页查询(管理端), 默认按创建时间倒序
    public Page<PayRefundOrder> page(PageParam pageParam, PayRefundOrderQuery query) {
        Page<PayRefundOrder> mpPage = MpUtil.getMpPage(pageParam);
        QueryWrapper<PayRefundOrder> wrapper = QueryGenerator.generator(query);
        // 默认按创建时间倒序
        wrapper.orderByDesc("create_time");
        return this.page(mpPage, wrapper);
    }
}
