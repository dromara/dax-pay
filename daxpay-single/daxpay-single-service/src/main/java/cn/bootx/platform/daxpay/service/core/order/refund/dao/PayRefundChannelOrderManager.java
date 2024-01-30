package cn.bootx.platform.daxpay.service.core.order.refund.dao;

import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.daxpay.service.core.order.refund.entity.PayRefundChannelOrder;
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
public class PayRefundChannelOrderManager extends BaseManager<PayRefundChannelOrderMapper, PayRefundChannelOrder> {

    /**
     * 根据退款单ID查找
     */
    public List<PayRefundChannelOrder> findAllByRefundId(Long paymentId){
        return findAllByField(PayRefundChannelOrder::getRefundId,paymentId);
    }

    /**
     * 根据退款单ID和退款通道查询
     */
    public Optional<PayRefundChannelOrder> findByRefundIdAndChannel(Long refundId, String channel) {
        return lambdaQuery()
                .eq(PayRefundChannelOrder::getRefundId,refundId)
                .eq(PayRefundChannelOrder::getChannel,channel)
                .oneOpt();
    }
}
