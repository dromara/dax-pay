package cn.daxpay.open.channel.wechat.controller.callback;

import cn.daxpay.open.channel.wechat.service.callback.WechatPayCallbackService;
import cn.daxpay.open.channel.wechat.service.callback.WechatRefundCallbackService;
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
@RequestMapping("/unipay/callback/{mchNo}/{appId}/wechat/{channelMchNo}")
public class WechatCallbackController {

    private final WechatPayCallbackService wechatPayCallbackService;
    private final WechatRefundCallbackService wechatRefundCallbackService;

    /// 微信支付回调
    @Operation(summary = "微信支付回调")
    @PostMapping("/pay")
    public String payNotify(@PathVariable("mchNo") String mchNo,
                            @PathVariable("appId") String appId,
                            @PathVariable("channelMchNo") String channelMchNo,
                            HttpServletRequest request) {
        return wechatPayCallbackService.payHandle(mchNo, channelMchNo, request);
    }

    /// 微信退款回调
    @Operation(summary = "微信退款回调")
    @PostMapping("/refund")
    public String refundNotify(@PathVariable("mchNo") String mchNo,
                               @PathVariable("appId") String appId,
                               @PathVariable("channelMchNo") String channelMchNo,
                               HttpServletRequest request) {
        return wechatRefundCallbackService.refundHandle(mchNo, channelMchNo, request);
    }
}
