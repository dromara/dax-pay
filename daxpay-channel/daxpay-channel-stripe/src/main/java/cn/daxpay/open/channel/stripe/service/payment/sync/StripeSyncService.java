package cn.daxpay.open.channel.stripe.service.payment.sync;

import cn.daxpay.open.channel.stripe.client.StripeChannelClient;
import cn.daxpay.open.channel.stripe.client.credential.StripeSdkCredential;
import cn.daxpay.open.channel.stripe.client.req.StripeSyncReq;
import cn.daxpay.open.channel.stripe.client.resp.StripeSyncResp;
import cn.daxpay.open.channel.stripe.code.StripePayCode;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.trade.enums.PayFundStatusEnum;
import cn.daxpay.open.payment.trade.order.entity.PayTrade;
import cn.daxpay.open.payment.trade.runtime.bo.PaySyncResultBo;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.Objects;

/// # Stripe 支付同步业务服务
///
/// 通过 [StripeChannelClient] 调用子应用查询 PaymentIntent 状态,
/// 将 intent.status 映射为平台 [PayFundStatusEnum]。
///
/// 映射规则:
/// - succeeded → SUCCESS(支付成功)
/// - requires_payment_method / requires_confirmation / processing → PROCESSING(未支付/支付中)
/// - canceled → CLOSE(已取消)
/// - requires_action → PROCESSING(3DS 挑战中)
@Slf4j
@Service
@RequiredArgsConstructor
public class StripeSyncService {

    private final StripeChannelClient stripeChannelClient;

    /// 执行 Stripe 支付同步
    public PaySyncResultBo sync(PayTrade trade, StripeSdkCredential credential) {
        StripeSyncReq req = new StripeSyncReq();
        req.setPaymentIntentId(trade.getOutOrderNo());
        req.setCredential(credential);

        DaxResult<StripeSyncResp> result = stripeChannelClient.sync(req);
        if (result.getCode() != 0) {
            // Stripe 通道同步失败
            log.error("Stripe 通道同步失败: tradeNo={}, msg={}", trade.getTradeNo(), result.getMsg());
            return new PaySyncResultBo()
                    .setSyncSuccess(false)
                    .setSyncErrorMsg(result.getMsg());
        }

        return toSyncResult(result.getData());
    }

    /// 解析子应用响应, 映射 intent.status → [PayFundStatusEnum]
    private PaySyncResultBo toSyncResult(StripeSyncResp resp) {
        PaySyncResultBo bo = new PaySyncResultBo();
        bo.setOutOrderNo(resp.getOutOrderNo());
        // 支付成功时间(ISO8601 → OffsetDateTime)
        if (StrUtil.isNotBlank(resp.getPayTime())) {
            bo.setFinishTime(OffsetDateTime.parse(resp.getPayTime()));
        }
        bo.setRealAmount(resp.getAmount());

        String tradeStatus = resp.getTradeStatus();
        if (StrUtil.isBlank(tradeStatus)) {
            return bo.setSyncSuccess(false)
                    .setSyncErrorMsg("Stripe 同步查询无状态返回");
        }
        // 支付成功 → SUCCESS
        if (Objects.equals(StripePayCode.INTENT_STATUS_SUCCEEDED, tradeStatus)) {
            return bo.setPayStatus(PayFundStatusEnum.SUCCESS);
        }
        // 已取消 → CLOSE
        if (Objects.equals(StripePayCode.INTENT_STATUS_CANCELED, tradeStatus)) {
            return bo.setPayStatus(PayFundStatusEnum.CLOSE);
        }
        // 未支付 / 3DS 挑战中 / 处理中 → PROCESSING
        return bo.setPayStatus(PayFundStatusEnum.PROCESSING);
    }
}
