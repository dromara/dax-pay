package org.dromara.daxpay.channel.alipay.controller;

import cn.bootx.platform.core.annotation.IgnoreAuth;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.dromara.daxpay.channel.alipay.service.callback.AliPayCallbackService;
import org.dromara.daxpay.service.service.assist.PaymentAssistService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 支付宝回调通知
 * TODO 退款和支付回调都是同一个地址进行接收, 退款请求时传入的回调地址不生效,
 * @author xxm
 * @since 2024/9/2
 */
@IgnoreAuth
@Tag(name = "支付宝回调通知")
@RestController
@RequestMapping("/unipay/callback/{AppId}")
@RequiredArgsConstructor
public class AliPayCallbackController {

    private final PaymentAssistService paymentAssistService;
    private final AliPayCallbackService payCallbackService;

    @Operation(summary = "支付宝回调")
    @PostMapping("/alipay")
    public String aliPayNotify( @PathVariable("AppId") String appId, HttpServletRequest request) {
        paymentAssistService.initMchApp(appId);
        return payCallbackService.callbackHandle(request);
    }
}
