package cn.bootx.platform.daxpay.service.core.order.refund.dao;

import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.daxpay.service.core.order.refund.entity.PayRefundOrderChannel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 *
 * @author xxm
 * @since 2024/1/17
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class PayRefundOrderChannelManager extends BaseManager<PayRefundOrderChannelMapper, PayRefundOrderChannel> {


    /**
     * 根据退款单ID查找
     */
    public List<PayRefundOrderChannel> findAllByRefundId(Long paymentId){
        return findAllByField(PayRefundOrderChannel::getRefundId,paymentId);
    }

    /**
     * 根据退款单ID和退款通道查询
     */
    public Optional<PayRefundOrderChannel> findByPaymentIdAndChannel(Long paymentId, String channel) {
        return lambdaQuery()
                .eq(PayRefundOrderChannel::getRefundId,paymentId)
                .eq(PayRefundOrderChannel::getChannel,channel)
                .oneOpt();
    }
}
