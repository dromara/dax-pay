package cn.daxpay.open.channel.leshua.strategy;

import cn.daxpay.open.channel.leshua.client.credential.LeshuaSdkCredential;
import cn.daxpay.open.channel.leshua.service.isv.LeshuaIsvConfigAssembler;
import cn.daxpay.open.channel.leshua.service.payment.LeshuaPayService;
import cn.daxpay.open.payment.core.strategy.pay.AbsNormalPayStrategy;
import cn.daxpay.open.payment.core.strategy.pay.PayStrategyContext;
import cn.daxpay.open.payment.core.trade.runtime.bo.PayTradeResultBo;
import cn.daxpay.open.payment.unipay.param.trade.pay.NormalPayParam;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 乐刷服务商支付策略
///
/// 乐刷支付(ProductEnum.LESHUA_PAY)下发起支付的具体执行策略。
/// 配置组装在 [#doBeforePay] 阶段完成(委托 [LeshuaIsvConfigAssembler]), 支付执行委托给 [LeshuaPayService]。
@Slf4j
@Service
@RequiredArgsConstructor
public class LeshuaPayStrategy extends AbsNormalPayStrategy {

    private final LeshuaPayService leshuaPayService;
    private final LeshuaIsvConfigAssembler leshuaIsvConfigAssembler;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.LESHUA_PAY;
    }

    /// 支付前预处理: 组装通道凭证写入上下文
    @Override
    public void doBeforePay(PayStrategyContext context) {
        NormalPayParam payParam = context.getPayParam();
        LeshuaSdkCredential credential = leshuaIsvConfigAssembler.buildConfig(
                payParam.getMchNo(), payParam.getChannelMchNo(), payParam.getCapability());
        context.setChannelConfig(credential);
    }

    @Override
    public PayTradeResultBo doPay(PayStrategyContext context) {
        LeshuaSdkCredential credential = context.getChannelConfig(LeshuaSdkCredential.class);
        return leshuaPayService.pay(context.getTrade(), context.getPayParam(), credential);
    }
}
