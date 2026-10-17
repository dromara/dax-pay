package cn.daxpay.open.channel.youzan.service.callback;

import cn.daxpay.open.channel.youzan.client.YouzanChannelClient;
import cn.daxpay.open.channel.youzan.client.credential.YouzanSdkCredential;
import cn.daxpay.open.channel.youzan.client.req.YouzanCallbackParseReq;
import cn.daxpay.open.channel.youzan.client.resp.YouzanCallbackParseResp;
import cn.daxpay.open.channel.youzan.code.YouzanCode;
import cn.daxpay.open.channel.youzan.service.direct.YouzanDirectConfigAssembler;
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

/// # 有赞退款回调处理服务
///
/// 有赞退款异步通知(notify_url) → 主应用接收 → 组装凭证 → 转发子应用验签 → 记录退款结果。
/// 应答返回 `{"success":true}` JSON 规约串(对齐参考实现)。
///
/// 参考实现未消费退款回调的业务字段(仅验签应答), 退款单号按报文 outer_trade_no 反查
/// (有赞退款通知以原退款请求上下文推送, 字段名以联调实测为准)。
///
/// 退款单状态流转已接入 [RefundCallbackService], 由框架完成终态守卫与状态更新。
@Slf4j
@Service
@RequiredArgsConstructor
public class YouzanRefundCallbackService {

    private final YouzanChannelClient youzanChannelClient;
    private final YouzanDirectConfigAssembler configAssembler;
    private final RefundCallbackService refundCallbackService;
    private final PayCallbackRecordService payCallbackRecordService;

    /// 退款回调处理
    public String refundHandle(String mchNo, String channelMchNo, String body, String sign, String timestamp, String noncestr) {
        // 1. 组装凭证
        YouzanSdkCredential credential = configAssembler.buildConfig(mchNo, channelMchNo, null);

        // 2. 转发到子应用验签解析
        YouzanCallbackParseReq req = new YouzanCallbackParseReq();
        req.setCredential(credential);
        req.setBody(body);
        req.setSign(sign);
        req.setTimestamp(timestamp);
        req.setNoncestr(noncestr);
        Map<String, Object> notify = new HashMap<>();
        notify.put("body", body);
        DaxResult<YouzanCallbackParseResp> result = youzanChannelClient.parseRefundCallback(req);
        if (result.getCode() != 0 || Objects.isNull(result.getData()) || !Boolean.TRUE.equals(result.getData().getSuccess())) {
            log.error("有赞退款回调验签失败: channelMchNo={}", channelMchNo);
            RefundCallbackData failData = new RefundCallbackData();
            failData.setCallbackData(notify);
            failData.setCallbackStatus(CallbackStatusEnum.FAIL);
            failData.setCallbackErrorMsg("有赞退款回调验签失败");
            payCallbackRecordService.saveRefund(channelMchNo, failData);
            return YouzanCode.NOTIFY_FAIL;
        }

        // 3. 构建退款回调数据, 交框架更新退款单状态
        YouzanCallbackParseResp resp = result.getData();
        RefundCallbackData callbackData = new RefundCallbackData();
        // 退款通知的 outer_trade_no 为退款请求上下文单号(以联调实测为准)
        callbackData.setRefundNo(resp.getOutTradeNo());
        callbackData.setFinishTime(resp.getFinishTime());
        if (Objects.equals(YouzanCode.TRADE_STATUS_SUCCESS, resp.getTradeStatus())) {
            callbackData.setTradeStatus(CallbackStatusEnum.SUCCESS.getCode());
        } else {
            callbackData.setTradeErrorMsg("有赞退款状态非成功: " + resp.getTradeStatus());
        }
        callbackData.setCallbackData(notify);
        try {
            refundCallbackService.refundCallback(callbackData);
        } catch (Exception e) {
            log.error("有赞退款回调业务处理失败: refundNo={}", callbackData.getRefundNo(), e);
            callbackData.setCallbackStatus(CallbackStatusEnum.EXCEPTION).setCallbackErrorMsg(e.getMessage());
            payCallbackRecordService.saveRefund(channelMchNo, callbackData);
            return YouzanCode.NOTIFY_FAIL;
        }
        payCallbackRecordService.saveRefund(channelMchNo, callbackData);
        // 4. 业务处理成功, 返回有赞约定的 JSON 应答
        return YouzanCode.NOTIFY_SUCCESS;
    }
}
