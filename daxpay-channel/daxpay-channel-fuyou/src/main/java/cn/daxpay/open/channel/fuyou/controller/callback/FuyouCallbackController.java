package cn.daxpay.open.channel.fuyou.controller.callback;

import cn.daxpay.open.channel.fuyou.service.callback.FuyouDirectPayCallbackService;
import cn.daxpay.open.channel.fuyou.service.callback.FuyouPayCallbackService;
import cn.daxpay.open.channel.fuyou.service.callback.FuyouRefundCallbackService;
import cn.daxpay.open.payment.common.context.MerchantContextLoader;
import cn.daxpay.open.platform.core.annotation.IgnoreAuth;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/// # 富友支付回调通知控制器
///
/// 富友异步通知入口(支付), 不走 Sa-Token 认证(由 @IgnoreAuth 注解放行)。
/// 服务商模式回调为 form-urlencoded 的 `req` 参数(URL编码的XML), 验签只需全局服务商公钥;
/// 直连模式回调为 JSON 报文体, 验签按通道商户取商户密钥;
/// 两者均凭 mchnt_order_no(关联订单号) 反查 PayTrade。
@Tag(name = "富友支付回调通知控制器")
@RestController
@RequestMapping("/unipay/callback/{mchNo}/{channelMchNo}/fuyou")
@RequiredArgsConstructor
@IgnoreAuth
public class FuyouCallbackController {

    private final MerchantContextLoader merchantContextLoader;
    private final FuyouPayCallbackService fuyouPayCallbackService;
    private final FuyouRefundCallbackService fuyouRefundCallbackService;
    private final FuyouDirectPayCallbackService fuyouDirectPayCallbackService;

    /// 富友支付回调
    ///
    /// 响应格式: 返回 "1" 表示已收到, 其他视为失败(富友会重试)
    @Operation(summary = "富友支付回调")
    @PostMapping("/pay")
    public String payNotify(@PathVariable("mchNo") String mchNo,
                            @PathVariable("channelMchNo") String channelMchNo,
                            @RequestParam("req") String req) {
        merchantContextLoader.bindMchNoForCallback(mchNo);
        return fuyouPayCallbackService.payHandle(channelMchNo, req);
    }

    /// 富友退款回调
    @Operation(summary = "富友退款回调")
    @PostMapping("/refund")
    public String refundNotify(@PathVariable("mchNo") String mchNo,
                               @PathVariable("channelMchNo") String channelMchNo,
                               @RequestParam("req") String req) {
        merchantContextLoader.bindMchNoForCallback(mchNo);
        return fuyouRefundCallbackService.refundHandle(channelMchNo, req);
    }

    /// 富友直连支付回调
    ///
    /// 直连回调报文为 JSON(UTF-8), 原始报文体直接透传子应用验签解析(直连无退款结果通知)
    @Operation(summary = "富友直连支付回调")
    @PostMapping("/direct/pay")
    public String directPayNotify(@PathVariable("mchNo") String mchNo,
                                  @PathVariable("channelMchNo") String channelMchNo,
                                  @RequestBody String body) {
        merchantContextLoader.bindMchNoForCallback(mchNo);
        return fuyouDirectPayCallbackService.payHandle(channelMchNo, body);
    }
}
