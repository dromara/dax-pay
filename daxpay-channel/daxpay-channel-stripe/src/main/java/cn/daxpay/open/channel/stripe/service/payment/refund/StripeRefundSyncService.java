package cn.daxpay.open.channel.stripe.service.payment.refund;

import cn.daxpay.open.channel.stripe.client.StripeChannelClient;
import cn.daxpay.open.channel.stripe.client.credential.StripeSdkCredential;
import cn.daxpay.open.channel.stripe.client.req.StripeRefundSyncReq;
import cn.daxpay.open.channel.stripe.client.resp.StripeRefundSyncResp;
import cn.daxpay.open.channel.stripe.code.StripePayCode;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.trade.enums.RefundOrderStatusEnum;
import cn.daxpay.open.payment.trade.order.entity.RefundOrder;
import cn.daxpay.open.payment.trade.runtime.bo.RefundResultBo;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.Objects;

/// # Stripe 退款同步业务服务
///
/// 通过 [StripeChannelClient] 调用子应用查询 Stripe Refund 状态,
/// 将 refund.status 映射为平台 [RefundOrderStatusEnum]。
@Slf4j
@Service
@RequiredArgsConstructor
public class StripeRefundSyncService {

    private final StripeChannelClient stripeChannelClient;

    /// 执行 Stripe 退款同步
    public RefundResultBo sync(RefundOrder refundOrder, StripeSdkCredential credential) {
        StripeRefundSyncReq req = new StripeRefundSyncReq();
        req.setRefundId(refundOrder.getOutRefundNo());
        req.setCredential(credential);

        DaxResult<StripeRefundSyncResp> result = stripeChannelClient.refundSync(req);
        if (result.getCode() != 0) {
            // Stripe 通道退款同步失败
            log.error("Stripe 通道退款同步失败: refundNo={}, msg={}", refundOrder.getRefundNo(), result.getMsg());
            return new RefundResultBo()
                    .setSyncSuccess(false)
                    .setSyncErrorMsg(result.getMsg());
        }

        return toSyncResult(result.getData());
    }

    /// 解析子应用响应, 映射 refund.status → [RefundOrderStatusEnum]
    private RefundResultBo toSyncResult(StripeRefundSyncResp resp) {
        RefundResultBo bo = new RefundResultBo();
        bo.setOutRefundNo(resp.getOutRefundNo());
        // 退款完成时间(ISO8601 → OffsetDateTime)
        if (StrUtil.isNotBlank(resp.getFinishTime())) {
            bo.setFinishTime(OffsetDateTime.parse(resp.getFinishTime()));
        }
        bo.setRefundAmount(resp.getAmount());
        // succeeded → 退款成功; failed → 退款失败; 其余 → 退款中
        if (Objects.equals(StripePayCode.REFUND_STATUS_SUCCEEDED, resp.getTradeStatus())) {
            bo.setComplete(true)
                    .setStatus(RefundOrderStatusEnum.SUCCESS);
        } else if (Objects.equals(StripePayCode.REFUND_STATUS_FAILED, resp.getTradeStatus())) {
            bo.setComplete(true)
                    .setStatus(RefundOrderStatusEnum.FAIL);
        } else {
            bo.setComplete(false)
                    .setStatus(RefundOrderStatusEnum.PROGRESS);
        }
        return bo;
    }
}
