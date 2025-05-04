package org.dromara.daxpay.channel.wechat.controller.payment;

import cn.bootx.platform.core.annotation.IgnoreAuth;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.dromara.daxpay.channel.wechat.service.payment.callback.WechatPayCallbackService;
import org.dromara.daxpay.channel.wechat.service.payment.callback.WechatRefundCallbackService;
import org.dromara.daxpay.channel.wechat.service.payment.callback.WechatTransferCallbackService;
import org.dromara.daxpay.service.service.assist.PaymentAssistService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author xxm
 * @since 2024/7/29
 */

@Validated
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

    @Operation(summary = "微信支付回调(普通商户)")
    @PostMapping("/pay")
    public String wechatPayNotify(@PathVariable("AppId") String appId,HttpServletRequest request) {
        paymentAssistService.initMchAndApp(appId);
        return payCallbackService.payHandle(request,false);
    }

    @Operation(summary = "微信支付回调(特约商户)")
    @PostMapping("/isv/pay")
    public String wechatPayIsvNotify(@PathVariable("AppId") String appId,HttpServletRequest request) {
        paymentAssistService.initMchAndApp(appId);
        return payCallbackService.payHandle(request,true);
    }

    @Operation(summary = "微信退款回调(普通商户)")
    @PostMapping("/refund")
    public String wechatRefundNotify(@PathVariable("AppId") String appId,HttpServletRequest request) {
        paymentAssistService.initMchAndApp(appId);
        return refundCallbackService.refundHandle(request,false);
    }

    @Operation(summary = "微信退款回调(特约商户)")
    @PostMapping("/isv/refund")
    public String wechatIsvRefundNotify(@PathVariable("AppId") String appId,HttpServletRequest request) {
        paymentAssistService.initMchAndApp(appId);
        return refundCallbackService.refundHandle(request,true);
    }


    @Operation(summary = "微信转账回调(普通商户)")
    @PostMapping("/transfer")
    public String wechatTransferNotify(@PathVariable("AppId") String appId, HttpServletRequest request) {
        paymentAssistService.initMchAndApp(appId);
        return transferCallbackService.transferHandle(request,false);
    }

    @Operation(summary = "微信转账回调(特约商户)")
    @PostMapping("/isv/transfer")
    public String wechatIsvTransferNotify(@PathVariable("AppId") String appId, HttpServletRequest request) {
        paymentAssistService.initMchAndApp(appId);
        return transferCallbackService.transferHandle(request,true);
    }
}
