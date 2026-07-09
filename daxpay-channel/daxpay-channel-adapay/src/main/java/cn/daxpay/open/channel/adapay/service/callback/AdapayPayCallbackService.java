package cn.daxpay.open.channel.adapay.service.callback;

import cn.daxpay.open.channel.adapay.client.AdapayChannelClient;
import cn.daxpay.open.channel.adapay.client.credential.AdapaySdkCredential;
import cn.daxpay.open.channel.adapay.client.req.AdapayCallbackParseReq;
import cn.daxpay.open.channel.adapay.client.resp.AdapayCallbackParseResp;
import cn.daxpay.open.channel.adapay.code.AdapayCode;
import cn.daxpay.open.payment.common.callback.CallbackData;
import cn.daxpay.open.payment.core.trade.service.PayCallbackService;
import cn.daxpay.open.platform.core.enums.pay.notice.CallbackStatusEnum;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.JakartaServletUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

/// # Adapay 支付回调处理服务
///
/// Adapay 异步通知 → 主应用接收原始 body → 转发子应用验签与解析(平台公钥 SHA1withRSA) →
/// 构建 [CallbackData] 交由 [PayCallbackService] 更新订单状态。
///
/// 验签只需全局平台公钥(子应用内置默认), 不需 channelMchNo, 凭 order_no 反查 PayTrade。
/// 主应用零加密代码, 验签/解析集中在子应用 dax-pay-channel-two。
@Slf4j
@Service
@RequiredArgsConstructor
public class AdapayPayCallbackService {

    private final AdapayChannelClient adapayChannelClient;
    private final PayCallbackService payCallbackService;

    /// 支付回调处理
    public String payHandle(HttpServletRequest request) {
        // 1. 提取回调原始 body
        String body = JakartaServletUtil.getBody(request);
        if (StrUtil.isBlank(body)) {
            log.error("Adapay 支付回调: body 为空");
            return AdapayCode.NOTIFY_FAIL;
        }

        // 2. 转发子应用验签解析(publicKey 为空, 子应用用全局默认平台公钥)
        AdapayCallbackParseResp resp = parse(body, false);
        if (resp == null || !Boolean.TRUE.equals(resp.getSuccess())) {
            log.error("Adapay 支付回调验签失败");
            return AdapayCode.NOTIFY_FAIL;
        }

        // 3. 构建 CallbackData 交框架更新订单状态
        CallbackData callbackData = new CallbackData();
        // order_no = 下单时传入的平台 tradeNo
        callbackData.setTradeNo(resp.getOutTradeNo());
        // id = Adapay 支付对象 ID
        callbackData.setOutTradeNo(resp.getTradeNo());
        if (Objects.equals(resp.getTradeStatus(), "SUCCESS")) {
            callbackData.setTradeStatus(CallbackStatusEnum.SUCCESS.getCode());
        } else {
            callbackData.setCallbackErrorMsg("Adapay 回调状态非成功: " + resp.getTradeStatus());
        }
        callbackData.setFinishTime(resp.getFinishTime());
        try {
            payCallbackService.payCallback(callbackData);
        } catch (Exception e) {
            log.error("Adapay 支付回调业务处理失败: tradeNo={}", callbackData.getTradeNo(), e);
            return AdapayCode.NOTIFY_FAIL;
        }
        return AdapayCode.NOTIFY_SUCCESS;
    }

    /// 转发子应用验签解析
    private AdapayCallbackParseResp parse(String body, boolean refund) {
        AdapaySdkCredential credential = new AdapaySdkCredential();
        AdapayCallbackParseReq req = new AdapayCallbackParseReq();
        req.setCredential(credential);
        req.setBody(body);
        var result = refund
                ? adapayChannelClient.parseRefundCallback(req)
                : adapayChannelClient.parsePayCallback(req);
        if (result.getCode() != 0) {
            log.error("Adapay 回调: 子应用解析失败: {}", result.getMsg());
            return null;
        }
        return result.getData();
    }
}
