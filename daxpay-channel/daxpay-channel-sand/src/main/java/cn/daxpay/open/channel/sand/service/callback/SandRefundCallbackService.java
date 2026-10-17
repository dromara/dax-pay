package cn.daxpay.open.channel.sand.service.callback;

import cn.daxpay.open.channel.sand.client.SandChannelClient;
import cn.daxpay.open.channel.sand.client.credential.SandSdkCredential;
import cn.daxpay.open.channel.sand.client.req.SandCallbackParseReq;
import cn.daxpay.open.channel.sand.client.resp.SandCallbackParseResp;
import cn.daxpay.open.channel.sand.code.SandCode;
import cn.daxpay.open.channel.sand.service.direct.SandDirectConfigAssembler;
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

/// # 杉德退款回调处理服务
///
/// 杉德退款异步通知(表单 bizData + sign) → 主应用接收 → 组装凭证 → 转发子应用验签 → 记录退款结果。
/// 应答按处理结果返回杉德规约纯文本(`respCode=000000` / `respCode=020002`)。
///
/// 退款单状态流转已接入 [RefundCallbackService], 由框架完成终态守卫与状态更新。
@Slf4j
@Service
@RequiredArgsConstructor
public class SandRefundCallbackService {

    private final SandChannelClient sandChannelClient;
    private final SandDirectConfigAssembler configAssembler;
    private final RefundCallbackService refundCallbackService;
    private final PayCallbackRecordService payCallbackRecordService;

    /// 退款回调处理
    public String refundHandle(String mchNo, String channelMchNo, String bizData, String sign) {
        // 1. 组装凭证
        SandSdkCredential credential = configAssembler.buildConfig(mchNo, channelMchNo, null);

        // 2. 转发到子应用验签解析
        SandCallbackParseReq req = new SandCallbackParseReq();
        req.setCredential(credential);
        req.setBizData(bizData);
        req.setSign(sign);
        Map<String, Object> notify = new HashMap<>();
        notify.put("bizData", bizData);
        notify.put("sign", sign);
        DaxResult<SandCallbackParseResp> result = sandChannelClient.parseRefundCallback(req);
        if (result.getCode() != 0 || Objects.isNull(result.getData()) || !Boolean.TRUE.equals(result.getData().getSuccess())) {
            log.error("杉德退款回调验签失败: channelMchNo={}", channelMchNo);
            RefundCallbackData failData = new RefundCallbackData();
            failData.setCallbackData(notify);
            failData.setCallbackStatus(CallbackStatusEnum.FAIL);
            failData.setCallbackErrorMsg("杉德退款回调验签失败");
            payCallbackRecordService.saveRefund(channelMchNo, failData);
            return SandCode.NOTIFY_FAIL;
        }

        // 3. 构建退款回调数据, 交框架更新退款单状态
        SandCallbackParseResp resp = result.getData();
        RefundCallbackData callbackData = new RefundCallbackData();
        // resp.outTradeNo 为退款时传入的 outOrderNo = 平台退款关联号
        callbackData.setRefundNo(resp.getOutTradeNo());
        callbackData.setFinishTime(resp.getFinishTime());
        if (Objects.equals(SandCode.ORDER_STATUS_SUCCESS, resp.getTradeStatus())) {
            callbackData.setTradeStatus(CallbackStatusEnum.SUCCESS.getCode());
        } else {
            callbackData.setTradeErrorMsg("杉德退款状态非成功: " + resp.getTradeStatus());
        }
        callbackData.setCallbackData(notify);
        try {
            refundCallbackService.refundCallback(callbackData);
        } catch (Exception e) {
            log.error("杉德退款回调业务处理失败: refundNo={}", callbackData.getRefundNo(), e);
            callbackData.setCallbackStatus(CallbackStatusEnum.EXCEPTION).setCallbackErrorMsg(e.getMessage());
            payCallbackRecordService.saveRefund(channelMchNo, callbackData);
            return SandCode.NOTIFY_FAIL;
        }
        payCallbackRecordService.saveRefund(channelMchNo, callbackData);
        // 业务处理成功, 返回杉德规约应答
        return SandCode.NOTIFY_SUCCESS;
    }
}
