package cn.daxpay.open.channel.union.strategy.refund;

import cn.daxpay.open.channel.union.client.credential.UnionSdkCredential;
import cn.daxpay.open.channel.union.client.enums.UnionPayMethod;
import cn.daxpay.open.channel.union.service.direct.UnionDirectConfigAssembler;
import cn.daxpay.open.channel.union.service.payment.refund.UnionRefundService;
import cn.daxpay.open.channel.union.strategy.UnionStrategySupport;
import cn.daxpay.open.payment.strategy.refund.AbsRefundStrategy;
import cn.daxpay.open.payment.trade.order.entity.RefundOrder;
import cn.daxpay.open.payment.trade.runtime.bo.RefundResultBo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/// # 云闪付退款策略基类
///
/// 退款支付方式由所属产品决定(经 [UnionStrategySupport#resolveMethod])。子类只需实现 getProduct。
@Slf4j
@RequiredArgsConstructor
public abstract class AbsUnionRefundStrategy extends AbsRefundStrategy {

    protected final UnionRefundService unionRefundService;
    protected final UnionDirectConfigAssembler unionDirectConfigAssembler;

    @Override
    public RefundResultBo doRefund(RefundOrder refundOrder) {
        UnionSdkCredential credential = unionDirectConfigAssembler.buildConfig(
                refundOrder.getMchNo(), refundOrder.getChannelMchNo(), refundOrder.getCapability());
        UnionPayMethod method = UnionStrategySupport.resolveMethod(getProduct());
        return unionRefundService.refund(refundOrder, credential, method);
    }
}
