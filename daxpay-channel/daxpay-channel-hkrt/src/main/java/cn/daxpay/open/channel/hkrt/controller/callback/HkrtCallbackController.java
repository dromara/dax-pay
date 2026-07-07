package cn.daxpay.open.channel.hkrt.controller.callback;

import cn.daxpay.open.channel.hkrt.service.callback.HkrtPayCallbackService;
import cn.daxpay.open.channel.hkrt.service.callback.HkrtRefundCallbackService;
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
/// 海科融通回调验签只需全局服务商 accessKey(从 HkrtIsvKeyConfig 读取), 不需 channelMchNo,
/// 因此路径不带 channelMchNo, 凭 out_trade_no 反查 PayTrade。
@Tag(name = "海科融通支付回调通知控制器")
@RestController
@RequestMapping("/unipay/callback/{mchNo}/{appId}/hkrt")
@RequiredArgsConstructor
public class HkrtCallbackController {

    private final HkrtPayCallbackService hkrtPayCallbackService;
    private final HkrtRefundCallbackService hkrtRefundCallbackService;

    /// 海科融通支付回调
    @Operation(summary = "海科融通支付回调")
    @PostMapping("/pay")
    public String payNotify(@PathVariable("mchNo") String mchNo,
                            @PathVariable("appId") String appId,
                            HttpServletRequest request) {
        return hkrtPayCallbackService.payHandle(request);
    }

    /// 海科融通退款回调
    @Operation(summary = "海科融通退款回调")
    @PostMapping("/refund")
    public String refundNotify(@PathVariable("mchNo") String mchNo,
                               @PathVariable("appId") String appId,
                               HttpServletRequest request) {
        return hkrtRefundCallbackService.refundHandle(request);
    }
}
