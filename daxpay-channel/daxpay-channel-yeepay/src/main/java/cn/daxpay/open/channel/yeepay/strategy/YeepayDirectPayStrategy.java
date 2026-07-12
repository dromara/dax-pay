package cn.daxpay.open.channel.yeepay.strategy;

import cn.daxpay.open.channel.yeepay.client.credential.YeepaySdkCredential;
import cn.daxpay.open.channel.yeepay.service.direct.YeepayDirectConfigAssembler;
import cn.daxpay.open.channel.yeepay.service.payment.pay.YeepayPayService;
import cn.daxpay.open.payment.core.strategy.pay.AbsNormalPayStrategy;
import cn.daxpay.open.payment.core.strategy.pay.PayStrategyContext;
import cn.daxpay.open.payment.core.trade.runtime.bo.PayTradeResultBo;
import cn.daxpay.open.payment.unipay.param.trade.pay.NormalPayParam;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 易宝直连支付策略
///
/// 易宝支付(ProductEnum.YEE_PAY)下发起支付的具体执行策略。
/// 配置组装在 [#doBeforePay] 阶段完成(委托 [YeepayDirectConfigAssembler]),
/// 支付执行委托给 [YeepayPayService]。
@Slf4j
@Service
@RequiredArgsConstructor
public class YeepayDirectPayStrategy extends AbsNormalPayStrategy {

    private final YeepayPayService yeepayPayService;
    private final YeepayDirectConfigAssembler yeepayDirectConfigAssembler;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.YEE_PAY;
    }

    /// 支付前预处理: 组装直连通道凭证写入上下文
    @Override
    public void doBeforePay(PayStrategyContext context) {
        NormalPayParam payParam = context.getPayParam();
        YeepaySdkCredential credential = yeepayDirectConfigAssembler.buildConfig(
                payParam.getMchNo(), payParam.getChannelMchNo(), payParam.getCapability());
        context.setChannelConfig(credential);
    }

    @Override
    public PayTradeResultBo doPay(PayStrategyContext context) {
        YeepaySdkCredential credential = context.getChannelConfig(YeepaySdkCredential.class);
        return yeepayPayService.pay(context.getTrade(), context.getPayParam(), credential);
    }
}
