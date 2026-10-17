package cn.daxpay.open.channel.jeepay.strategy.pay;

import cn.daxpay.open.channel.jeepay.client.credential.JeepaySdkCredential;
import cn.daxpay.open.channel.jeepay.service.config.JeepayConfigAssembler;
import cn.daxpay.open.channel.jeepay.service.payment.JeepayPayService;
import cn.daxpay.open.payment.strategy.pay.AbsNormalPayStrategy;
import cn.daxpay.open.payment.strategy.pay.PayStrategyContext;
import cn.daxpay.open.payment.trade.runtime.bo.PayTradeResultBo;
import cn.daxpay.open.payment.unipay.param.trade.pay.NormalPayParam;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # Jeepay支付策略
///
/// Jeepay 支付(ProductEnum.JEE_PAY)下发起支付的具体执行策略。
/// 配置组装在 [#doBeforePay] 阶段完成(委托 [JeepayConfigAssembler]), 支付执行委托给 [JeepayPayService]。
@Slf4j
@Service
@RequiredArgsConstructor
public class JeepayPayStrategy extends AbsNormalPayStrategy {

    private final JeepayPayService jeepayPayService;

    private final JeepayConfigAssembler jeepayConfigAssembler;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.JEE_PAY;
    }

    /// 支付前预处理: 从支付参数读取通道路由参数, 组装通道凭证写入上下文
    /// 配置缺失在此阶段抛异常, fail-fast, 不会进入 doPay
    @Override
    public void doBeforePay(PayStrategyContext context) {
        NormalPayParam payParam = context.getPayParam();
        JeepaySdkCredential credential = jeepayConfigAssembler.buildConfig(
                payParam.getMchNo(), payParam.getChannelMchNo(), payParam.getCapability());
        context.setChannelConfig(credential);
    }

    @Override
    public PayTradeResultBo doPay(PayStrategyContext context) {
        // 直接使用 doBeforePay 预组装的通道凭证
        JeepaySdkCredential credential = context.getChannelConfig(JeepaySdkCredential.class);
        return jeepayPayService.pay(context.getTrade(), context.getPayParam(), credential);
    }
}
