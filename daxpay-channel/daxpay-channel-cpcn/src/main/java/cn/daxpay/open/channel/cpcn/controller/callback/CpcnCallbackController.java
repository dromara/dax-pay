package cn.daxpay.open.channel.cpcn.controller.callback;

import cn.daxpay.open.channel.cpcn.service.callback.CpcnPayCallbackService;
import cn.daxpay.open.channel.cpcn.service.callback.CpcnRefundCallbackService;
import cn.daxpay.open.payment.common.context.MerchantContextLoader;
import cn.daxpay.open.platform.core.annotation.IgnoreAuth;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/// # 中金支付回调通知控制器
///
/// 中金异步通知入口(支付/退款, form 表单 message + signature, 国密 SM2 签名),
/// 不走 Sa-Token 认证(由 @IgnoreAuth 注解放行)。
/// URL 中的 channelMchNo 用于回调时组装凭证(验签需中金平台证书)。
/// 应答为 Base64 XML(Head.Code=2000 接收成功, 由子应用验签后生成)。
@Tag(name = "中金支付回调通知控制器")
@RestController
@RequiredArgsConstructor
@RequestMapping("/unipay/callback/{mchNo}/{channelMchNo}/cpcn")
@IgnoreAuth
public class CpcnCallbackController {

    private final MerchantContextLoader merchantContextLoader;
    private final CpcnPayCallbackService cpcnPayCallbackService;
    private final CpcnRefundCallbackService cpcnRefundCallbackService;

    /// 中金支付回调
    @Operation(summary = "中金支付回调")
    @PostMapping("/pay")
    public String payNotify(@PathVariable("mchNo") String mchNo,
                            @PathVariable("channelMchNo") String channelMchNo,
                            @RequestParam("message") String message,
                            @RequestParam("signature") String signature) {
        merchantContextLoader.bindMchNoForCallback(mchNo);
        return cpcnPayCallbackService.payHandle(mchNo, channelMchNo, message, signature);
    }

    /// 中金退款回调
    @Operation(summary = "中金退款回调")
    @PostMapping("/refund")
    public String refundNotify(@PathVariable("mchNo") String mchNo,
                               @PathVariable("channelMchNo") String channelMchNo,
                               @RequestParam("message") String message,
                               @RequestParam("signature") String signature) {
        merchantContextLoader.bindMchNoForCallback(mchNo);
        return cpcnRefundCallbackService.refundHandle(mchNo, channelMchNo, message, signature);
    }
}
