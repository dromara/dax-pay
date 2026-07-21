package cn.daxpay.open.channel.leshua.controller.callback;

import cn.daxpay.open.channel.leshua.service.callback.LeshuaPayCallbackService;
import cn.daxpay.open.channel.leshua.service.callback.LeshuaRefundCallbackService;
import cn.daxpay.open.payment.common.context.MerchantContextLoader;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/// # 乐刷支付回调通知控制器
///
/// 乐刷异步通知入口(支付/退款), 不走 Sa-Token 认证(由安全配置放行 `/unipay/callback/**`)。
/// 验签转发子应用(MD5/SM3 签名工具在子应用侧), 凭 third_order_id 反查 PayTrade。
@Tag(name = "乐刷支付回调通知控制器")
@RestController
@RequestMapping("/unipay/callback/{mchNo}/{channelMchNo}/leshua")
@RequiredArgsConstructor
public class LeshuaCallbackController {

    private final MerchantContextLoader merchantContextLoader;
    private final LeshuaPayCallbackService leshuaPayCallbackService;
    private final LeshuaRefundCallbackService leshuaRefundCallbackService;

    /// 乐刷支付回调
    @Operation(summary = "乐刷支付回调")
    @PostMapping("/pay")
    public String payNotify(@PathVariable("mchNo") String mchNo,
                            @PathVariable("channelMchNo") String channelMchNo,
                            HttpServletRequest request) {
        merchantContextLoader.bindMchNoForCallback(mchNo);
        return leshuaPayCallbackService.payHandle(channelMchNo, request);
    }

    /// 乐刷退款回调
    @Operation(summary = "乐刷退款回调")
    @PostMapping("/refund")
    public String refundNotify(@PathVariable("mchNo") String mchNo,
                               @PathVariable("channelMchNo") String channelMchNo,
                               HttpServletRequest request) {
        merchantContextLoader.bindMchNoForCallback(mchNo);
        return leshuaRefundCallbackService.refundHandle(channelMchNo, request);
    }
}
