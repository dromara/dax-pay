package cn.bootx.platform.daxpay.service.core.order.refund.dao;

import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.common.query.generator.QueryGenerator;
import cn.bootx.platform.daxpay.service.core.order.refund.entity.PayRefundOrder;
import cn.bootx.platform.daxpay.service.param.order.PayRefundOrderQuery;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 支付退款订单管理
 * @author xxm
 * @since 2022/3/2
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class PayRefundOrderManager extends BaseManager<PayRefundOrderMapper, PayRefundOrder> {

    /**
     * 分页
     */
    public Page<PayRefundOrder> page(PageParam pageParam, PayRefundOrderQuery query) {
        Page<PayRefundOrder> mpPage = MpUtil.getMpPage(pageParam, PayRefundOrder.class);
        QueryWrapper<PayRefundOrder> generator = QueryGenerator.generator(query);
        return page(mpPage,generator);
    }

    /**
     * 根据退款号查询
     */
    public Optional<PayRefundOrder> findByRefundNo(String refundNo) {
        return findByField(PayRefundOrder::getRefundNo, refundNo);
    }

    /**
     * 查询支付号是否重复
     */
    public boolean existsByRefundNo(String refundNo){
        return this.existedByField(PayRefundOrder::getRefundNo,refundNo);
    }
}
