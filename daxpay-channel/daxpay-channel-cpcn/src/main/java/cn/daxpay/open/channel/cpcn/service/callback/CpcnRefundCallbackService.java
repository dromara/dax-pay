package cn.daxpay.open.channel.cpcn.service.callback;

import cn.daxpay.open.channel.cpcn.client.CpcnChannelClient;
import cn.daxpay.open.channel.cpcn.client.credential.CpcnSdkCredential;
import cn.daxpay.open.channel.cpcn.client.req.CpcnCallbackParseReq;
import cn.daxpay.open.channel.cpcn.client.resp.CpcnCallbackParseResp;
import cn.daxpay.open.channel.cpcn.code.CpcnCode;
import cn.daxpay.open.channel.cpcn.service.direct.CpcnDirectConfigAssembler;
import cn.daxpay.open.payment.trade.runtime.bo.RefundCallbackData;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.trade.record.service.PayCallbackRecordService;
import cn.daxpay.open.payment.trade.runtime.service.callback.RefundCallbackService;
import cn.daxpay.open.platform.core.enums.pay.notice.CallbackStatusEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/// # 中金退款回调处理服务
///
/// 中金退款异步通知(5021 NoticeURL) → 主应用接收 → 组装凭证 → 转发子应用验签 → 记录退款结果。
/// 应答返回子应用生成的 Base64 XML 应答报文。
///
/// 退款单状态流转已接入 [RefundCallbackService], 由框架完成终态守卫与状态更新。
@Slf4j
@Service
@RequiredArgsConstructor
public class CpcnRefundCallbackService {

    private final CpcnChannelClient cpcnChannelClient;
    private final CpcnDirectConfigAssembler configAssembler;
    private final RefundCallbackService refundCallbackService;
    private final PayCallbackRecordService payCallbackRecordService;

    /// 退款回调处理
    public String refundHandle(String mchNo, String channelMchNo, String message, String signature) {
        // 1. 组装凭证
        CpcnSdkCredential credential = configAssembler.buildConfig(mchNo, channelMchNo, null);

        // 2. 转发到子应用验签解析
        CpcnCallbackParseReq req = new CpcnCallbackParseReq();
        req.setCredential(credential);
        req.setMessage(message);
        req.setSignature(signature);
        Map<String, Object> notify = new HashMap<>();
        notify.put("message", message);
        notify.put("signature", signature);
        DaxResult<CpcnCallbackParseResp> result = cpcnChannelClient.parseRefundCallback(req);
        if (result.getCode() != 0 || Objects.isNull(result.getData()) || !Boolean.TRUE.equals(result.getData().getSuccess())) {
            log.error("中金退款回调验签失败: channelMchNo={}", channelMchNo);
            RefundCallbackData failData = new RefundCallbackData();
            failData.setCallbackData(notify);
            failData.setCallbackStatus(CallbackStatusEnum.FAIL);
            failData.setCallbackErrorMsg("中金退款回调验签失败");
            payCallbackRecordService.saveRefund(channelMchNo, failData);
            return CpcnCode.NOTIFY_FAIL;
        }

        // 3. 构建退款回调数据, 交框架更新退款单状态(退款成功态 Status=20)
        CpcnCallbackParseResp resp = result.getData();
        RefundCallbackData callbackData = new RefundCallbackData();
        // resp.outTradeNo 为退款时上送的 TxSN = 平台退款关联号
        callbackData.setRefundNo(resp.getOutTradeNo());
        callbackData.setFinishTime(resp.getFinishTime());
        if (Objects.equals(CpcnCode.TRADE_STATUS_REFUND_SUCCESS, resp.getTradeStatus())) {
            callbackData.setTradeStatus(CallbackStatusEnum.SUCCESS.getCode());
        } else if (Objects.equals(CpcnCode.TRADE_STATUS_FAIL, resp.getTradeStatus())) {
            callbackData.setTradeStatus(CallbackStatusEnum.FAIL.getCode());
        } else {
            callbackData.setTradeErrorMsg("中金退款状态非成功: " + resp.getTradeStatus());
        }
        callbackData.setCallbackData(notify);
        try {
            refundCallbackService.refundCallback(callbackData);
        } catch (Exception e) {
            log.error("中金退款回调业务处理失败: refundNo={}", callbackData.getRefundNo(), e);
            callbackData.setCallbackStatus(CallbackStatusEnum.EXCEPTION).setCallbackErrorMsg(e.getMessage());
            payCallbackRecordService.saveRefund(channelMchNo, callbackData);
            return CpcnCode.NOTIFY_FAIL;
        }
        payCallbackRecordService.saveRefund(channelMchNo, callbackData);
        // 业务处理成功, 返回子应用生成的 Base64 XML 应答
        return resp.getAckBody();
    }
}
