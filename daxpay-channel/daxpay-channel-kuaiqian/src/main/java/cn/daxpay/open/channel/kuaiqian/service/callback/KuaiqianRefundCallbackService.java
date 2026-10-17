package cn.daxpay.open.channel.kuaiqian.service.callback;

import cn.daxpay.open.channel.kuaiqian.client.KuaiqianChannelClient;
import cn.daxpay.open.channel.kuaiqian.client.credential.KuaiqianSdkCredential;
import cn.daxpay.open.channel.kuaiqian.client.req.KuaiqianCallbackParseReq;
import cn.daxpay.open.channel.kuaiqian.client.resp.KuaiqianCallbackParseResp;
import cn.daxpay.open.channel.kuaiqian.code.KuaiqianCode;
import cn.daxpay.open.channel.kuaiqian.service.direct.KuaiqianDirectConfigAssembler;
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

/// # 快钱退款回调处理服务
///
/// 快钱退款异步通知(A7003 tr3Url) → 主应用接收 → 组装凭证 → 转发子应用解密验签 → 记录退款结果。
/// 应答返回子应用生成的 A9005 加密加签报文。
///
/// 退款单状态流转已接入 [RefundCallbackService], 由框架完成终态守卫与状态更新。
@Slf4j
@Service
@RequiredArgsConstructor
public class KuaiqianRefundCallbackService {

    private final KuaiqianChannelClient kuaiqianChannelClient;
    private final KuaiqianDirectConfigAssembler configAssembler;
    private final RefundCallbackService refundCallbackService;
    private final PayCallbackRecordService payCallbackRecordService;

    /// 退款回调处理
    public String refundHandle(String mchNo, String channelMchNo, String body) {
        // 1. 组装凭证
        KuaiqianSdkCredential credential = configAssembler.buildConfig(mchNo, channelMchNo, null);

        // 2. 转发到子应用解密验签
        KuaiqianCallbackParseReq req = new KuaiqianCallbackParseReq();
        req.setCredential(credential);
        req.setBody(body);
        Map<String, Object> notify = new HashMap<>();
        notify.put("body", body);
        DaxResult<KuaiqianCallbackParseResp> result = kuaiqianChannelClient.parseRefundCallback(req);
        if (result.getCode() != 0 || Objects.isNull(result.getData()) || !Boolean.TRUE.equals(result.getData().getSuccess())) {
            log.error("快钱退款回调验签失败: channelMchNo={}", channelMchNo);
            RefundCallbackData failData = new RefundCallbackData();
            failData.setCallbackData(notify);
            failData.setCallbackStatus(CallbackStatusEnum.FAIL);
            failData.setCallbackErrorMsg("快钱退款回调验签失败");
            payCallbackRecordService.saveRefund(channelMchNo, failData);
            return KuaiqianCode.NOTIFY_FAIL;
        }

        // 3. 构建退款回调数据, 交框架更新退款单状态
        KuaiqianCallbackParseResp resp = result.getData();
        RefundCallbackData callbackData = new RefundCallbackData();
        // resp.outTradeNo 为退款时传入的 externalRefNumber = 平台退款关联号
        callbackData.setRefundNo(resp.getOutTradeNo());
        callbackData.setFinishTime(resp.getFinishTime());
        if (Objects.equals(KuaiqianCode.TRADE_STATUS_SUCCESS, resp.getTradeStatus())) {
            callbackData.setTradeStatus(CallbackStatusEnum.SUCCESS.getCode());
        } else {
            callbackData.setTradeErrorMsg("快钱退款状态非成功: " + resp.getTradeStatus());
        }
        callbackData.setCallbackData(notify);
        try {
            refundCallbackService.refundCallback(callbackData);
        } catch (Exception e) {
            log.error("快钱退款回调业务处理失败: refundNo={}", callbackData.getRefundNo(), e);
            callbackData.setCallbackStatus(CallbackStatusEnum.EXCEPTION).setCallbackErrorMsg(e.getMessage());
            payCallbackRecordService.saveRefund(channelMchNo, callbackData);
            return KuaiqianCode.NOTIFY_FAIL;
        }
        payCallbackRecordService.saveRefund(channelMchNo, callbackData);
        // 业务处理成功, 返回 A9005 加密加签应答
        return resp.getAckBody();
    }
}
