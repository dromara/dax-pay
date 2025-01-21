package org.dromara.daxpay.channel.union.controller;

import cn.bootx.platform.core.annotation.IgnoreAuth;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.dromara.daxpay.channel.union.service.callback.UnionPayCallbackService;
import org.dromara.daxpay.channel.union.service.callback.UnionRefundCallbackService;
import org.dromara.daxpay.service.service.assist.PaymentAssistService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 银联回调通知
 * @author xxm
 * @since 2024/9/6
 */
@IgnoreAuth
@Tag(name = "银联回调通知")
@RestController
@RequestMapping("/unipay/callback/{AppId}/union")
@RequiredArgsConstructor
public class UnionPayCallbackController {

    private final UnionPayCallbackService unionPayCallbackService;

    private final PaymentAssistService paymentAssistService;
    private final UnionRefundCallbackService refundCallbackService;

    @Operation(summary = "云闪付支付回调")
    @PostMapping("/pay")
    public String wechatPayNotify(@PathVariable("AppId") String appId, HttpServletRequest request) {
        paymentAssistService.initMchApp(appId);
        return unionPayCallbackService.payHandle(request);
    }


    @Operation(summary = "云闪付退款回调")
    @PostMapping("/refund")
    public String wechatRefundNotify(@PathVariable("AppId") String appId, HttpServletRequest request) {
        paymentAssistService.initMchApp(appId);
        return refundCallbackService.refundHandle(request);
    }
}
