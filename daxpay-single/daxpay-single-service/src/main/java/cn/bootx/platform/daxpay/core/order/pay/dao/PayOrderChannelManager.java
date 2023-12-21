package cn.bootx.platform.daxpay.core.order.pay.dao;

import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.daxpay.core.order.pay.entity.PayOrderChannel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 支付订单关联支付时通道信息
 * @author xxm
 * @since 2023/12/20
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class PayOrderChannelManager extends BaseManager<PayOrderChannelMapper, PayOrderChannel> {
    /**
     * 根据订单id和支付渠道查询
     */
    public Optional<PayOrderChannel> findByOderIdAndChannel(Long paymentId, String channel) {
        return lambdaQuery()
                .eq(PayOrderChannel::getPaymentId,paymentId)
                .eq(PayOrderChannel::getChannel,channel)
                .oneOpt();
    }
}
