package cn.daxpay.open.channel.stripe.service.payment.refund;

import cn.daxpay.open.channel.stripe.client.StripeChannelClient;
import cn.daxpay.open.channel.stripe.client.credential.StripeSdkCredential;
import cn.daxpay.open.channel.stripe.client.req.StripeRefundReq;
import cn.daxpay.open.channel.stripe.client.resp.StripeRefundResp;
import cn.daxpay.open.channel.stripe.code.StripePayCode;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.trade.enums.RefundOrderStatusEnum;
import cn.daxpay.open.payment.trade.order.entity.RefundOrder;
import cn.daxpay.open.payment.trade.runtime.bo.RefundResultBo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

/// # Stripe 退款业务服务
///
/// 通过 [StripeChannelClient] 调用子应用 dax-pay-channel-three 发起 Stripe 退款。
///
/// 资金变动判定:
/// - 退款状态 succeeded → 退款即时成功
/// - processing / failed → 退款中或失败, 需同步查询确认最终状态
@Slf4j
@Service
@RequiredArgsConstructor
public class StripeRefundService {

    private final StripeChannelClient stripeChannelClient;

    /// 执行 Stripe 退款
    public RefundResultBo refund(RefundOrder refundOrder, StripeSdkCredential credential) {
        StripeRefundReq req = new StripeRefundReq();
        req.setPaymentIntentId(refundOrder.getOutOrderNo());
        req.setAmount(refundOrder.getAmount());
        req.setReason(null);
        req.setCredential(credential);

        DaxResult<StripeRefundResp> result = stripeChannelClient.refund(req);
        if (result.getCode() != 0) {
            // Stripe 通道退款失败
            log.error("Stripe 通道退款失败: refundNo={}, msg={}", refundOrder.getRefundNo(), result.getMsg());
            return new RefundResultBo()
                    .setComplete(false)
                    .setStatus(RefundOrderStatusEnum.FAIL)
                    .setSyncSuccess(false)
                    .setSyncErrorMsg(result.getMsg());
        }

        return toRefundResult(result.getData());
    }

    /// 解析子应用响应
    private RefundResultBo toRefundResult(StripeRefundResp resp) {
        RefundResultBo bo = new RefundResultBo();
        bo.setOutRefundNo(resp.getOutRefundNo());
        // succeeded 退款即时成功; 其他状态需同步查询确认
        if (Objects.equals(StripePayCode.REFUND_STATUS_SUCCEEDED, resp.getTradeStatus())) {
            bo.setComplete(true)
                    .setStatus(RefundOrderStatusEnum.SUCCESS);
        } else {
            bo.setComplete(false)
                    .setStatus(RefundOrderStatusEnum.PROGRESS);
        }
        return bo;
    }
}
