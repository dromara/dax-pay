package cn.daxpay.open.channel.adapay.controller.callback;

import cn.daxpay.open.channel.adapay.service.callback.AdapayPayCallbackService;
import cn.daxpay.open.channel.adapay.service.callback.AdapayRefundCallbackService;
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
/// Adapay 异步通知入口(支付/退款), 不走 Sa-Token 认证(由安全配置放行 `/unipay/callback/**`)。
/// Adapay 回调验签只需全局平台公钥, 不需 channelMchNo, 路径不带 channelMchNo, 凭 order_no 反查 PayTrade。
@Tag(name = "Adapay 支付回调通知控制器")
@RestController
@RequiredArgsConstructor
@RequestMapping("/unipay/callback/{mchNo}/{appId}/adapay")
public class AdapayCallbackController {

    private final AdapayPayCallbackService adapayPayCallbackService;
    private final AdapayRefundCallbackService adapayRefundCallbackService;

    /// Adapay 支付回调
    @Operation(summary = "Adapay 支付回调")
    @PostMapping("/pay")
    public String payNotify(@PathVariable("mchNo") String mchNo,
                            @PathVariable("appId") String appId,
                            HttpServletRequest request) {
        return adapayPayCallbackService.payHandle(request);
    }

    /// Adapay 退款回调
    @Operation(summary = "Adapay 退款回调")
    @PostMapping("/refund")
    public String refundNotify(@PathVariable("mchNo") String mchNo,
                               @PathVariable("appId") String appId,
                               HttpServletRequest request) {
        return adapayRefundCallbackService.refundHandle(request);
    }
}
