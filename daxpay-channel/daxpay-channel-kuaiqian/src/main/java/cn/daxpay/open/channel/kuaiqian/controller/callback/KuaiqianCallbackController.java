package cn.daxpay.open.channel.kuaiqian.controller.callback;

import cn.daxpay.open.channel.kuaiqian.service.callback.KuaiqianPayCallbackService;
import cn.daxpay.open.channel.kuaiqian.service.callback.KuaiqianRefundCallbackService;
import cn.daxpay.open.payment.common.context.MerchantContextLoader;
import cn.daxpay.open.platform.core.annotation.IgnoreAuth;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/// # 快钱支付回调通知控制器
///
/// 快钱异步通知入口(支付/退款, JSON + PKCS7 数字信封), 不走 Sa-Token 认证(由 @IgnoreAuth 注解放行)。
/// URL 中的 channelMchNo 用于回调时组装凭证(解密需商户 pfx 证书)。
/// 应答为 A9005 规约加密加签报文(由子应用生成后原样返回)。
@Tag(name = "快钱支付回调通知控制器")
@RestController
@RequiredArgsConstructor
@RequestMapping("/unipay/callback/{mchNo}/{channelMchNo}/kuaiqian")
@IgnoreAuth
public class KuaiqianCallbackController {

    private final MerchantContextLoader merchantContextLoader;
    private final KuaiqianPayCallbackService kuaiqianPayCallbackService;
    private final KuaiqianRefundCallbackService kuaiqianRefundCallbackService;

    /// 快钱支付回调
    @Operation(summary = "快钱支付回调")
    @PostMapping("/pay")
    public String payNotify(@PathVariable("mchNo") String mchNo,
                            @PathVariable("channelMchNo") String channelMchNo,
                            @RequestBody String body) {
        merchantContextLoader.bindMchNoForCallback(mchNo);
        return kuaiqianPayCallbackService.payHandle(mchNo, channelMchNo, body);
    }

    /// 快钱退款回调
    @Operation(summary = "快钱退款回调")
    @PostMapping("/refund")
    public String refundNotify(@PathVariable("mchNo") String mchNo,
                               @PathVariable("channelMchNo") String channelMchNo,
                               @RequestBody String body) {
        merchantContextLoader.bindMchNoForCallback(mchNo);
        return kuaiqianRefundCallbackService.refundHandle(mchNo, channelMchNo, body);
    }
}
