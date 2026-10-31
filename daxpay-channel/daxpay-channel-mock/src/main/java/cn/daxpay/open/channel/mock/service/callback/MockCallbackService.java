package cn.daxpay.open.channel.mock.service.callback;

import cn.daxpay.open.channel.mock.client.MockChannelClient;
import cn.daxpay.open.channel.mock.client.req.MockCallbackParseReq;
import cn.daxpay.open.channel.mock.client.resp.MockCallbackParseResp;
import cn.daxpay.open.channel.mock.service.MockCredentialSupport;
import cn.daxpay.open.payment.trade.order.dao.PayTradeManager;
import cn.daxpay.open.payment.trade.order.dao.RefundOrderManager;
import cn.daxpay.open.payment.trade.order.entity.PayTrade;
import cn.daxpay.open.payment.trade.order.entity.RefundOrder;
import cn.daxpay.open.payment.trade.record.service.PayCallbackRecordService;
import cn.daxpay.open.payment.trade.runtime.bo.CallbackData;
import cn.daxpay.open.payment.trade.runtime.bo.RefundCallbackData;
import cn.daxpay.open.payment.trade.runtime.service.callback.PayCallbackService;
import cn.daxpay.open.payment.trade.runtime.service.callback.RefundCallbackService;
import cn.daxpay.open.platform.core.enums.pay.notice.CallbackStatusEnum;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/// # Mock 模拟通道回调业务服务
///
/// 骨架照抄易支付 [cn.daxpay.open.channel.easypay.service.callback.EasyPayPayCallbackService]。
/// 模拟网关回调报文为明文单层 JSON(`event_type`/`summary`/`out_biz_no` 明文锚点/`data` 业务JSON)
/// + Mock-Sign/Mock-Timestamp/Mock-Nonce 三签名头(平台私钥对三行串 `{timestamp}\n{nonce}\n{body}\n` 签名)。
/// 主应用不持密钥不验签: 从外层 `event_type` 前缀分流, `out_biz_no` 反查订单, body 与三头**原样转发**
/// one 子应用 mock 模块验签(契约见工作区 _doc/mock网关协议契约-2026-10-26.md §八)。
///
/// - PAY.* → 支付回调: out_biz_no=平台支付交易号(mock 侧 outTradeNo 存 PayTrade.tradeNo)
///   反查 [PayTrade] → 转发解析 → 组 [CallbackData] → 回写支付单;
/// - REFUND.* → 退款回调: out_biz_no=商户退款号(退款单 relationOrderNo)
///   反查 [RefundOrder] → 转发解析 → 组 [RefundCallbackData] → 回写退款单。
///
/// 凭证经 [MockCredentialSupport] 按回调 URL 的 channelMchNo 反查 product 装配(商户级两件套),
/// 应答字符串口径与易支付一致(`success` / `fail`), 处理异常记录 EXCEPTION 回调并应答 fail 由 mock 侧留痕补发。
/// 一期不含预授权/分账回调分支。
@Slf4j
@Service
@RequiredArgsConstructor
public class MockCallbackService {

    private static final String NOTIFY_SUCCESS = "success";
    private static final String NOTIFY_FAIL = "fail";

    /// V3 网关回调外层事件类型前缀: 支付
    private static final String EVENT_PREFIX_PAY = "PAY.";
    /// V3 网关回调外层事件类型前缀: 退款
    private static final String EVENT_PREFIX_REFUND = "REFUND.";

    private final MockChannelClient mockChannelClient;
    private final MockCredentialSupport mockCredentialSupport;
    private final PayTradeManager payTradeManager;
    private final RefundOrderManager refundOrderManager;
    private final PayCallbackService payCallbackService;
    private final RefundCallbackService refundCallbackService;
    private final PayCallbackRecordService payCallbackRecordService;

    /// 回调统一处理(按外层 event_type 前缀自动区分支付/退款)
    public String handle(String channelMchNo, String body, String sign, String timestamp, String nonce) {
        JSONObject outer;
        try {
            outer = JSONUtil.parseObj(body);
        } catch (Exception e) {
            log.error("mock 回调: 外层 JSON 解析失败", e);
            CallbackData failData = new CallbackData();
            failData.setCallbackData(new LinkedHashMap<>());
            failData.setCallbackStatus(CallbackStatusEnum.FAIL);
            failData.setCallbackErrorMsg("mock 回调: 外层 JSON 解析失败");
            payCallbackRecordService.savePay(channelMchNo, failData);
            return NOTIFY_FAIL;
        }
        String eventType = outer.getStr("event_type");
        String outBizNo = outer.getStr("out_biz_no");
        boolean refund = eventType != null && eventType.startsWith(EVENT_PREFIX_REFUND);
        Map<String, Object> envelope = buildEnvelope(outer);
        try {
            if (refund) {
                return handleRefund(channelMchNo, outBizNo, body, sign, timestamp, nonce, envelope);
            }
            if (eventType != null && eventType.startsWith(EVENT_PREFIX_PAY)) {
                return handlePay(channelMchNo, outBizNo, body, sign, timestamp, nonce, envelope);
            }
            log.error("mock 回调: 未知回调类型 event_type={}", eventType);
            return NOTIFY_FAIL;
        } catch (Exception e) {
            log.error("mock 回调业务处理失败", e);
            // 处理异常按类型记录 EXCEPTION 回调并应答 fail(mock 侧留痕可补发)
            if (refund) {
                RefundCallbackData failData = new RefundCallbackData();
                failData.setCallbackData(envelope);
                failData.setRefundNo(outBizNo);
                failData.setCallbackStatus(CallbackStatusEnum.EXCEPTION);
                failData.setCallbackErrorMsg(e.getMessage());
                payCallbackRecordService.saveRefund(channelMchNo, failData);
            } else {
                CallbackData failData = new CallbackData();
                failData.setCallbackData(envelope);
                failData.setTradeNo(outBizNo);
                failData.setCallbackStatus(CallbackStatusEnum.EXCEPTION);
                failData.setCallbackErrorMsg(e.getMessage());
                payCallbackRecordService.savePay(channelMchNo, failData);
            }
            return NOTIFY_FAIL;
        }
    }

    /// 构造落库用完整外层信封(event_type/summary/out_biz_no + data 业务子对象整体嵌套):
    /// 替代原 3 键锚点(eventType/outBizNo/status), mock 报文未来新增任何字段,
    /// 回调记录 notify_info 自动可见, 不依赖解析 resp 链路(mock 报文无密文壳, 落原文无泄露面)
    private Map<String, Object> buildEnvelope(JSONObject outer) {
        Map<String, Object> envelope = new LinkedHashMap<>();
        envelope.put("event_type", outer.getStr("event_type"));
        envelope.put("summary", outer.getStr("summary"));
        envelope.put("out_biz_no", outer.getStr("out_biz_no"));
        JSONObject data = outer.getJSONObject("data");
        envelope.put("data", data != null ? new LinkedHashMap<>(data) : new LinkedHashMap<>());
        return envelope;
    }

    /// 支付回调处理
    private String handlePay(String channelMchNo, String outBizNo, String body, String sign, String timestamp,
                             String nonce, Map<String, Object> envelope) {
        // out_biz_no = 平台支付交易号(mock 下单时透传), 凭此反查支付单
        PayTrade trade = payTradeManager.findByTradeNo(outBizNo).orElse(null);
        if (trade == null) {
            log.error("mock 支付回调: 订单不存在 tradeNo={}", outBizNo);
            CallbackData failData = new CallbackData();
            failData.setCallbackData(envelope);
            failData.setTradeNo(outBizNo);
            failData.setCallbackStatus(CallbackStatusEnum.FAIL);
            failData.setCallbackErrorMsg("mock 支付回调: 订单不存在");
            payCallbackRecordService.savePay(channelMchNo, failData);
            return NOTIFY_FAIL;
        }
        // 转发 one 验签解析(凭证按回调 URL 的 channelMchNo 反查 product 装配——
        // 回调 URL 的 channelMchNo 即下单订单的通道商户号, 与订单 product 同源)
        MockCallbackParseResp resp = parse(channelMchNo, body, sign, timestamp, nonce, false);
        if (resp == null || !Boolean.TRUE.equals(resp.getSuccess())) {
            log.error("mock 支付回调解析失败: tradeNo={}", outBizNo);
            CallbackData failData = new CallbackData();
            failData.setCallbackData(envelope);
            failData.setTradeNo(outBizNo);
            failData.setCallbackStatus(CallbackStatusEnum.FAIL);
            failData.setCallbackErrorMsg("mock 支付回调解析失败(验签或解密不通过)");
            payCallbackRecordService.savePay(channelMchNo, failData);
            return NOTIFY_FAIL;
        }
        // 组装回调数据(字段方位与支付宝对齐: tradeNo=平台交易号, outTradeNo=通道交易号)
        CallbackData callbackData = new CallbackData();
        callbackData.setCallbackData(envelope);
        callbackData.setTradeNo(resp.getOutTradeNo());
        callbackData.setOutTradeNo(resp.getTradeNo());
        if (Objects.equals(resp.getStatus(), "SUCCESS")) {
            callbackData.setTradeStatus(CallbackStatusEnum.SUCCESS.getCode());
        } else {
            callbackData.setTradeErrorMsg("mock 回调状态非成功: " + resp.getStatus());
        }
        callbackData.setFinishTime(resp.getFinishTime());
        // 买家标识回填(微信 openid/支付宝 user_id 同方位, 事后风控补录用; 旧报文未携带为 null 不覆盖)
        callbackData.setBuyerId(resp.getOpenId());
        // open 的 CallbackData 无 provider 字段: 实际付款钱包仅随报文留痕, 不参与回调数据赋值
        log.info("mock 支付回调: tradeNo={}, status={}, 报文 provider={}",
                resp.getOutTradeNo(), resp.getStatus(), resp.getProvider());
        payCallbackService.payCallback(callbackData);
        payCallbackRecordService.savePay(channelMchNo, callbackData);
        return NOTIFY_SUCCESS;
    }

    /// 退款回调处理
    private String handleRefund(String channelMchNo, String outBizNo, String body, String sign, String timestamp,
                                String nonce, Map<String, Object> envelope) {
        // out_biz_no = 上送 mock 的商户退款号(=退款单 relationOrderNo, 回调/同步反查权威)
        RefundOrder refundOrder = refundOrderManager.findByRelationOrderNo(outBizNo).orElse(null);
        if (refundOrder == null) {
            log.error("mock 退款回调: 退款单不存在 outRefundNo={}", outBizNo);
            RefundCallbackData failData = new RefundCallbackData();
            failData.setCallbackData(envelope);
            failData.setRefundNo(outBizNo);
            failData.setCallbackStatus(CallbackStatusEnum.FAIL);
            failData.setCallbackErrorMsg("mock 退款回调: 退款单不存在");
            payCallbackRecordService.saveRefund(channelMchNo, failData);
            return NOTIFY_FAIL;
        }
        // 转发 one 验签解析(凭证按回调 URL 的 channelMchNo 反查 product 装配, 与支付同口径)
        MockCallbackParseResp resp = parse(channelMchNo, body, sign, timestamp, nonce, true);
        if (resp == null || !Boolean.TRUE.equals(resp.getSuccess())) {
            log.error("mock 退款回调解析失败: outRefundNo={}", outBizNo);
            RefundCallbackData failData = new RefundCallbackData();
            failData.setCallbackData(envelope);
            failData.setRefundNo(outBizNo);
            failData.setCallbackStatus(CallbackStatusEnum.FAIL);
            failData.setCallbackErrorMsg("mock 退款回调解析失败(验签或解密不通过)");
            payCallbackRecordService.saveRefund(channelMchNo, failData);
            return NOTIFY_FAIL;
        }
        // 组装退款回调数据(refundId = mock 通道退款流水号)
        RefundCallbackData callbackData = new RefundCallbackData();
        callbackData.setCallbackData(envelope);
        callbackData.setRefundNo(refundOrder.getRefundNo());
        callbackData.setOutRefundNo(resp.getRefundId());
        if (Objects.equals(resp.getStatus(), "SUCCESS")) {
            callbackData.setTradeStatus(CallbackStatusEnum.SUCCESS.getCode());
        } else {
            callbackData.setTradeErrorMsg("mock 退款回调状态非成功: " + resp.getStatus());
        }
        callbackData.setFinishTime(resp.getFinishTime());
        refundCallbackService.refundCallback(callbackData);
        payCallbackRecordService.saveRefund(channelMchNo, callbackData);
        return NOTIFY_SUCCESS;
    }

    /// 转发 one 验签解析(body 与三签名头原文透传; 凭证按通道商户号反查 product 装配, refund 选择退款解析端点)
    private MockCallbackParseResp parse(String channelMchNo, String body, String sign, String timestamp,
                                        String nonce, boolean refund) {
        MockCallbackParseReq req = new MockCallbackParseReq();
        req.setCredential(mockCredentialSupport.build(channelMchNo));
        req.setBody(body);
        req.setSign(sign);
        req.setTimestamp(timestamp);
        req.setNonce(nonce);
        var result = refund
                ? mockChannelClient.parseRefundCallback(req)
                : mockChannelClient.parsePayCallback(req);
        if (result.getCode() != 0) {
            log.error("mock 回调: one 解析失败: {}", result.getMsg());
            return null;
        }
        return result.getData();
    }
}
