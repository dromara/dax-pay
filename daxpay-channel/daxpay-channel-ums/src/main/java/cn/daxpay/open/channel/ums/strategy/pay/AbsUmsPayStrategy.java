package cn.daxpay.open.channel.ums.strategy.pay;

import cn.daxpay.open.channel.ums.client.credential.UmsSdkCredential;
import cn.daxpay.open.channel.ums.service.direct.UmsDirectConfigAssembler;
import cn.daxpay.open.channel.ums.service.payment.pay.UmsPayService;
import cn.daxpay.open.payment.strategy.pay.AbsNormalPayStrategy;
import cn.daxpay.open.payment.strategy.pay.PayStrategyContext;
import cn.daxpay.open.payment.trade.runtime.bo.PayTradeResultBo;
import cn.daxpay.open.payment.unipay.param.trade.pay.NormalPayParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/// # 银联商务支付策略基类
///
/// 银联商务各产品(UMS_QRCODE/H5/JSAPI/MINI/APP/BARCODE)支付执行逻辑相同:
/// 统一委托 [UmsPayService#pay], 内部按请求 method([NormalPayParam#getMethod])
/// 经 [UmsPayService#mapMethod] 映射为子应用 [cn.daxpay.open.channel.ums.client.enums.UmsPayMethod]。
///
/// 子类只需实现 [cn.daxpay.open.payment.strategy.PaymentStrategy#getProduct] 返回对应产品,
/// 注册为独立 Spring Bean 即可被 [cn.daxpay.open.payment.strategy.PaymentStrategyFactory] 按 productCode 路由。
@Slf4j
@RequiredArgsConstructor
public abstract class AbsUmsPayStrategy extends AbsNormalPayStrategy {

    protected final UmsPayService umsPayService;
    protected final UmsDirectConfigAssembler umsDirectConfigAssembler;

    /// 支付前预处理: 组装通道凭证写入上下文
    @Override
    public void doBeforePay(PayStrategyContext context) {
        NormalPayParam payParam = context.getPayParam();
        UmsSdkCredential credential = umsDirectConfigAssembler.buildConfig(
                payParam.getMchNo(), payParam.getChannelMchNo(), payParam.getCapability());
        context.setChannelConfig(credential);
    }

    @Override
    public PayTradeResultBo doPay(PayStrategyContext context) {
        UmsSdkCredential credential = context.getChannelConfig(UmsSdkCredential.class);
        return umsPayService.pay(context.getTrade(), context.getPayParam(), credential);
    }
}
