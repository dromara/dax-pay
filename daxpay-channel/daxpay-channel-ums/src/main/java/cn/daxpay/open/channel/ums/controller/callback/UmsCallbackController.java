package cn.daxpay.open.channel.ums.controller.callback;

import cn.daxpay.open.channel.ums.service.callback.UmsPayCallbackService;
import cn.daxpay.open.channel.ums.service.callback.UmsRefundCallbackService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/// # 银联商务支付回调通知控制器
///
/// 银联商务异步通知入口(支付/退款), 不走 Sa-Token 认证(由安全配置放行 `/unipay/callback/**`)。
/// URL 中的 channelMchNo 用于回调时组装凭证验签。
@Tag(name = "银联商务支付回调通知控制器")
@RestController
@RequiredArgsConstructor
@RequestMapping("/unipay/callback/{mchNo}/{appId}/ums/{channelMchNo}")
public class UmsCallbackController {

    private final UmsPayCallbackService umsPayCallbackService;
    private final UmsRefundCallbackService umsRefundCallbackService;

    /// 银联商务支付回调
    @Operation(summary = "银联商务支付回调")
    @PostMapping("/pay")
    public String payNotify(@PathVariable("mchNo") String mchNo,
                            @PathVariable("appId") String appId,
                            @PathVariable("channelMchNo") String channelMchNo,
                            HttpServletRequest request) {
        return umsPayCallbackService.payHandle(mchNo, channelMchNo, request);
    }

    /// 银联商务退款回调
    @Operation(summary = "银联商务退款回调")
    @PostMapping("/refund")
    public String refundNotify(@PathVariable("mchNo") String mchNo,
                               @PathVariable("appId") String appId,
                               @PathVariable("channelMchNo") String channelMchNo,
                               HttpServletRequest request) {
        return umsRefundCallbackService.refundHandle(mchNo, channelMchNo, request);
    }
}
