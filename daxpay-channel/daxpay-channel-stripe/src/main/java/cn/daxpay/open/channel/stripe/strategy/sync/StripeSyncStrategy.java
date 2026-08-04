package cn.daxpay.open.channel.stripe.strategy.sync;

import cn.daxpay.open.channel.stripe.client.credential.StripeSdkCredential;
import cn.daxpay.open.channel.stripe.service.StripeConfigAssembler;
import cn.daxpay.open.channel.stripe.service.payment.sync.StripeSyncService;
import cn.daxpay.open.payment.strategy.pay.PayStrategyContext;
import cn.daxpay.open.payment.strategy.sync.AbsSyncPayOrderStrategy;
import cn.daxpay.open.payment.trade.runtime.bo.PaySyncResultBo;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # Stripe 支付同步策略
///
/// Stripe(ProductEnum.STRIPE_PAY)下的支付同步策略, 查询 PaymentIntent 状态。
@Slf4j
@Service
@RequiredArgsConstructor
public class StripeSyncStrategy extends AbsSyncPayOrderStrategy {

    private final StripeSyncService stripeSyncService;
    private final StripeConfigAssembler stripeConfigAssembler;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.STRIPE_PAY;
    }

    @Override
    public PaySyncResultBo doSync(PayStrategyContext context) {
        StripeSdkCredential credential = stripeConfigAssembler.buildConfig(
                context.getChannelMchNo());
        return stripeSyncService.sync(context.getTrade(), credential);
    }
}
