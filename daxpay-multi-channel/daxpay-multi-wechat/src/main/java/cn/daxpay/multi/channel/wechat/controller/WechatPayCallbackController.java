package cn.daxpay.multi.channel.wechat.controller;

import cn.bootx.platform.core.annotation.IgnoreAuth;
import cn.daxpay.multi.channel.wechat.service.assist.WechatAuthService;
import cn.daxpay.multi.channel.wechat.service.callback.WechatPayCallbackService;
import cn.daxpay.multi.channel.wechat.service.callback.WechatRefundCallbackService;
import cn.daxpay.multi.channel.wechat.service.callback.WechatTransferCallbackService;
import cn.daxpay.multi.service.service.assist.PaymentAssistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author xxm
 * @since 2024/7/29
 */
@IgnoreAuth
@Tag(name = "微信回调通知控制器")
@RestController
@RequestMapping("/unipay/callback/{mchNo}/{AppId}/wechat")
@RequiredArgsConstructor
public class WechatPayCallbackController {
    private final WechatTransferCallbackService transferCallbackService;
    private final PaymentAssistService paymentAssistService;
    private final WechatPayCallbackService payCallbackService;
    private final WechatRefundCallbackService refundCallbackService;
    private final WechatAuthService wechatAuthService;

    @Operation(summary = "微信转账回调")
    @PostMapping("/transfer")
    public String transferHandle(@PathVariable("mchNo") String mchNo, @PathVariable("AppId") String appId, HttpServletRequest request) {
        paymentAssistService.initMchAndApp(mchNo, appId);
        return transferCallbackService.transferHandle(request);
    }

    @Operation(summary = "微信支付回调")
    @PostMapping("/pay")
    public String wechatPayNotify(@PathVariable("mchNo") String mchNo, @PathVariable("AppId") String appId,HttpServletRequest request) {
        paymentAssistService.initMchAndApp(mchNo, appId);
        return payCallbackService.payHandle(request);
    }


    @Operation(summary = "微信退款回调")
    @PostMapping("/refund")
    public String wechatRefundNotify(@PathVariable("mchNo") String mchNo, @PathVariable("AppId") String appId,HttpServletRequest request) {
        paymentAssistService.initMchAndApp(mchNo, appId);
        return refundCallbackService.refundHandle(request);
    }


    @Operation(summary = "微信认证授权回调")
    @GetMapping("/auth/{code}")
    public ModelAndView wechatCallback(@PathVariable("mchNo") String mchNo, @PathVariable("AppId") String appId, String authCode, @PathVariable("code") String code){
        paymentAssistService.initMchAndApp(mchNo, appId);
        wechatAuthService.authCallback(authCode, code);
        return new ModelAndView("forward:/h5/openIdCallbackClose.html");
    }
}
