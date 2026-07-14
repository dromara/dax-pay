package cn.daxpay.open.channel.douyin.controller.callback;

import cn.daxpay.open.channel.douyin.service.callback.DouyinPayCallbackService;
import cn.daxpay.open.channel.douyin.service.callback.DouyinRefundCallbackService;
import cn.daxpay.open.payment.common.context.MerchantContextLoader;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/// # 抖音支付回调通知控制器
///
/// 抖音异步通知入口(支付/退款), 不走 Sa-Token 认证(由安全配置放行 `/unipay/callback/**`)。
/// URL 中的 channelMchNo 用于回调时组装凭证验签(抖音 body 加密, 验签前无法解析)。
@Tag(name = "抖音支付回调通知控制器")
@RestController
@RequestMapping("/unipay/callback/{mchNo}/{channelMchNo}/douyin")
@RequiredArgsConstructor
public class DouyinCallbackController {

    private final MerchantContextLoader merchantContextLoader;
    private final DouyinPayCallbackService payCallbackService;
    private final DouyinRefundCallbackService refundCallbackService;

    /// 抖音支付回调
    @Operation(summary = "抖音支付回调")
    @PostMapping("/pay")
    public String payNotify(@PathVariable("mchNo") String mchNo,
                            @PathVariable("channelMchNo") String channelMchNo,
                            HttpServletRequest request) {
        merchantContextLoader.bindMchNoForCallback(mchNo);
        return payCallbackService.payHandle(mchNo, channelMchNo, request);
    }

    /// 抖音退款回调
    @Operation(summary = "抖音退款回调")
    @PostMapping("/refund")
    public String refundNotify(@PathVariable("mchNo") String mchNo,
                               @PathVariable("channelMchNo") String channelMchNo,
                               HttpServletRequest request) {
        merchantContextLoader.bindMchNoForCallback(mchNo);
        return refundCallbackService.refundHandle(mchNo, channelMchNo, request);
    }
}
