package cn.daxpay.open.channel.adapay.controller.callback;

import cn.daxpay.open.channel.adapay.service.callback.AdapayPayCallbackService;
import cn.daxpay.open.channel.adapay.service.callback.AdapayRefundCallbackService;
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

/// # Adapay 支付回调通知控制器
///
/// Adapay 异步通知入口(支付/退款), 不走 Sa-Token 认证(由 @IgnoreAuth 注解放行)。
/// 验签只需全局平台公钥; path 仍带 channelMchNo 统一约定, 凭 order_no 反查 PayTrade。
@Tag(name = "Adapay 支付回调通知控制器")
@RestController
@RequiredArgsConstructor
@RequestMapping("/unipay/callback/{mchNo}/{channelMchNo}/adapay")
@IgnoreAuth
public class AdapayCallbackController {

    private final MerchantContextLoader merchantContextLoader;
    private final AdapayPayCallbackService adapayPayCallbackService;
    private final AdapayRefundCallbackService adapayRefundCallbackService;

    /// Adapay 支付回调
    @Operation(summary = "Adapay 支付回调")
    @PostMapping("/pay")
    public String payNotify(@PathVariable("mchNo") String mchNo,
                            @PathVariable("channelMchNo") String channelMchNo,
                            HttpServletRequest request) {
        merchantContextLoader.bindMchNoForCallback(mchNo);
        return adapayPayCallbackService.payHandle(channelMchNo, request);
    }

    /// Adapay 退款回调
    @Operation(summary = "Adapay 退款回调")
    @PostMapping("/refund")
    public String refundNotify(@PathVariable("mchNo") String mchNo,
                               @PathVariable("channelMchNo") String channelMchNo,
                               HttpServletRequest request) {
        merchantContextLoader.bindMchNoForCallback(mchNo);
        return adapayRefundCallbackService.refundHandle(channelMchNo, request);
    }
}
