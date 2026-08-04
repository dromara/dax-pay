package cn.daxpay.open.channel.stripe.service.callback;

import cn.daxpay.open.channel.stripe.client.StripeChannelClient;
import cn.daxpay.open.channel.stripe.client.credential.StripeSdkCredential;
import cn.daxpay.open.channel.stripe.client.req.StripeCallbackParseReq;
import cn.daxpay.open.channel.stripe.client.resp.StripeCallbackParseResp;
import cn.daxpay.open.channel.stripe.code.StripePayCode;
import cn.daxpay.open.channel.stripe.service.StripeConfigAssembler;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.trade.record.service.PayCallbackRecordService;
import cn.daxpay.open.payment.trade.runtime.bo.RefundCallbackData;
import cn.daxpay.open.payment.trade.runtime.service.callback.RefundCallbackService;
import cn.daxpay.open.platform.core.enums.pay.notice.CallbackStatusEnum;
import cn.hutool.extra.servlet.JakartaServletUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/// # Stripe 退款回调处理服务
///
/// Stripe 退款异步通知(charge.refunded / refund.updated) → 主应用接收 → 按 channelMchNo 组装凭证 →
/// 转发到子应用验签解析 → 更新退款单状态。
@Slf4j
@Service
@RequiredArgsConstructor
public class StripeRefundCallbackService {

    private final StripeChannelClient stripeChannelClient;
    private final StripeConfigAssembler configAssembler;
    private final RefundCallbackService refundCallbackService;
    private final PayCallbackRecordService payCallbackRecordService;

    /// 退款回调处理
    public String refundHandle(String mchNo, String channelMchNo, HttpServletRequest request) {
        // 1. 提取回调原始数据
        String body = JakartaServletUtil.getBody(request);
        Map<String, String> headerMap = JakartaServletUtil.getHeaderMap(request);
        Map<String, Object> notify = new HashMap<>();
        notify.put("body", body);
        notify.put("headers", headerMap);

        // 2. 组装凭证
        StripeSdkCredential credential = configAssembler.buildConfig(channelMchNo);

        // 3. 转发到子应用验签
        StripeCallbackParseReq req = new StripeCallbackParseReq();
        req.setCredential(credential);
        req.setPayload(body);
        req.setSignature(this.getHeader(headerMap, StripePayCode.HEADER_SIGNATURE));

        DaxResult<StripeCallbackParseResp> result = stripeChannelClient.parseRefundCallback(req);
        if (result.getCode() != 0 || result.getData() == null) {
            log.error("Stripe 退款回调验签失败: channelMchNo={}", channelMchNo);
            RefundCallbackData failData = new RefundCallbackData();
            failData.setCallbackData(notify);
            failData.setCallbackStatus(CallbackStatusEnum.FAIL);
            failData.setCallbackErrorMsg("Stripe 退款回调验签失败");
            payCallbackRecordService.saveRefund(channelMchNo, failData);
            return StripePayCode.NOTIFY_FAIL;
        }

        StripeCallbackParseResp resp = result.getData();

        // 4. 构建退款回调数据, 交框架更新退款单状态
        RefundCallbackData callbackData = new RefundCallbackData();
        callbackData.setRefundNo(resp.getRefundNo());
        callbackData.setOutRefundNo(resp.getOutOrderNo());
        if (Objects.equals(StripePayCode.REFUND_STATUS_SUCCEEDED, resp.getTradeStatus())) {
            callbackData.setTradeStatus(CallbackStatusEnum.SUCCESS.getCode());
        } else {
            callbackData.setTradeErrorMsg("Stripe 退款状态非成功: " + resp.getTradeStatus());
        }
        notify.put("outRefundNo", resp.getOutOrderNo());
        notify.put("refundStatus", resp.getTradeStatus());
        callbackData.setCallbackData(notify);
        try {
            refundCallbackService.refundCallback(callbackData);
        } catch (Exception e) {
            log.error("Stripe 退款回调业务处理失败: refundNo={}", callbackData.getRefundNo(), e);
            callbackData.setCallbackStatus(CallbackStatusEnum.EXCEPTION).setCallbackErrorMsg(e.getMessage());
            payCallbackRecordService.saveRefund(channelMchNo, callbackData);
            return StripePayCode.NOTIFY_FAIL;
        }
        payCallbackRecordService.saveRefund(channelMchNo, callbackData);
        return StripePayCode.NOTIFY_SUCCESS;
    }

    /// 获取 header(大小写兼容)
    private String getHeader(Map<String, String> headerMap, String name) {
        String value = headerMap.get(name);
        return value != null ? value : headerMap.get(name.toLowerCase());
    }
}
