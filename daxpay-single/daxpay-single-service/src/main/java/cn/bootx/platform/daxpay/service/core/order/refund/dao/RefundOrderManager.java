package cn.bootx.platform.daxpay.service.core.order.refund.dao;

import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.common.query.generator.QueryGenerator;
import cn.bootx.platform.daxpay.service.core.order.refund.entity.RefundOrder;
import cn.bootx.platform.daxpay.service.param.order.RefundOrderQuery;
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
public class RefundOrderManager extends BaseManager<RefundOrderMapper, RefundOrder> {

    /**
     * 分页
     */
    public Page<RefundOrder> page(PageParam pageParam, RefundOrderQuery query) {
        Page<RefundOrder> mpPage = MpUtil.getMpPage(pageParam, RefundOrder.class);
        QueryWrapper<RefundOrder> generator = QueryGenerator.generator(query);
        return page(mpPage,generator);
    }

    /**
     * 根据退款号查询
     */
    public Optional<RefundOrder> findByRefundNo(String refundNo) {
        return findByField(RefundOrder::getRefundNo, refundNo);
    }

    /**
     * 查询支付号是否重复
     */
    public boolean existsByRefundNo(String refundNo){
        return this.existedByField(RefundOrder::getRefundNo,refundNo);
    }
}
