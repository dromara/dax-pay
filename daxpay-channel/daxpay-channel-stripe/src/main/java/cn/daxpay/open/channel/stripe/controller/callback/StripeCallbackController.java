package cn.daxpay.open.channel.stripe.controller.callback;

import cn.daxpay.open.channel.stripe.service.callback.StripePayCallbackService;
import cn.daxpay.open.channel.stripe.service.callback.StripeRefundCallbackService;
import cn.daxpay.open.payment.common.context.MerchantContextLoader;
import cn.daxpay.open.platform.core.annotation.IgnoreAuth;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/// # Stripe 支付回调通知控制器
///
/// Stripe 异步通知入口(支付/退款), 不走 Sa-Token 认证(由 @IgnoreAuth 注解放行)。
/// URL 中的 channelMchNo 用于回调时组装凭证验签(Stripe 回调验签前无法解析订单号)。
@Tag(name = "Stripe 支付回调通知控制器")
@RestController
@RequestMapping("/unipay/callback/{mchNo}/{channelMchNo}/stripe")
@RequiredArgsConstructor
@IgnoreAuth
public class StripeCallbackController {

    private final MerchantContextLoader merchantContextLoader;
    private final StripePayCallbackService payCallbackService;
    private final StripeRefundCallbackService refundCallbackService;

    /// Stripe 支付回调
    @Operation(summary = "Stripe 支付回调")
    @PostMapping("/pay")
    public String payNotify(@PathVariable("mchNo") String mchNo,
                            @PathVariable("channelMchNo") String channelMchNo,
                            HttpServletRequest request) {
        merchantContextLoader.bindMchNoForCallback(mchNo);
        return payCallbackService.payHandle(mchNo, channelMchNo, request);
    }

    /// Stripe 退款回调
    @Operation(summary = "Stripe 退款回调")
    @PostMapping("/refund")
    public String refundNotify(@PathVariable("mchNo") String mchNo,
                               @PathVariable("channelMchNo") String channelMchNo,
                               HttpServletRequest request) {
        merchantContextLoader.bindMchNoForCallback(mchNo);
        return refundCallbackService.refundHandle(mchNo, channelMchNo, request);
    }
}
