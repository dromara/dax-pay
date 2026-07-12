package cn.daxpay.open.channel.adapay.service.callback;

import cn.daxpay.open.channel.adapay.client.AdapayChannelClient;
import cn.daxpay.open.channel.adapay.client.credential.AdapaySdkCredential;
import cn.daxpay.open.channel.adapay.client.req.AdapayCallbackParseReq;
import cn.daxpay.open.channel.adapay.client.resp.AdapayCallbackParseResp;
import cn.daxpay.open.channel.adapay.code.AdapayCode;
import cn.daxpay.open.payment.common.callback.RefundCallbackData;
import cn.daxpay.open.payment.core.trade.runtime.service.callback.RefundCallbackService;
import cn.daxpay.open.platform.core.enums.pay.notice.CallbackStatusEnum;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.JakartaServletUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

/// # Adapay 退款回调处理服务
///
/// Adapay 退款异步通知 → 主应用接收原始 body → 转发子应用验签与解析 →
/// 构建 [RefundCallbackData] 交由 [RefundCallbackService] 更新退款单状态。
///
/// 验签只需全局平台公钥(子应用内置默认), 主应用零加密代码。
@Slf4j
@Service
@RequiredArgsConstructor
public class AdapayRefundCallbackService {

    private final AdapayChannelClient adapayChannelClient;
    private final RefundCallbackService refundCallbackService;

    /// 退款回调处理
    public String refundHandle(HttpServletRequest request) {
        String body = JakartaServletUtil.getBody(request);
        if (StrUtil.isBlank(body)) {
            log.error("Adapay 退款回调: body 为空");
            return AdapayCode.NOTIFY_FAIL;
        }

        // 转发子应用验签解析(publicKey 为空, 子应用用全局默认平台公钥)
        AdapaySdkCredential credential = new AdapaySdkCredential();
        AdapayCallbackParseReq req = new AdapayCallbackParseReq();
        req.setCredential(credential);
        req.setBody(body);
        var result = adapayChannelClient.parseRefundCallback(req);
        if (result.getCode() != 0) {
            log.error("Adapay 退款回调: 子应用解析失败: {}", result.getMsg());
            return AdapayCode.NOTIFY_FAIL;
        }
        AdapayCallbackParseResp resp = result.getData();
        if (resp == null || !Boolean.TRUE.equals(resp.getSuccess())) {
            log.error("Adapay 退款回调验签失败");
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
        refundCallbackService.refundCallback(callbackData);
        return AdapayCode.NOTIFY_SUCCESS;
    }
}
