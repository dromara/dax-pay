package org.dromara.daxpay.channel.union.controller;

import cn.bootx.platform.core.annotation.IgnoreAuth;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.dromara.daxpay.service.service.assist.PaymentAssistService;
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
@RequestMapping("/unipay/callback/{AppId}")
@RequiredArgsConstructor
public class UnionPayCallbackController {

    private final PaymentAssistService paymentAssistService;

//    @Operation(summary = "云闪付支付回调")
//    @PostMapping("/pay")
//    public String wechatPayNotify(@PathVariable("AppId") String appId, HttpServletRequest request) {
//        paymentAssistService.initMchApp(appId);
//        return payCallbackService.payHandle(request);
//    }
//
//
//    @Operation(summary = "云闪付退款回调")
//    @PostMapping("/refund")
//    public String wechatRefundNotify(@PathVariable("AppId") String appId,HttpServletRequest request) {
//        paymentAssistService.initMchApp(appId);
//        return refundCallbackService.refundHandle(request);
//    }
}
