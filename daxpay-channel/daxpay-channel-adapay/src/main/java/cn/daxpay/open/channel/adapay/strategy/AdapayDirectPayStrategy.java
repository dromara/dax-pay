package cn.daxpay.open.channel.adapay.strategy;

import cn.daxpay.open.channel.adapay.client.credential.AdapaySdkCredential;
import cn.daxpay.open.channel.adapay.service.direct.AdapayDirectConfigAssembler;
import cn.daxpay.open.channel.adapay.service.payment.pay.AdapayPayService;
import cn.daxpay.open.payment.core.strategy.pay.AbsNormalPayStrategy;
import cn.daxpay.open.payment.core.strategy.pay.PayStrategyContext;
import cn.daxpay.open.payment.core.trade.bo.PayTradeResultBo;
import cn.daxpay.open.payment.unipay.param.trade.pay.NormalPayParam;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 汇付天下直连支付策略
///
/// 汇付天下支付(ProductEnum.ADA_PAY)下发起支付的具体执行策略。
/// 配置组装在 [doBeforePay] 阶段完成(委托 [AdapayDirectConfigAssembler]),
/// 支付执行委托给 [AdapayPayService]。
@Slf4j
@Service
@RequiredArgsConstructor
public class AdapayDirectPayStrategy extends AbsNormalPayStrategy {

    private final AdapayPayService adapayPayService;
    private final AdapayDirectConfigAssembler adapayDirectConfigAssembler;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.ADA_PAY;
    }

    /// 支付前预处理: 组装直连通道凭证写入上下文
    @Override
    public void doBeforePay(PayStrategyContext context) {
        NormalPayParam payParam = context.getPayParam();
        AdapaySdkCredential credential = adapayDirectConfigAssembler.buildConfig(
                payParam.getMchNo(), payParam.getChannelMchNo(), payParam.getCapability());
        context.setChannelConfig(credential);
    }

    @Override
    public PayTradeResultBo doPay(PayStrategyContext context) {
        AdapaySdkCredential credential = context.getChannelConfig(AdapaySdkCredential.class);
        return adapayPayService.pay(context.getTrade(), context.getPayParam(), credential);
    }
}
