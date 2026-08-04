package cn.daxpay.open.channel.stripe.service.callback;

import cn.daxpay.open.channel.stripe.client.StripeChannelClient;
import cn.daxpay.open.channel.stripe.client.credential.StripeSdkCredential;
import cn.daxpay.open.channel.stripe.client.req.StripeCallbackParseReq;
import cn.daxpay.open.channel.stripe.client.resp.StripeCallbackParseResp;
import cn.daxpay.open.channel.stripe.code.StripePayCode;
import cn.daxpay.open.channel.stripe.service.StripeConfigAssembler;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.trade.record.service.PayCallbackRecordService;
import cn.daxpay.open.payment.trade.runtime.bo.CallbackData;
import cn.daxpay.open.payment.trade.runtime.service.callback.PayCallbackService;
import cn.daxpay.open.platform.core.enums.pay.notice.CallbackStatusEnum;
import cn.hutool.extra.servlet.JakartaServletUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/// # Stripe 支付回调处理服务
///
/// Stripe 异步通知 → 主应用接收原始 body + Stripe-Signature 头 → 按 channelMchNo 组装凭证 →
/// 转发到子应用(channel-three)用 Webhook.constructEvent 验签解析 →
/// 构建 [CallbackData] 交由 [PayCallbackService] 更新订单状态。
///
/// 主应用零 SDK 依赖, 验签能力集中在 channel-three。
@Slf4j
@Service
@RequiredArgsConstructor
public class StripePayCallbackService {

    private final StripeChannelClient stripeChannelClient;
    private final StripeConfigAssembler configAssembler;
    private final PayCallbackService payCallbackService;
    private final PayCallbackRecordService payCallbackRecordService;

    /// 支付回调处理
    public String payHandle(String mchNo, String channelMchNo, HttpServletRequest request) {
        // 1. 提取回调原始数据(验签必须用 raw body, 不能被 JSON 解析器预处理)
        String body = JakartaServletUtil.getBody(request);
        Map<String, String> headerMap = JakartaServletUtil.getHeaderMap(request);

        // 2. 组装凭证(回调验签只需 webhookSecret)
        StripeSdkCredential credential = configAssembler.buildConfig(channelMchNo);

        // 3. 转发到子应用验签解析
        StripeCallbackParseResp resp = this.parsePayCallback(credential, body, headerMap);
        if (resp == null) {
            log.error("Stripe 支付回调验签失败: channelMchNo={}", channelMchNo);
            CallbackData failData = new CallbackData();
            Map<String, Object> notify = new HashMap<>();
            notify.put("body", body);
            notify.put("headers", headerMap);
            failData.setCallbackData(notify);
            failData.setCallbackStatus(CallbackStatusEnum.FAIL);
            failData.setCallbackErrorMsg("Stripe 支付回调验签失败");
            payCallbackRecordService.savePay(channelMchNo, failData);
            return StripePayCode.NOTIFY_FAIL;
        }

        // 4. 构建回调数据交由框架处理
        CallbackData callbackData = this.buildCallbackData(resp);
        Map<String, Object> notify = new HashMap<>();
        notify.put("body", body);
        notify.put("outOrderNo", resp.getOutOrderNo());
        notify.put("tradeStatus", resp.getTradeStatus());
        callbackData.setCallbackData(notify);
        try {
            payCallbackService.payCallback(callbackData);
        } catch (Exception e) {
            log.error("Stripe 支付回调业务处理失败: tradeNo={}", callbackData.getTradeNo(), e);
            callbackData.setCallbackStatus(CallbackStatusEnum.EXCEPTION).setCallbackErrorMsg(e.getMessage());
            payCallbackRecordService.savePay(channelMchNo, callbackData);
            return StripePayCode.NOTIFY_FAIL;
        }
        payCallbackRecordService.savePay(channelMchNo, callbackData);
        return StripePayCode.NOTIFY_SUCCESS;
    }

    /// 转发到子应用验签解析
    private StripeCallbackParseResp parsePayCallback(StripeSdkCredential credential,
                                                      String body, Map<String, String> headerMap) {
        StripeCallbackParseReq req = new StripeCallbackParseReq();
        req.setCredential(credential);
        req.setPayload(body);
        req.setSignature(this.getHeader(headerMap, StripePayCode.HEADER_SIGNATURE));
        DaxResult<StripeCallbackParseResp> result = stripeChannelClient.parsePayCallback(req);
        if (result.getCode() != 0) {
            log.error("Stripe 支付回调验签通道调用失败: {}", result.getMsg());
            return null;
        }
        return result.getData();
    }

    /// 构建框架回调数据
    private CallbackData buildCallbackData(StripeCallbackParseResp resp) {
        CallbackData data = new CallbackData();
        // resp.orderNo 是下单时写入的 metadata.orderNo = 平台 tradeNo
        data.setTradeNo(resp.getOrderNo());
        // resp.outOrderNo 是 Stripe PaymentIntent ID
        data.setOutTradeNo(resp.getOutOrderNo());
        // 支付成功时间
        if (resp.getFinishTime() != null && !resp.getFinishTime().isBlank()) {
            data.setFinishTime(OffsetDateTime.parse(resp.getFinishTime()));
        }
        // 交易状态映射: succeeded → 回调 SUCCESS; 其他 → 非成功(触发 fail 处理)
        if (Objects.equals(StripePayCode.INTENT_STATUS_SUCCEEDED, resp.getTradeStatus())) {
            data.setTradeStatus(CallbackStatusEnum.SUCCESS.getCode());
        } else {
            data.setTradeStatus(resp.getTradeStatus());
            data.setCallbackErrorMsg("Stripe 回调状态非成功: " + resp.getTradeStatus());
        }
        return data;
    }

    /// 获取 header(大小写兼容)
    private String getHeader(Map<String, String> headerMap, String name) {
        String value = headerMap.get(name);
        return value != null ? value : headerMap.get(name.toLowerCase());
    }
}
