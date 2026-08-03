package cn.daxpay.open.channel.union.strategy.refund;

import cn.daxpay.open.channel.union.client.credential.UnionSdkCredential;
import cn.daxpay.open.channel.union.client.enums.UnionPayMethod;
import cn.daxpay.open.channel.union.service.UnionConfigAssembler;
import cn.daxpay.open.channel.union.service.payment.refund.UnionRefundService;
import cn.daxpay.open.channel.union.strategy.UnionStrategySupport;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import cn.daxpay.open.payment.strategy.refund.AbsRefundStrategy;
import cn.daxpay.open.payment.trade.order.entity.RefundOrder;
import cn.daxpay.open.payment.trade.runtime.bo.RefundResultBo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 云闪付退款策略
///
/// 退款支付方式由退款单 capability(与 method 同码) 经 [UnionStrategySupport#resolveMethod] 解析。
@Slf4j
@Service
@RequiredArgsConstructor
public class UnionPayRefundStrategy extends AbsRefundStrategy {

    private final UnionRefundService unionRefundService;
    private final UnionConfigAssembler unionConfigAssembler;

    @Override
    public RefundResultBo doRefund(RefundOrder refundOrder) {
        UnionSdkCredential credential = unionConfigAssembler.buildConfig(
                refundOrder.getMchNo(), refundOrder.getChannelMchNo(), refundOrder.getCapability());
        UnionPayMethod method = UnionStrategySupport.resolveMethod(refundOrder.getCapability());
        return unionRefundService.refund(refundOrder, credential, method);
    }

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.UNION_PAY;
    }
}
