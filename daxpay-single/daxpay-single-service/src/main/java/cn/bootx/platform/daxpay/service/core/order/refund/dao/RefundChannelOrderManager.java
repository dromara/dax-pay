package cn.bootx.platform.daxpay.service.core.order.refund.dao;

import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.daxpay.service.core.order.refund.entity.RefundChannelOrder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 通道退款订单
 * @author xxm
 * @since 2024/1/17
 */
@Slf4j
@Repository
@RequiredArgsConstructor
@Deprecated
public class RefundChannelOrderManager extends BaseManager<RefundChannelOrderMapper, RefundChannelOrder> {

    /**
     * 根据退款单ID查找
     */
    public List<RefundChannelOrder> findAllByRefundId(Long paymentId){
        return findAllByField(RefundChannelOrder::getRefundId,paymentId);
    }

    /**
     * 根据退款单ID和退款通道查询
     */
    public Optional<RefundChannelOrder> findByRefundIdAndChannel(Long refundId, String channel) {
        return lambdaQuery()
                .eq(RefundChannelOrder::getRefundId,refundId)
                .eq(RefundChannelOrder::getChannel,channel)
                .oneOpt();
    }
}
