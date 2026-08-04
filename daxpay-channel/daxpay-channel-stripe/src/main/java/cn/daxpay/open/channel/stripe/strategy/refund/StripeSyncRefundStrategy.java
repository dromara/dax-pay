package cn.daxpay.open.channel.stripe.strategy.refund;

import cn.daxpay.open.channel.stripe.client.credential.StripeSdkCredential;
import cn.daxpay.open.channel.stripe.service.StripeConfigAssembler;
import cn.daxpay.open.channel.stripe.service.payment.refund.StripeRefundSyncService;
import cn.daxpay.open.payment.strategy.refund.AbsSyncRefundStrategy;
import cn.daxpay.open.payment.trade.order.entity.RefundOrder;
import cn.daxpay.open.payment.trade.runtime.bo.RefundResultBo;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # Stripe 退款同步策略
///
/// Stripe(ProductEnum.STRIPE_PAY)下的退款同步策略, 查询 Refund 最终状态。
@Slf4j
@Service
@RequiredArgsConstructor
public class StripeSyncRefundStrategy extends AbsSyncRefundStrategy {

    private final StripeRefundSyncService stripeRefundSyncService;
    private final StripeConfigAssembler stripeConfigAssembler;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.STRIPE_PAY;
    }

    @Override
    public RefundResultBo doSync(RefundOrder refundOrder) {
        StripeSdkCredential credential = stripeConfigAssembler.buildConfig(
                refundOrder.getChannelMchNo());
        return stripeRefundSyncService.sync(refundOrder, credential);
    }
}
