package cn.daxpay.open.channel.sheng.strategy.pay;

import cn.daxpay.open.channel.sheng.client.credential.ShengSdkCredential;
import cn.daxpay.open.channel.sheng.service.config.ShengConfigAssembler;
import cn.daxpay.open.channel.sheng.service.payment.ShengPayService;
import cn.daxpay.open.payment.strategy.pay.AbsNormalPayStrategy;
import cn.daxpay.open.payment.strategy.pay.PayStrategyContext;
import cn.daxpay.open.payment.trade.runtime.bo.PayTradeResultBo;
import cn.daxpay.open.payment.unipay.param.trade.pay.NormalPayParam;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 盛付通支付策略
///
/// 盛付通支付(ProductEnum.SHENG_PAY)下发起支付的具体执行策略。
/// 配置组装在 [#doBeforePay] 阶段完成(委托 [ShengConfigAssembler]), 支付执行委托给 [ShengPayService]。
///
/// 被扫场景主应用只透传 payMethod + authCode, 由子应用分流 authPay 接口。
@Slf4j
@Service
@RequiredArgsConstructor
public class ShengPayStrategy extends AbsNormalPayStrategy {

    private final ShengPayService shengPayService;
    private final ShengConfigAssembler shengConfigAssembler;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.SHENG_PAY;
    }

    /// 支付前预处理: 从支付参数读取通道路由参数, 组装通道凭证写入上下文
    /// 配置缺失在此阶段抛异常, fail-fast, 不会进入 doPay
    @Override
    public void doBeforePay(PayStrategyContext context) {
        NormalPayParam payParam = context.getPayParam();
        ShengSdkCredential credential = shengConfigAssembler.buildConfig(
                payParam.getMchNo(), payParam.getChannelMchNo(), payParam.getCapability());
        context.setChannelConfig(credential);
    }

    @Override
    public PayTradeResultBo doPay(PayStrategyContext context) {
        // 直接使用 doBeforePay 预组装的通道凭证
        ShengSdkCredential credential = context.getChannelConfig(ShengSdkCredential.class);
        return shengPayService.pay(context.getTrade(), context.getPayParam(), credential);
    }
}
