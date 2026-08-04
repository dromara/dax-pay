package cn.daxpay.open.channel.stripe.strategy.pay;

import cn.daxpay.open.channel.stripe.client.credential.StripeSdkCredential;
import cn.daxpay.open.channel.stripe.service.StripeConfigAssembler;
import cn.daxpay.open.channel.stripe.service.payment.close.StripeCloseService;
import cn.daxpay.open.payment.strategy.pay.AbsPayCloseStrategy;
import cn.daxpay.open.payment.strategy.pay.PayStrategyContext;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import cn.daxpay.open.platform.core.enums.pay.pay.CloseTypeEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # Stripe 支付关闭策略
///
/// Stripe(ProductEnum.STRIPE_PAY)下的关单策略, 取消 PaymentIntent 释放卡组授权额度。
@Slf4j
@Service
@RequiredArgsConstructor
public class StripeCloseStrategy extends AbsPayCloseStrategy {

    private final StripeCloseService stripeCloseService;
    private final StripeConfigAssembler stripeConfigAssembler;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.STRIPE_PAY;
    }

    @Override
    public CloseTypeEnum doClose(PayStrategyContext context, boolean useCancel) {
        StripeSdkCredential credential = stripeConfigAssembler.buildConfig(
                context.getChannelMchNo());
        return stripeCloseService.close(context.getTrade(), credential, useCancel);
    }
}
