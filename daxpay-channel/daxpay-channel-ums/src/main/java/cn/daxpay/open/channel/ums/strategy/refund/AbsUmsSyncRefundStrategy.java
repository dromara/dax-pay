package cn.daxpay.open.channel.ums.strategy.refund;

import cn.daxpay.open.channel.ums.client.credential.UmsSdkCredential;
import cn.daxpay.open.channel.ums.client.enums.UmsPayMethod;
import cn.daxpay.open.channel.ums.service.direct.UmsDirectConfigAssembler;
import cn.daxpay.open.channel.ums.service.payment.refund.UmsRefundSyncService;
import cn.daxpay.open.channel.ums.strategy.UmsStrategySupport;
import cn.daxpay.open.payment.strategy.refund.AbsSyncRefundStrategy;
import cn.daxpay.open.payment.trade.order.entity.RefundOrder;
import cn.daxpay.open.payment.trade.runtime.bo.RefundResultBo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/// # 银联商务退款同步策略基类
///
/// 退款查询支付方式由所属产品决定(经 [UmsStrategySupport#resolveCloseSyncMethod]):
/// 扫码类走 bills 退款查询, H5 类走 netpay 退款查询。子类只需实现 getProduct。
@Slf4j
@RequiredArgsConstructor
public abstract class AbsUmsSyncRefundStrategy extends AbsSyncRefundStrategy {

    protected final UmsRefundSyncService umsRefundSyncService;
    protected final UmsDirectConfigAssembler umsDirectConfigAssembler;

    @Override
    public RefundResultBo doSync(RefundOrder refundOrder) {
        UmsSdkCredential credential = umsDirectConfigAssembler.buildConfig(
                refundOrder.getMchNo(), refundOrder.getChannelMchNo(), refundOrder.getCapability());
        // 支付方式按所属产品决定(扫码类走 bills 退款查询, H5 类走 netpay 退款查询)
        UmsPayMethod method = UmsStrategySupport.resolveCloseSyncMethod(getProduct());
        return umsRefundSyncService.sync(refundOrder, credential, method);
    }
}
