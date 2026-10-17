package cn.daxpay.open.channel.yisheng.strategy.pay;

import cn.daxpay.open.channel.yisheng.client.credential.YishengSdkCredential;
import cn.daxpay.open.channel.yisheng.service.direct.YishengDirectConfigAssembler;
import cn.daxpay.open.channel.yisheng.service.payment.pay.YishengPayService;
import cn.daxpay.open.payment.strategy.pay.AbsNormalPayStrategy;
import cn.daxpay.open.payment.strategy.pay.PayStrategyContext;
import cn.daxpay.open.payment.trade.runtime.bo.PayTradeResultBo;
import cn.daxpay.open.payment.unipay.param.trade.pay.NormalPayParam;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 易生直连支付策略
///
/// 易生支付(ProductEnum.YISHENG_PAY)下发起支付的具体执行策略。
/// 配置组装在 [#doBeforePay] 阶段完成(委托 [YishengDirectConfigAssembler]),
/// 支付执行委托给 [YishengPayService]。
@Slf4j
@Service
@RequiredArgsConstructor
public class YishengDirectPayStrategy extends AbsNormalPayStrategy {

    private final YishengPayService yishengPayService;
    private final YishengDirectConfigAssembler yishengDirectConfigAssembler;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.YISHENG_PAY;
    }

    /// 支付前预处理: 组装直连通道凭证写入上下文
    @Override
    public void doBeforePay(PayStrategyContext context) {
        NormalPayParam payParam = context.getPayParam();
        YishengSdkCredential credential = yishengDirectConfigAssembler.buildConfig(
                payParam.getMchNo(), payParam.getChannelMchNo(), payParam.getCapability());
        context.setChannelConfig(credential);
    }

    @Override
    public PayTradeResultBo doPay(PayStrategyContext context) {
        YishengSdkCredential credential = context.getChannelConfig(YishengSdkCredential.class);
        return yishengPayService.pay(context.getTrade(), context.getPayParam(), credential);
    }
}
