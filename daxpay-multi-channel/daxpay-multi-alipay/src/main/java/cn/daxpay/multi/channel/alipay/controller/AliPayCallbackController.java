package cn.daxpay.multi.channel.alipay.controller;

import cn.daxpay.multi.channel.alipay.service.callback.AliPayCallbackService;
import cn.daxpay.multi.channel.alipay.service.callback.AliPayRefundCallbackService;
import cn.daxpay.multi.service.service.assist.PaymentAssistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 支付宝回调通知
 * @author xxm
 * @since 2024/9/2
 */
@Tag(name = "支付宝回调通知")
@RestController
@RequestMapping("/unipay/callback/{mchNo}/{AppId}/alipay")
@RequiredArgsConstructor
public class AliPayCallbackController {

    private final PaymentAssistService paymentAssistService;
    private final AliPayCallbackService payCallbackService;
    private final AliPayRefundCallbackService refundCallbackService;

    @Operation(summary = "支付宝支付回调")
    @PostMapping("/pay")
    public String aliPayNotify(@PathVariable("mchNo") String mchNo, @PathVariable("AppId") String appId, HttpServletRequest request) {
        paymentAssistService.initMchAndApp(mchNo, appId);
        return payCallbackService.payHandle(request);
    }

    @SneakyThrows
    @Operation(summary = "支付宝退款回调")
    @PostMapping("/refund")
    public String aliRefundNotify(@PathVariable("mchNo") String mchNo, @PathVariable("AppId") String appId, HttpServletRequest request) {
        paymentAssistService.initMchAndApp(mchNo, appId);
        return refundCallbackService.refundHandle(request);
    }
}
