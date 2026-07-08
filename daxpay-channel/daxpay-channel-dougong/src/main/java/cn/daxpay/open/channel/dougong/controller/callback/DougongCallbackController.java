package cn.daxpay.open.channel.dougong.controller.callback;

import cn.daxpay.open.channel.dougong.service.callback.DougongPayCallbackService;
import cn.daxpay.open.channel.dougong.service.callback.DougongRefundCallbackService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/// # 斗拱支付回调通知控制器
///
/// 汇付异步通知入口(支付/退款), 不走 Sa-Token 认证(由安全配置放行 `/unipay/callback/**`)。
/// 验签转发子应用(汇付 RsaUtils 在子应用侧), 凭 req_seq_id 反查 PayTrade/PayRefundOrder。
@Tag(name = "斗拱支付回调通知控制器")
@RestController
@RequestMapping("/unipay/callback/{mchNo}/{appId}/dougong")
@RequiredArgsConstructor
public class DougongCallbackController {

    private final DougongPayCallbackService dougongPayCallbackService;
    private final DougongRefundCallbackService dougongRefundCallbackService;

    /// 斗拱支付回调
    @Operation(summary = "斗拱支付回调")
    @PostMapping("/pay")
    public String payNotify(@PathVariable("mchNo") String mchNo,
                            @PathVariable("appId") String appId,
                            HttpServletRequest request) {
        return dougongPayCallbackService.payHandle(request);
    }

    /// 斗拱退款回调
    @Operation(summary = "斗拱退款回调")
    @PostMapping("/refund")
    public String refundNotify(@PathVariable("mchNo") String mchNo,
                               @PathVariable("appId") String appId,
                               HttpServletRequest request) {
        return dougongRefundCallbackService.refundHandle(request);
    }
}
