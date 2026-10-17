package cn.daxpay.open.channel.jlpay.service.callback;

import cn.daxpay.open.channel.jlpay.client.JlpayChannelClient;
import cn.daxpay.open.channel.jlpay.client.credential.JlpaySdkCredential;
import cn.daxpay.open.channel.jlpay.client.req.JlpayCallbackParseReq;
import cn.daxpay.open.channel.jlpay.client.resp.JlpayCallbackParseResp;
import cn.daxpay.open.channel.jlpay.code.JlpayCode;
import cn.daxpay.open.channel.jlpay.service.direct.JlpayDirectConfigAssembler;
import cn.daxpay.open.payment.trade.runtime.bo.CallbackData;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.trade.record.service.PayCallbackRecordService;
import cn.daxpay.open.payment.trade.runtime.service.callback.PayCallbackService;
import cn.daxpay.open.platform.core.enums.pay.notice.CallbackStatusEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/// # 嘉联支付回调处理服务
///
/// 嘉联异步通知(JSON body + 签名请求头) → 主应用接收 → 按 channelMchNo 组装凭证 →
/// 转发到子应用用平台 SM2 公钥验签 →
/// 构建 [CallbackData] 交由 [PayCallbackService] 更新订单状态,
/// 应答返回嘉联规约 JSON(`{"ret_code":"00000"}`)。
///
/// 嘉联仅有支付异步通知(退款为同步终态, 无退款通知)。
@Slf4j
@Service
@RequiredArgsConstructor
public class JlpayPayCallbackService {

    private final JlpayChannelClient jlpayChannelClient;
    private final JlpayDirectConfigAssembler configAssembler;
    private final PayCallbackService payCallbackService;
    private final PayCallbackRecordService payCallbackRecordService;

    /// 支付回调处理
    public String payHandle(String mchNo, String channelMchNo, String body, String callbackPath,
                            String timestamp, String nonce, String sign) {
        // 1. 组装凭证(平台公钥验签)
        JlpaySdkCredential credential = configAssembler.buildConfig(mchNo, channelMchNo, null);

        // 2. 转发到子应用验签
        JlpayCallbackParseReq req = new JlpayCallbackParseReq();
        req.setCredential(credential);
        req.setBody(body);
        req.setCallbackPath(callbackPath);
        req.setTimestamp(timestamp);
        req.setNonce(nonce);
        req.setSign(sign);
        Map<String, Object> notify = new HashMap<>();
        notify.put("body", body);
        DaxResult<JlpayCallbackParseResp> result = jlpayChannelClient.parsePayCallback(req);
        if (result.getCode() != 0 || Objects.isNull(result.getData()) || !Boolean.TRUE.equals(result.getData().getSuccess())) {
            log.error("嘉联支付回调验签失败: channelMchNo={}", channelMchNo);
            CallbackData failData = new CallbackData();
            failData.setCallbackData(notify);
            failData.setCallbackStatus(CallbackStatusEnum.FAIL);
            failData.setCallbackErrorMsg("嘉联支付回调验签失败");
            payCallbackRecordService.savePay(channelMchNo, failData);
            return JlpayCode.NOTIFY_SIGN_FAIL;
        }

        // 3. 构建回调数据交由框架处理
        JlpayCallbackParseResp resp = result.getData();
        CallbackData callbackData = this.buildCallbackData(resp);
        callbackData.setCallbackData(notify);
        try {
            payCallbackService.payCallback(callbackData);
        } catch (Exception e) {
            log.error("嘉联支付回调业务处理失败: tradeNo={}", callbackData.getTradeNo(), e);
            callbackData.setCallbackStatus(CallbackStatusEnum.EXCEPTION).setCallbackErrorMsg(e.getMessage());
            payCallbackRecordService.savePay(channelMchNo, callbackData);
            // 业务处理异常返回失败应答, 触发嘉联重发通知自愈
            return JlpayCode.NOTIFY_PROCESS_FAIL;
        }
        payCallbackRecordService.savePay(channelMchNo, callbackData);
        // 业务处理成功, 返回嘉联规约应答
        return resp.getAckBody();
    }

    /// 构建框架回调数据
    private CallbackData buildCallbackData(JlpayCallbackParseResp resp) {
        CallbackData data = new CallbackData();
        // resp.outTradeNo 是支付时传入的 out_trade_no = 平台 tradeNo
        data.setTradeNo(resp.getOutTradeNo());
        // resp.outOrderNo 是嘉联交易号 transaction_id
        data.setOutTradeNo(resp.getOutOrderNo());
        // 完成时间(子应用已解析为 OffsetDateTime)
        data.setFinishTime(resp.getFinishTime());
        // 交易状态映射(2 成功, 其余传原始值)
        data.setTradeStatus(this.mapStatus(resp.getTradeStatus()));
        return data;
    }

    /// 嘉联支付状态码 → CallbackStatusEnum
    private String mapStatus(String tradeStatus) {
        if (JlpayCode.TRADE_STATUS_SUCCESS.equals(tradeStatus)) {
            return CallbackStatusEnum.SUCCESS.getCode();
        }
        // 非成功状态直接传原始值
        return tradeStatus;
    }
}
