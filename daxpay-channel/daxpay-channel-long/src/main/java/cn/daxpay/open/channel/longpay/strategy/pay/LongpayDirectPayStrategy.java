package cn.daxpay.open.channel.longpay.strategy.pay;

import cn.daxpay.open.channel.longpay.client.credential.LongpaySdkCredential;
import cn.daxpay.open.channel.longpay.service.direct.LongpayDirectConfigAssembler;
import cn.daxpay.open.channel.longpay.service.payment.pay.LongpayPayService;
import cn.daxpay.open.payment.strategy.pay.AbsNormalPayStrategy;
import cn.daxpay.open.payment.strategy.pay.PayStrategyContext;
import cn.daxpay.open.payment.trade.runtime.bo.PayTradeResultBo;
import cn.daxpay.open.payment.unipay.param.trade.pay.NormalPayParam;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 建行龙支付直连支付策略
///
/// 建行龙支付(ProductEnum.LONG_PAY)下发起支付的具体执行策略。
/// 配置组装在 [#doBeforePay] 阶段完成(委托 [LongpayDirectConfigAssembler]),
/// 支付执行委托给 [LongpayPayService]。
@Slf4j
@Service
@RequiredArgsConstructor
public class LongpayDirectPayStrategy extends AbsNormalPayStrategy {

    private final LongpayPayService longpayPayService;
    private final LongpayDirectConfigAssembler longpayDirectConfigAssembler;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.LONG_PAY;
    }

    /// 支付前预处理: 组装直连通道凭证写入上下文
    @Override
    public void doBeforePay(PayStrategyContext context) {
        NormalPayParam payParam = context.getPayParam();
        LongpaySdkCredential credential = longpayDirectConfigAssembler.buildConfig(
                payParam.getMchNo(), payParam.getChannelMchNo(), payParam.getCapability());
        context.setChannelConfig(credential);
    }

    @Override
    public PayTradeResultBo doPay(PayStrategyContext context) {
        LongpaySdkCredential credential = context.getChannelConfig(LongpaySdkCredential.class);
        return longpayPayService.pay(context.getTrade(), context.getPayParam(), credential);
    }
}
