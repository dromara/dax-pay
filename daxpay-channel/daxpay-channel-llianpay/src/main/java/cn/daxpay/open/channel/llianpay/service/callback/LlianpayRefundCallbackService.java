package cn.daxpay.open.channel.llianpay.service.callback;

import cn.daxpay.open.channel.llianpay.client.LlianpayChannelClient;
import cn.daxpay.open.channel.llianpay.client.credential.LlianpaySdkCredential;
import cn.daxpay.open.channel.llianpay.client.req.LlianpayCallbackParseReq;
import cn.daxpay.open.channel.llianpay.client.resp.LlianpayCallbackParseResp;
import cn.daxpay.open.channel.llianpay.code.LlianpayCode;
import cn.daxpay.open.channel.llianpay.service.direct.LlianpayDirectConfigAssembler;
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

/// # 连连退款回调处理服务
///
/// 连连退款异步通知(notify_url) → 主应用接收 → 组装凭证 → 转发子应用验签 → 记录退款结果。
/// 应答返回连连规约串(Success/Fail)。
///
/// 退款单状态流转已接入 [RefundCallbackService], 由框架完成终态守卫与状态更新。
@Slf4j
@Service
@RequiredArgsConstructor
public class LlianpayRefundCallbackService {

    private final LlianpayChannelClient llianpayChannelClient;
    private final LlianpayDirectConfigAssembler configAssembler;
    private final RefundCallbackService refundCallbackService;
    private final PayCallbackRecordService payCallbackRecordService;

    /// 退款回调处理
    public String refundHandle(String mchNo, String channelMchNo, String body, String sign) {
        // 1. 组装凭证
        LlianpaySdkCredential credential = configAssembler.buildConfig(mchNo, channelMchNo, null);

        // 2. 转发到子应用验签解析
        LlianpayCallbackParseReq req = new LlianpayCallbackParseReq();
        req.setCredential(credential);
        req.setBody(body);
        req.setSign(sign);
        Map<String, Object> notify = new HashMap<>();
        notify.put("body", body);
        DaxResult<LlianpayCallbackParseResp> result = llianpayChannelClient.parseRefundCallback(req);
        if (result.getCode() != 0 || Objects.isNull(result.getData()) || !Boolean.TRUE.equals(result.getData().getSuccess())) {
            log.error("连连退款回调验签失败: channelMchNo={}", channelMchNo);
            RefundCallbackData failData = new RefundCallbackData();
            failData.setCallbackData(notify);
            failData.setCallbackStatus(CallbackStatusEnum.FAIL);
            failData.setCallbackErrorMsg("连连退款回调验签失败");
            payCallbackRecordService.saveRefund(channelMchNo, failData);
            return LlianpayCode.NOTIFY_FAIL;
        }

        // 3. 构建退款回调数据, 交框架更新退款单状态
        LlianpayCallbackParseResp resp = result.getData();
        RefundCallbackData callbackData = new RefundCallbackData();
        // resp.outTradeNo 为退款时传入的 refund_seqno = 平台退款关联号
        callbackData.setRefundNo(resp.getOutTradeNo());
        callbackData.setFinishTime(resp.getFinishTime());
        if (Objects.equals(LlianpayCode.TRADE_STATUS_SUCCESS, resp.getTradeStatus())) {
            callbackData.setTradeStatus(CallbackStatusEnum.SUCCESS.getCode());
        } else {
            callbackData.setTradeErrorMsg("连连退款状态非成功: " + resp.getTradeStatus());
        }
        callbackData.setCallbackData(notify);
        try {
            refundCallbackService.refundCallback(callbackData);
        } catch (Exception e) {
            log.error("连连退款回调业务处理失败: refundNo={}", callbackData.getRefundNo(), e);
            callbackData.setCallbackStatus(CallbackStatusEnum.EXCEPTION).setCallbackErrorMsg(e.getMessage());
            payCallbackRecordService.saveRefund(channelMchNo, callbackData);
            return LlianpayCode.NOTIFY_FAIL;
        }
        payCallbackRecordService.saveRefund(channelMchNo, callbackData);
        // 业务处理成功, 返回连连规约应答串(Success)
        return resp.getAckBody();
    }
}
