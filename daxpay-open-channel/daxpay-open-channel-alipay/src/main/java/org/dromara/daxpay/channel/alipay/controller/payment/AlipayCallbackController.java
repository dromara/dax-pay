package org.dromara.daxpay.channel.alipay.controller.payment;

import cn.bootx.platform.core.annotation.IgnoreAuth;
import org.dromara.daxpay.channel.alipay.service.payment.callback.AlipayCallbackService;
import org.dromara.daxpay.service.service.assist.PaymentAssistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
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
@Validated
@IgnoreAuth
@Tag(name = "支付宝回调通知")
@RestController
@RequestMapping("/unipay/callback/{AppId}")
@RequiredArgsConstructor
public class AlipayCallbackController {

    private final PaymentAssistService paymentAssistService;
    private final AlipayCallbackService payCallbackService;

    @Operation(summary = "支付宝回调(普通商户)")
    @PostMapping("/alipay")
    public String aliPayNotify(@PathVariable("AppId") String appId, HttpServletRequest request) {
        paymentAssistService.initMchAndApp(appId);
        return payCallbackService.callbackHandle(request,false);
    }
    @Operation(summary = "支付宝回调(特约商户)")
    @PostMapping("/alipay/isv")
    public String aliPayIsvNotify(@PathVariable("AppId") String appId, HttpServletRequest request) {
        paymentAssistService.initMchAndApp(appId);
        return payCallbackService.callbackHandle(request, true);
    }
}
