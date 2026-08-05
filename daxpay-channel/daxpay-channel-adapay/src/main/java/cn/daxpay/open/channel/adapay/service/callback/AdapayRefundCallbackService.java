package cn.daxpay.open.channel.adapay.service.callback;

import cn.daxpay.open.channel.adapay.client.AdapayChannelClient;
import cn.daxpay.open.channel.adapay.client.credential.AdapaySdkCredential;
import cn.daxpay.open.channel.adapay.client.req.AdapayCallbackParseReq;
import cn.daxpay.open.channel.adapay.client.resp.AdapayCallbackParseResp;
import cn.daxpay.open.channel.adapay.code.AdapayCode;
import cn.daxpay.open.payment.trade.runtime.bo.RefundCallbackData;
import cn.daxpay.open.payment.trade.record.service.PayCallbackRecordService;
import cn.daxpay.open.payment.trade.runtime.service.callback.RefundCallbackService;
import cn.daxpay.open.platform.core.enums.pay.notice.CallbackStatusEnum;
import cn.hutool.core.util.StrUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/// # Adapay 退款回调处理服务
///
/// Adapay 退款异步通知(表单参数 data + sign) → 主应用提取表单参数 → 转发子应用验签与解析 →
/// 构建 [RefundCallbackData] 交由 [RefundCallbackService] 更新退款单状态。
///
/// 验签只需全局平台公钥(子应用内置默认), 主应用零加密代码。
@Slf4j
@Service
@RequiredArgsConstructor
public class AdapayRefundCallbackService {

    private final AdapayChannelClient adapayChannelClient;
    private final RefundCallbackService refundCallbackService;
    private final PayCallbackRecordService payCallbackRecordService;

    /// 退款回调处理
    public String refundHandle(String channelMchNo, HttpServletRequest request) {
        // 提取回调表单参数(Adapay 异步通知为 application/x-www-form-urlencoded, 含 data + sign)
        String data = request.getParameter("data");
        String sign = request.getParameter("sign");
        Map<String, Object> notify = new HashMap<>();
        notify.put("data", data);
        notify.put("sign", sign);
        if (StrUtil.isBlank(data) || StrUtil.isBlank(sign)) {
            log.error("Adapay 退款回调: data 或 sign 为空");
            RefundCallbackData failData = new RefundCallbackData();
            failData.setCallbackData(notify);
            failData.setCallbackStatus(CallbackStatusEnum.FAIL);
            failData.setCallbackErrorMsg("Adapay 退款回调: data 或 sign 为空");
            payCallbackRecordService.saveRefund(channelMchNo, failData);
            return AdapayCode.NOTIFY_FAIL;
        }

        // 转发子应用验签解析(publicKey 为空, 子应用用全局默认平台公钥)
        AdapaySdkCredential credential = new AdapaySdkCredential();
        AdapayCallbackParseReq req = new AdapayCallbackParseReq();
        req.setCredential(credential);
        req.setData(data);
        req.setSign(sign);
        var result = adapayChannelClient.parseRefundCallback(req);
        if (result.getCode() != 0) {
            log.error("Adapay 退款回调: 子应用解析失败: {}", result.getMsg());
            RefundCallbackData failData = new RefundCallbackData();
            failData.setCallbackData(notify);
            failData.setCallbackStatus(CallbackStatusEnum.FAIL);
            failData.setCallbackErrorMsg("Adapay 退款回调: 子应用解析失败");
            payCallbackRecordService.saveRefund(channelMchNo, failData);
            return AdapayCode.NOTIFY_FAIL;
        }
        AdapayCallbackParseResp resp = result.getData();
        if (resp == null || !Boolean.TRUE.equals(resp.getSuccess())) {
            log.error("Adapay 退款回调验签失败");
            RefundCallbackData failData = new RefundCallbackData();
            failData.setCallbackData(notify);
            failData.setCallbackStatus(CallbackStatusEnum.FAIL);
            failData.setCallbackErrorMsg("Adapay 退款回调验签失败");
            payCallbackRecordService.saveRefund(channelMchNo, failData);
            return AdapayCode.NOTIFY_FAIL;
        }

        // 构建退款回调数据, 交框架更新退款单状态
        RefundCallbackData callbackData = new RefundCallbackData();
        // out_refund_no = 平台退款号
        callbackData.setRefundNo(resp.getOutTradeNo());
        callbackData.setOutRefundNo(resp.getOutRefundNo());
        if (Objects.equals(resp.getTradeStatus(), "SUCCESS")) {
            callbackData.setTradeStatus(CallbackStatusEnum.SUCCESS.getCode());
        } else {
            callbackData.setTradeErrorMsg("Adapay 退款状态非成功: " + resp.getTradeStatus());
        }
        callbackData.setFinishTime(resp.getFinishTime());
        notify.put("outTradeNo", resp.getOutTradeNo());
        notify.put("outRefundNo", resp.getOutRefundNo());
        notify.put("tradeStatus", resp.getTradeStatus());
        callbackData.setCallbackData(notify);
        try {
            refundCallbackService.refundCallback(callbackData);
        } catch (Exception e) {
            log.error("Adapay 退款回调业务处理失败: refundNo={}", callbackData.getRefundNo(), e);
            callbackData.setCallbackStatus(CallbackStatusEnum.EXCEPTION).setCallbackErrorMsg(e.getMessage());
            payCallbackRecordService.saveRefund(channelMchNo, callbackData);
            return AdapayCode.NOTIFY_FAIL;
        }
        payCallbackRecordService.saveRefund(channelMchNo, callbackData);
        return AdapayCode.NOTIFY_SUCCESS;
    }
}
