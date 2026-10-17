package cn.daxpay.open.channel.helipay.strategy.pay;

import cn.daxpay.open.channel.helipay.client.credential.HelipaySdkCredential;
import cn.daxpay.open.channel.helipay.service.config.HelipayConfigAssembler;
import cn.daxpay.open.channel.helipay.service.payment.HelipayPayService;
import cn.daxpay.open.payment.strategy.pay.AbsNormalPayStrategy;
import cn.daxpay.open.payment.strategy.pay.PayStrategyContext;
import cn.daxpay.open.payment.trade.runtime.bo.PayTradeResultBo;
import cn.daxpay.open.payment.unipay.param.trade.pay.NormalPayParam;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 合利宝直连支付策略
///
/// 合利宝支付(ProductEnum.HELI_PAY)下发起支付的具体执行策略。
/// 配置组装在 [#doBeforePay] 阶段完成(委托 [HelipayConfigAssembler]), 支付执行委托给 [HelipayPayService]。
@Slf4j
@Service
@RequiredArgsConstructor
public class HelipayDirectPayStrategy extends AbsNormalPayStrategy {

    private final HelipayPayService helipayPayService;

    private final HelipayConfigAssembler helipayConfigAssembler;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.HELI_PAY;
    }

    /// 支付前预处理: 从支付参数读取通道路由参数, 组装通道凭证写入上下文
    /// 配置缺失在此阶段抛异常, fail-fast, 不会进入 doPay
    @Override
    public void doBeforePay(PayStrategyContext context) {
        NormalPayParam payParam = context.getPayParam();
        HelipaySdkCredential credential = helipayConfigAssembler.buildConfig(
                payParam.getMchNo(), payParam.getChannelMchNo(), payParam.getCapability());
        context.setChannelConfig(credential);
    }

    @Override
    public PayTradeResultBo doPay(PayStrategyContext context) {
        // 直接使用 doBeforePay 预组装的通道凭证
        HelipaySdkCredential credential = context.getChannelConfig(HelipaySdkCredential.class);
        return helipayPayService.pay(context.getTrade(), context.getPayParam(), credential);
    }
}
