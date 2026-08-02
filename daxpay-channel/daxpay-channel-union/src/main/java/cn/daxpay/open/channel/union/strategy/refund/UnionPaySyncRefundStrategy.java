package cn.daxpay.open.channel.union.strategy.refund;

import cn.daxpay.open.channel.union.client.credential.UnionSdkCredential;
import cn.daxpay.open.channel.union.client.enums.UnionPayMethod;
import cn.daxpay.open.channel.union.service.direct.UnionDirectConfigAssembler;
import cn.daxpay.open.channel.union.service.payment.refund.UnionRefundSyncService;
import cn.daxpay.open.channel.union.strategy.UnionStrategySupport;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import cn.daxpay.open.payment.strategy.refund.AbsSyncRefundStrategy;
import cn.daxpay.open.payment.trade.order.entity.RefundOrder;
import cn.daxpay.open.payment.trade.runtime.bo.RefundResultBo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 云闪付退款同步策略
///
/// 退款查询支付方式由退款单 capability(与 method 同码) 经 [UnionStrategySupport#resolveMethod] 解析。
@Slf4j
@Service
@RequiredArgsConstructor
public class UnionPaySyncRefundStrategy extends AbsSyncRefundStrategy {

    private final UnionRefundSyncService unionRefundSyncService;
    private final UnionDirectConfigAssembler unionDirectConfigAssembler;

    @Override
    public RefundResultBo doSync(RefundOrder refundOrder) {
        UnionSdkCredential credential = unionDirectConfigAssembler.buildConfig(
                refundOrder.getMchNo(), refundOrder.getChannelMchNo(), refundOrder.getCapability());
        UnionPayMethod method = UnionStrategySupport.resolveMethod(refundOrder.getCapability());
        return unionRefundSyncService.sync(refundOrder, credential, method);
    }

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.UNION_PAY;
    }
}
