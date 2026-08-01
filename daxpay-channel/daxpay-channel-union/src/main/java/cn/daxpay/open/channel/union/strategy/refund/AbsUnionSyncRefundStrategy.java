package cn.daxpay.open.channel.union.strategy.refund;

import cn.daxpay.open.channel.union.client.credential.UnionSdkCredential;
import cn.daxpay.open.channel.union.client.enums.UnionPayMethod;
import cn.daxpay.open.channel.union.service.direct.UnionDirectConfigAssembler;
import cn.daxpay.open.channel.union.service.payment.refund.UnionRefundSyncService;
import cn.daxpay.open.channel.union.strategy.UnionStrategySupport;
import cn.daxpay.open.payment.strategy.refund.AbsSyncRefundStrategy;
import cn.daxpay.open.payment.trade.order.entity.RefundOrder;
import cn.daxpay.open.payment.trade.runtime.bo.RefundResultBo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/// # 云闪付退款同步策略基类
///
/// 退款查询支付方式由所属产品决定。子类只需实现 getProduct。
@Slf4j
@RequiredArgsConstructor
public abstract class AbsUnionSyncRefundStrategy extends AbsSyncRefundStrategy {

    protected final UnionRefundSyncService unionRefundSyncService;
    protected final UnionDirectConfigAssembler unionDirectConfigAssembler;

    @Override
    public RefundResultBo doSync(RefundOrder refundOrder) {
        UnionSdkCredential credential = unionDirectConfigAssembler.buildConfig(
                refundOrder.getMchNo(), refundOrder.getChannelMchNo(), refundOrder.getCapability());
        UnionPayMethod method = UnionStrategySupport.resolveMethod(getProduct());
        return unionRefundSyncService.sync(refundOrder, credential, method);
    }
}
