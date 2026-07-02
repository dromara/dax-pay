package cn.daxpay.open.channel.alipay.strategy.isv;

import cn.daxpay.open.channel.alipay.client.credential.AlipaySdkCredential;
import cn.daxpay.open.channel.alipay.service.isv.AlipayIsvConfigAssembler;
import cn.daxpay.open.channel.alipay.service.payment.pay.AlipayPayService;
import cn.daxpay.open.payment.strategy.pay.PayStrategyContext;
import cn.daxpay.open.payment.pay.bo.PayTradeResultBo;
import cn.daxpay.open.payment.strategy.pay.AbsNormalPayStrategy;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 支付宝服务商支付策略
///
/// 支付宝服务商模式(ProductEnum.ALIPAY_ISV)下发起支付的具体执行策略。
/// 配置组装(含应用授权令牌)在 [doBeforePay] 阶段完成(委托 [AlipayIsvConfigAssembler]), 支付执行委托给 [AlipayPayService]。
@Slf4j
@Service
@RequiredArgsConstructor
public class AlipayIsvPayStrategy extends AbsNormalPayStrategy {

    private final AlipayPayService alipayPayService;
    private final AlipayIsvConfigAssembler alipayIsvConfigAssembler;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.ALIPAY_ISV;
    }

    /// 支付前预处理: 组装服务商模式通道凭证(含应用授权令牌)写入上下文
    /// 配置缺失在此阶段抛异常, fail-fast, 不会进入 doPay
    @Override
    public void doBeforePay(PayStrategyContext context) {
        AlipaySdkCredential credential = alipayIsvConfigAssembler.buildConfig(context.getPayParam().getMchNo());
        context.setChannelConfig(credential);
    }

    @Override
    public PayTradeResultBo doPay(PayStrategyContext context) {
        // 直接使用 doBeforePay 预组装的通道凭证
        AlipaySdkCredential credential = (AlipaySdkCredential) context.getChannelConfig();
        return alipayPayService.pay(context.getTrade(), context.getPayParam(), credential);
    }
}
