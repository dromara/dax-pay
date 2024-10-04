package org.dromara.daxpay.channel.wechat.controller;

import cn.bootx.platform.core.annotation.IgnoreAuth;
import org.dromara.daxpay.channel.wechat.service.assist.WechatAuthService;
import org.dromara.daxpay.channel.wechat.service.callback.WechatPayCallbackService;
import org.dromara.daxpay.channel.wechat.service.callback.WechatRefundCallbackService;
import org.dromara.daxpay.channel.wechat.service.callback.WechatTransferCallbackService;
import org.dromara.daxpay.service.service.assist.PaymentAssistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author xxm
 * @since 2024/7/29
 */
@IgnoreAuth
@Tag(name = "微信回调通知控制器")
@RestController
@RequestMapping("/unipay/callback/{AppId}/wechat")
@RequiredArgsConstructor
public class WechatPayCallbackController {
    private final WechatTransferCallbackService transferCallbackService;
    private final PaymentAssistService paymentAssistService;
    private final WechatPayCallbackService payCallbackService;
    private final WechatRefundCallbackService refundCallbackService;

    @Operation(summary = "微信转账回调")
    @PostMapping("/transfer")
    public String transferHandle(@PathVariable("AppId") String appId, HttpServletRequest request) {
        paymentAssistService.initMchApp(appId);
        return transferCallbackService.transferHandle(request);
    }

    @Operation(summary = "微信支付回调")
    @PostMapping("/pay")
    public String wechatPayNotify(@PathVariable("AppId") String appId,HttpServletRequest request) {
        paymentAssistService.initMchApp(appId);
        return payCallbackService.payHandle(request);
    }


    @Operation(summary = "微信退款回调")
    @PostMapping("/refund")
    public String wechatRefundNotify(@PathVariable("AppId") String appId,HttpServletRequest request) {
        paymentAssistService.initMchApp(appId);
        return refundCallbackService.refundHandle(request);
    }

}
