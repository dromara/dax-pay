package cn.daxpay.open.channel.yeepay.controller.callback;

import cn.daxpay.open.channel.yeepay.service.callback.YeepayPayCallbackService;
import cn.daxpay.open.channel.yeepay.service.callback.YeepayRefundCallbackService;
import cn.daxpay.open.payment.common.context.MerchantContextLoader;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/// # 易宝支付回调通知控制器
///
/// 易宝异步通知入口(支付/退款), 不走 Sa-Token 认证(由安全配置放行 `/unipay/callback/**`)。
/// URL 中的 channelMchNo 用于回调时组装凭证(RSA2048 数字信封解密需 appKey/私钥)。
@Tag(name = "易宝支付回调通知控制器")
@RestController
@RequiredArgsConstructor
@RequestMapping("/unipay/callback/{mchNo}/{channelMchNo}/yeepay")
public class YeepayCallbackController {

    private final MerchantContextLoader merchantContextLoader;
    private final YeepayPayCallbackService yeepayPayCallbackService;
    private final YeepayRefundCallbackService yeepayRefundCallbackService;

    /// 易宝支付回调
    @Operation(summary = "易宝支付回调")
    @PostMapping("/pay")
    public String payNotify(@PathVariable("mchNo") String mchNo,
                            @PathVariable("channelMchNo") String channelMchNo,
                            HttpServletRequest request) {
        merchantContextLoader.bindMchNoForCallback(mchNo);
        return yeepayPayCallbackService.payHandle(mchNo, channelMchNo, request);
    }

    /// 易宝退款回调
    @Operation(summary = "易宝退款回调")
    @PostMapping("/refund")
    public String refundNotify(@PathVariable("mchNo") String mchNo,
                               @PathVariable("channelMchNo") String channelMchNo,
                               HttpServletRequest request) {
        merchantContextLoader.bindMchNoForCallback(mchNo);
        return yeepayRefundCallbackService.refundHandle(mchNo, channelMchNo, request);
    }
}
