package cn.daxpay.open.channel.stripe.strategy.refund;

import cn.daxpay.open.channel.stripe.client.credential.StripeSdkCredential;
import cn.daxpay.open.channel.stripe.service.StripeConfigAssembler;
import cn.daxpay.open.channel.stripe.service.payment.refund.StripeRefundService;
import cn.daxpay.open.payment.strategy.refund.AbsRefundStrategy;
import cn.daxpay.open.payment.trade.order.entity.RefundOrder;
import cn.daxpay.open.payment.trade.runtime.bo.RefundResultBo;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # Stripe 退款策略
///
/// Stripe(ProductEnum.STRIPE_PAY)下的退款策略。
/// 组装通道凭证(委托 [StripeConfigAssembler]), 退款执行委托给 [StripeRefundService]。
@Slf4j
@Service
@RequiredArgsConstructor
public class StripeRefundStrategy extends AbsRefundStrategy {

    private final StripeRefundService stripeRefundService;
    private final StripeConfigAssembler stripeConfigAssembler;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.STRIPE_PAY;
    }

    @Override
    public RefundResultBo doRefund(RefundOrder refundOrder) {
        StripeSdkCredential credential = stripeConfigAssembler.buildConfig(
                refundOrder.getChannelMchNo());
        return stripeRefundService.refund(refundOrder, credential);
    }
}
