package cn.daxpay.open.channel.ums.strategy.refund;

import cn.daxpay.open.channel.ums.client.credential.UmsSdkCredential;
import cn.daxpay.open.channel.ums.client.enums.UmsPayMethod;
import cn.daxpay.open.channel.ums.service.direct.UmsDirectConfigAssembler;
import cn.daxpay.open.channel.ums.service.payment.refund.UmsRefundService;
import cn.daxpay.open.channel.ums.strategy.UmsStrategySupport;
import cn.daxpay.open.payment.strategy.refund.AbsRefundStrategy;
import cn.daxpay.open.payment.trade.order.entity.RefundOrder;
import cn.daxpay.open.payment.trade.runtime.bo.RefundResultBo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/// # 银联商务退款策略基类
///
/// 退款支付方式由所属产品决定(经 [UmsStrategySupport#resolveCloseSyncMethod]):
/// 扫码类走 bills 退款, H5 类走 netpay 退款。子类只需实现 getProduct。
@Slf4j
@RequiredArgsConstructor
public abstract class AbsUmsRefundStrategy extends AbsRefundStrategy {

    protected final UmsRefundService umsRefundService;
    protected final UmsDirectConfigAssembler umsDirectConfigAssembler;

    @Override
    public RefundResultBo doRefund(RefundOrder refundOrder) {
        UmsSdkCredential credential = umsDirectConfigAssembler.buildConfig(
                refundOrder.getMchNo(), refundOrder.getChannelMchNo(), refundOrder.getCapability());
        // 支付方式按所属产品决定(扫码类走 bills 退款, H5 类走 netpay 退款)
        UmsPayMethod method = UmsStrategySupport.resolveCloseSyncMethod(getProduct());
        return umsRefundService.refund(refundOrder, credential, method);
    }
}
