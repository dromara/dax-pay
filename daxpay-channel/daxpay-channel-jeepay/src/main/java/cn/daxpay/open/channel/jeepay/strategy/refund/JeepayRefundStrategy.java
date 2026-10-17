package cn.daxpay.open.channel.jeepay.strategy.refund;

import cn.daxpay.open.channel.jeepay.client.credential.JeepaySdkCredential;
import cn.daxpay.open.channel.jeepay.service.config.JeepayConfigAssembler;
import cn.daxpay.open.channel.jeepay.service.payment.JeepayRefundService;
import cn.daxpay.open.payment.strategy.refund.AbsRefundStrategy;
import cn.daxpay.open.payment.trade.runtime.bo.RefundResultBo;
import cn.daxpay.open.payment.trade.order.entity.RefundOrder;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # Jeepay退款策略
///
/// 从退款订单读取通道路由参数(channelMchNo / capability),
/// 组装通道凭证(委托 [JeepayConfigAssembler]), 退款执行委托给 [JeepayRefundService]。
@Slf4j
@Service
@RequiredArgsConstructor
public class JeepayRefundStrategy extends AbsRefundStrategy {

    private final JeepayRefundService jeepayRefundService;

    private final JeepayConfigAssembler jeepayConfigAssembler;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.JEE_PAY;
    }

    @Override
    public RefundResultBo doRefund(RefundOrder refundOrder) {
        // 组装通道调用凭证
        JeepaySdkCredential credential = jeepayConfigAssembler.buildConfig(
                refundOrder.getMchNo(), refundOrder.getChannelMchNo(), refundOrder.getCapability());
        return jeepayRefundService.refund(refundOrder, credential);
    }
}
