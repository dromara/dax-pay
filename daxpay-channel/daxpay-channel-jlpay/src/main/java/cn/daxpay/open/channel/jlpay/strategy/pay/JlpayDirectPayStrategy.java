package cn.daxpay.open.channel.jlpay.strategy.pay;

import cn.daxpay.open.channel.jlpay.client.credential.JlpaySdkCredential;
import cn.daxpay.open.channel.jlpay.service.direct.JlpayDirectConfigAssembler;
import cn.daxpay.open.channel.jlpay.service.payment.pay.JlpayPayService;
import cn.daxpay.open.payment.strategy.pay.AbsNormalPayStrategy;
import cn.daxpay.open.payment.strategy.pay.PayStrategyContext;
import cn.daxpay.open.payment.trade.runtime.bo.PayTradeResultBo;
import cn.daxpay.open.payment.unipay.param.trade.pay.NormalPayParam;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 嘉联直连支付策略
///
/// 嘉联支付(ProductEnum.JL_PAY)下发起支付的具体执行策略。
/// 配置组装在 [#doBeforePay] 阶段完成(委托 [JlpayDirectConfigAssembler]),
/// 支付执行委托给 [JlpayPayService]。
@Slf4j
@Service
@RequiredArgsConstructor
public class JlpayDirectPayStrategy extends AbsNormalPayStrategy {

    private final JlpayPayService jlpayPayService;
    private final JlpayDirectConfigAssembler jlpayDirectConfigAssembler;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.JL_PAY;
    }

    /// 支付前预处理: 组装直连通道凭证写入上下文
    @Override
    public void doBeforePay(PayStrategyContext context) {
        NormalPayParam payParam = context.getPayParam();
        JlpaySdkCredential credential = jlpayDirectConfigAssembler.buildConfig(
                payParam.getMchNo(), payParam.getChannelMchNo(), payParam.getCapability());
        context.setChannelConfig(credential);
    }

    @Override
    public PayTradeResultBo doPay(PayStrategyContext context) {
        JlpaySdkCredential credential = context.getChannelConfig(JlpaySdkCredential.class);
        return jlpayPayService.pay(context.getTrade(), context.getPayParam(), credential);
    }
}
