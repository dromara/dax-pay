package cn.daxpay.open.channel.llianpay.strategy.pay;

import cn.daxpay.open.channel.llianpay.client.credential.LlianpaySdkCredential;
import cn.daxpay.open.channel.llianpay.service.direct.LlianpayDirectConfigAssembler;
import cn.daxpay.open.channel.llianpay.service.payment.pay.LlianpayPayService;
import cn.daxpay.open.payment.strategy.pay.AbsNormalPayStrategy;
import cn.daxpay.open.payment.strategy.pay.PayStrategyContext;
import cn.daxpay.open.payment.trade.runtime.bo.PayTradeResultBo;
import cn.daxpay.open.payment.unipay.param.trade.pay.NormalPayParam;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 连连直连支付策略
///
/// 连连支付(ProductEnum.LLIAN_PAY)下发起支付的具体执行策略。
/// 配置组装在 [#doBeforePay] 阶段完成(委托 [LlianpayDirectConfigAssembler]),
/// 支付执行委托给 [LlianpayPayService]。
@Slf4j
@Service
@RequiredArgsConstructor
public class LlianpayDirectPayStrategy extends AbsNormalPayStrategy {

    private final LlianpayPayService llianpayPayService;
    private final LlianpayDirectConfigAssembler llianpayDirectConfigAssembler;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.LLIAN_PAY;
    }

    /// 支付前预处理: 组装直连通道凭证写入上下文
    @Override
    public void doBeforePay(PayStrategyContext context) {
        NormalPayParam payParam = context.getPayParam();
        LlianpaySdkCredential credential = llianpayDirectConfigAssembler.buildConfig(
                payParam.getMchNo(), payParam.getChannelMchNo(), payParam.getCapability());
        context.setChannelConfig(credential);
    }

    @Override
    public PayTradeResultBo doPay(PayStrategyContext context) {
        LlianpaySdkCredential credential = context.getChannelConfig(LlianpaySdkCredential.class);
        return llianpayPayService.pay(context.getTrade(), context.getPayParam(), credential);
    }
}
