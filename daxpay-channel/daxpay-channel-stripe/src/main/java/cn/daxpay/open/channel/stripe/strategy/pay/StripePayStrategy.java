package cn.daxpay.open.channel.stripe.strategy.pay;

import cn.daxpay.open.channel.stripe.client.credential.StripeSdkCredential;
import cn.daxpay.open.channel.stripe.service.StripeConfigAssembler;
import cn.daxpay.open.channel.stripe.service.payment.pay.StripePayService;
import cn.daxpay.open.payment.strategy.pay.AbsNormalPayStrategy;
import cn.daxpay.open.payment.strategy.pay.PayStrategyContext;
import cn.daxpay.open.payment.trade.runtime.bo.PayTradeResultBo;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # Stripe 支付策略
///
/// Stripe(ProductEnum.STRIPE_PAY)下发起支付的具体执行策略。
/// 配置组装在 [#doBeforePay] 阶段完成(委托 [StripeConfigAssembler]), 支付执行委托给 [StripePayService]。
@Slf4j
@Service
@RequiredArgsConstructor
public class StripePayStrategy extends AbsNormalPayStrategy {

    private final StripePayService stripePayService;
    private final StripeConfigAssembler stripeConfigAssembler;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.STRIPE_PAY;
    }

    /// 支付前预处理: 从容器读取通道路由参数, 组装通道凭证写入上下文
    @Override
    public void doBeforePay(PayStrategyContext context) {
        StripeSdkCredential credential = stripeConfigAssembler.buildConfig(
                context.getChannelMchNo());
        context.setChannelConfig(credential);
    }

    @Override
    public PayTradeResultBo doPay(PayStrategyContext context) {
        // 直接使用 doBeforePay 预组装的通道凭证
        StripeSdkCredential credential = context.getChannelConfig(StripeSdkCredential.class);
        return stripePayService.pay(context.getTrade(), context.getPayParam(), credential);
    }
}
