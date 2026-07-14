package cn.daxpay.open.channel.wechat.controller.callback;

import cn.daxpay.open.channel.wechat.service.callback.WechatPayCallbackService;
import cn.daxpay.open.channel.wechat.service.callback.WechatRefundCallbackService;
import cn.daxpay.open.payment.common.context.MerchantContextLoader;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/// # 微信支付回调通知控制器
///
/// 微信异步通知入口(支付/退款), 不走 Sa-Token 认证(由安全配置放行 `/unipay/callback/**`)。
/// URL 中的 channelMchNo 用于回调时组装凭证验签(微信 body 加密, 验签前无法解析)。
@Tag(name = "微信支付回调通知控制器")
@RestController
@RequiredArgsConstructor
@RequestMapping("/unipay/callback/{mchNo}/{channelMchNo}/wechat")
public class WechatCallbackController {

    private final MerchantContextLoader merchantContextLoader;
    private final WechatPayCallbackService wechatPayCallbackService;
    private final WechatRefundCallbackService wechatRefundCallbackService;

    /// 微信支付回调(直连)
    @Operation(summary = "微信支付回调")
    @PostMapping("/pay")
    public String payNotify(@PathVariable("mchNo") String mchNo,
                            @PathVariable("channelMchNo") String channelMchNo,
                            HttpServletRequest request) {
        merchantContextLoader.bindMchNoForCallback(mchNo);
        return wechatPayCallbackService.payHandle(mchNo, channelMchNo, request);
    }

    /// 微信退款回调(直连)
    @Operation(summary = "微信退款回调")
    @PostMapping("/refund")
    public String refundNotify(@PathVariable("mchNo") String mchNo,
                               @PathVariable("channelMchNo") String channelMchNo,
                               HttpServletRequest request) {
        merchantContextLoader.bindMchNoForCallback(mchNo);
        return wechatRefundCallbackService.refundHandle(mchNo, channelMchNo, request);
    }

    /// 微信支付回调(服务商)
    @Operation(summary = "微信服务商支付回调")
    @PostMapping("/isv/pay")
    public String isvPayNotify(@PathVariable("mchNo") String mchNo,
                               @PathVariable("channelMchNo") String channelMchNo,
                               HttpServletRequest request) {
        merchantContextLoader.bindMchNoForCallback(mchNo);
        return wechatPayCallbackService.isvPayHandle(mchNo, channelMchNo, request);
    }

    /// 微信退款回调(服务商)
    @Operation(summary = "微信服务商退款回调")
    @PostMapping("/isv/refund")
    public String isvRefundNotify(@PathVariable("mchNo") String mchNo,
                                  @PathVariable("channelMchNo") String channelMchNo,
                                  HttpServletRequest request) {
        merchantContextLoader.bindMchNoForCallback(mchNo);
        return wechatRefundCallbackService.isvRefundHandle(mchNo, channelMchNo, request);
    }
}
