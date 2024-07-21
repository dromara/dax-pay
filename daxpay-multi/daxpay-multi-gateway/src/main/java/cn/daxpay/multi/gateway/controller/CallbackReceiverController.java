package cn.daxpay.multi.gateway.controller;

import cn.daxpay.multi.core.enums.ChannelEnum;
import cn.daxpay.multi.service.service.assist.PaymentAssistService;
import cn.daxpay.multi.service.service.notice.callback.CallbackReceiverService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 三方支付回调处理器
 * @author xxm
 * @since 2024/6/4
 */
@Slf4j
@Tag(name = "支付通道信息回调")
@RestController
@RequestMapping("/unipay/callback/{mchNo}/{AppId}")
@AllArgsConstructor
public class CallbackReceiverController {

    private final CallbackReceiverService callbackReceiverService;

    private final PaymentAssistService paymentAssistService;

    @SneakyThrows
    @Operation(summary = "支付宝支付回调")
    @PostMapping("/pay/alipay")
    public String aliPayNotify(@PathVariable("mchNo") String mchNo, @PathVariable("AppId") String appId, HttpServletRequest request) {
        paymentAssistService.initMchAndApp(mchNo, appId);
        return callbackReceiverService.payHandle(request, ChannelEnum.ALI.getCode());
    }

    @SneakyThrows
    @Operation(summary = "微信支付回调")
    @PostMapping("/pay/wechat")
    public String wechatPayNotify(@PathVariable("mchNo") String mchNo, @PathVariable("AppId") String appId,HttpServletRequest request) {
        paymentAssistService.initMchAndApp(mchNo, appId);
        return callbackReceiverService.payHandle(request, ChannelEnum.WECHAT.getCode());
    }

    @SneakyThrows
    @Operation(summary = "云闪付支付回调")
    @PostMapping("/pay/union")
    public String unionPayNotify(@PathVariable("mchNo") String mchNo, @PathVariable("AppId") String appId,HttpServletRequest request) {
        paymentAssistService.initMchAndApp(mchNo, appId);
        return callbackReceiverService.payHandle(request, ChannelEnum.UNION_PAY.getCode());
    }

    @SneakyThrows
    @Operation(summary = "支付宝退款回调")
    @PostMapping("/refund/alipay")
    public String aliRefundNotify(@PathVariable("mchNo") String mchNo, @PathVariable("AppId") String appId, HttpServletRequest request) {
        paymentAssistService.initMchAndApp(mchNo, appId);
        return callbackReceiverService.refundHandle(request, ChannelEnum.ALI.getCode());
    }

    @SneakyThrows
    @Operation(summary = "微信退款回调")
    @PostMapping("/refund/wechat")
    public String wechatRefundNotify(@PathVariable("mchNo") String mchNo, @PathVariable("AppId") String appId,HttpServletRequest request) {
        paymentAssistService.initMchAndApp(mchNo, appId);
        return callbackReceiverService.refundHandle(request, ChannelEnum.WECHAT.getCode());
    }

    @SneakyThrows
    @Operation(summary = "云闪付退款回调")
    @PostMapping("/refund/union")
    public String unionRefundNotify(@PathVariable("mchNo") String mchNo, @PathVariable("AppId") String appId,HttpServletRequest request) {
        paymentAssistService.initMchAndApp(mchNo, appId);
        return callbackReceiverService.refundHandle(request, ChannelEnum.UNION_PAY.getCode());
    }
}
