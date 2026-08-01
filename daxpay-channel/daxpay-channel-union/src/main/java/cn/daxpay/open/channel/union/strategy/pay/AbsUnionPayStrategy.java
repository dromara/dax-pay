package cn.daxpay.open.channel.union.strategy.pay;

import cn.daxpay.open.channel.union.client.credential.UnionSdkCredential;
import cn.daxpay.open.channel.union.service.direct.UnionDirectConfigAssembler;
import cn.daxpay.open.channel.union.service.payment.pay.UnionPayService;
import cn.daxpay.open.payment.strategy.pay.AbsNormalPayStrategy;
import cn.daxpay.open.payment.strategy.pay.PayStrategyContext;
import cn.daxpay.open.payment.trade.runtime.bo.PayTradeResultBo;
import cn.daxpay.open.payment.unipay.param.trade.pay.NormalPayParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/// # 云闪付支付策略基类
///
/// 云闪付三个产品(UNION_QRCODE/H5/BARCODE)支付执行逻辑相同:
/// 统一委托 [UnionPayService#pay], 内部按请求 method 映射分发。
///
/// 子类只需实现 [cn.daxpay.open.payment.strategy.PaymentStrategy#getProduct] 返回对应产品,
/// 注册为独立 Spring Bean 即可被 [cn.daxpay.open.payment.strategy.PaymentStrategyFactory] 按 productCode 路由。
@Slf4j
@RequiredArgsConstructor
public abstract class AbsUnionPayStrategy extends AbsNormalPayStrategy {

    protected final UnionPayService unionPayService;
    protected final UnionDirectConfigAssembler unionDirectConfigAssembler;

    /// 支付前预处理: 组装通道凭证写入上下文
    @Override
    public void doBeforePay(PayStrategyContext context) {
        NormalPayParam payParam = context.getPayParam();
        UnionSdkCredential credential = unionDirectConfigAssembler.buildConfig(
                payParam.getMchNo(), payParam.getChannelMchNo(), payParam.getCapability());
        context.setChannelConfig(credential);
    }

    @Override
    public PayTradeResultBo doPay(PayStrategyContext context) {
        UnionSdkCredential credential = context.getChannelConfig(UnionSdkCredential.class);
        return unionPayService.pay(context.getTrade(), context.getPayParam(), credential);
    }
}
