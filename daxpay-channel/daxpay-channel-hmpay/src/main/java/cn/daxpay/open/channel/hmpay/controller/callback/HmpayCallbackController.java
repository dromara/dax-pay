package cn.daxpay.open.channel.hmpay.controller.callback;

import cn.daxpay.open.channel.hmpay.service.callback.HmpayPayCallbackService;
import cn.daxpay.open.channel.hmpay.service.callback.HmpayRefundCallbackService;
import cn.daxpay.open.payment.common.context.MerchantContextLoader;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/// # 河马付支付回调通知控制器
///
/// 杉德异步通知入口(支付/退款), 不走 Sa-Token 认证(由安全配置放行 `/unipay/callback/**`)。
/// 验签转发子应用(杉德 RSA 验签在子应用侧), 凭 out_order_no 反查 PayTrade/PayRefundOrder。
@Tag(name = "河马付支付回调通知控制器")
@RestController
@RequestMapping("/unipay/callback/{mchNo}/{channelMchNo}/hmpay")
@RequiredArgsConstructor
public class HmpayCallbackController {

    private final MerchantContextLoader merchantContextLoader;
    private final HmpayPayCallbackService hmpayPayCallbackService;
    private final HmpayRefundCallbackService hmpayRefundCallbackService;

    /// 河马付支付回调
    @Operation(summary = "河马付支付回调")
    @PostMapping("/pay")
    public String payNotify(@PathVariable("mchNo") String mchNo,
                            @PathVariable("channelMchNo") String channelMchNo,
                            HttpServletRequest request) {
        merchantContextLoader.bindMchNoForCallback(mchNo);
        return hmpayPayCallbackService.payHandle(request);
    }

    /// 河马付退款回调
    @Operation(summary = "河马付退款回调")
    @PostMapping("/refund")
    public String refundNotify(@PathVariable("mchNo") String mchNo,
                               @PathVariable("channelMchNo") String channelMchNo,
                               HttpServletRequest request) {
        merchantContextLoader.bindMchNoForCallback(mchNo);
        return hmpayRefundCallbackService.refundHandle(request);
    }
}
