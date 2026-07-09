package cn.daxpay.open.channel.fuyou.controller.callback;

import cn.daxpay.open.channel.fuyou.service.callback.FuyouPayCallbackService;
import cn.daxpay.open.channel.fuyou.service.callback.FuyouRefundCallbackService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/// # 富友支付回调通知控制器
///
/// 富友异步通知入口(支付), 不走 Sa-Token 认证(由安全配置放行 `/unipay/callback/**`)。
/// 富友回调为 form-urlencoded 的 `req` 参数(URL编码的XML), 验签只需全局服务商公钥,
/// 凭 mchnt_order_no(关联订单号) 反查 PayTrade。
@Tag(name = "富友支付回调通知控制器")
@RestController
@RequestMapping("/unipay/callback/{mchNo}/{appId}/fuyou")
@RequiredArgsConstructor
public class FuyouCallbackController {

    private final FuyouPayCallbackService fuyouPayCallbackService;
    private final FuyouRefundCallbackService fuyouRefundCallbackService;

    /// 富友支付回调
    ///
    /// 响应格式: 返回 "1" 表示已收到, 其他视为失败(富友会重试)
    @Operation(summary = "富友支付回调")
    @PostMapping("/pay")
    public String payNotify(@PathVariable("mchNo") String mchNo,
                            @PathVariable("appId") String appId,
                            @RequestParam("req") String req) {
        return fuyouPayCallbackService.payHandle(req);
    }

    /// 富友退款回调
    @Operation(summary = "富友退款回调")
    @PostMapping("/refund")
    public String refundNotify(@PathVariable("mchNo") String mchNo,
                               @PathVariable("appId") String appId,
                               @RequestParam("req") String req) {
        return fuyouRefundCallbackService.refundHandle(req);
    }
}
