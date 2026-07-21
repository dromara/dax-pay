package cn.daxpay.open.channel.hkrt.controller.callback;

import cn.daxpay.open.channel.hkrt.service.callback.HkrtPayCallbackService;
import cn.daxpay.open.channel.hkrt.service.callback.HkrtRefundCallbackService;
import cn.daxpay.open.payment.common.context.MerchantContextLoader;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/// # 海科融通支付回调通知控制器
///
/// 海科融通异步通知入口(支付/退款), 不走 Sa-Token 认证(由安全配置放行 `/unipay/callback/**`)。
/// 验签只需全局服务商 accessKey; path 仍带 channelMchNo 统一约定, 凭 out_trade_no 反查 PayTrade。
@Tag(name = "海科融通支付回调通知控制器")
@RestController
@RequestMapping("/unipay/callback/{mchNo}/{channelMchNo}/hkrt")
@RequiredArgsConstructor
public class HkrtCallbackController {

    private final MerchantContextLoader merchantContextLoader;
    private final HkrtPayCallbackService hkrtPayCallbackService;
    private final HkrtRefundCallbackService hkrtRefundCallbackService;

    /// 海科融通支付回调
    @Operation(summary = "海科融通支付回调")
    @PostMapping("/pay")
    public String payNotify(@PathVariable("mchNo") String mchNo,
                            @PathVariable("channelMchNo") String channelMchNo,
                            HttpServletRequest request) {
        merchantContextLoader.bindMchNoForCallback(mchNo);
        return hkrtPayCallbackService.payHandle(channelMchNo, request);
    }

    /// 海科融通退款回调
    @Operation(summary = "海科融通退款回调")
    @PostMapping("/refund")
    public String refundNotify(@PathVariable("mchNo") String mchNo,
                               @PathVariable("channelMchNo") String channelMchNo,
                               HttpServletRequest request) {
        merchantContextLoader.bindMchNoForCallback(mchNo);
        return hkrtRefundCallbackService.refundHandle(channelMchNo, request);
    }
}
