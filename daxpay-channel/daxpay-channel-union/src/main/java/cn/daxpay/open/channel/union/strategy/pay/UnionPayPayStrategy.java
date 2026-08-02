package cn.daxpay.open.channel.union.strategy.pay;

import cn.daxpay.open.channel.union.client.credential.UnionSdkCredential;
import cn.daxpay.open.channel.union.service.direct.UnionDirectConfigAssembler;
import cn.daxpay.open.channel.union.service.payment.pay.UnionPayService;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import cn.daxpay.open.payment.strategy.pay.AbsNormalPayStrategy;
import cn.daxpay.open.payment.strategy.pay.PayStrategyContext;
import cn.daxpay.open.payment.trade.runtime.bo.PayTradeResultBo;
import cn.daxpay.open.payment.unipay.param.trade.pay.NormalPayParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 云闪付支付策略
///
/// 单一产品(UNION_PAY), 委托 [UnionPayService#pay] 执行, 内部按请求 method 映射分发
/// (主扫 UNION_QR / H5 / 被扫 UNION_BARCODE)。
@Slf4j
@Service
@RequiredArgsConstructor
public class UnionPayPayStrategy extends AbsNormalPayStrategy {

    private final UnionPayService unionPayService;
    private final UnionDirectConfigAssembler unionDirectConfigAssembler;

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

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.UNION_PAY;
    }
}
