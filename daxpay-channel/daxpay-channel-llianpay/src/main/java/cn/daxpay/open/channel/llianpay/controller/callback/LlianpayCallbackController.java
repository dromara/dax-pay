package cn.daxpay.open.channel.llianpay.controller.callback;

import cn.daxpay.open.channel.llianpay.service.callback.LlianpayPayCallbackService;
import cn.daxpay.open.channel.llianpay.service.callback.LlianpayRefundCallbackService;
import cn.daxpay.open.payment.common.context.MerchantContextLoader;
import cn.daxpay.open.platform.core.annotation.IgnoreAuth;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/// # 连连支付回调通知控制器
///
/// 连连异步通知入口(支付/退款, JSON body + signature-data 头签名), 不走 Sa-Token 认证(由 @IgnoreAuth 注解放行)。
/// URL 中的 channelMchNo 用于回调时组装凭证(验签需连连平台公钥)。
/// 应答为连连规约纯文本(Success/Fail)。
@Tag(name = "连连支付回调通知控制器")
@RestController
@RequiredArgsConstructor
@RequestMapping("/unipay/callback/{mchNo}/{channelMchNo}/llianpay")
@IgnoreAuth
public class LlianpayCallbackController {

    private final MerchantContextLoader merchantContextLoader;
    private final LlianpayPayCallbackService llianpayPayCallbackService;
    private final LlianpayRefundCallbackService llianpayRefundCallbackService;

    /// 连连支付回调
    @Operation(summary = "连连支付回调")
    @PostMapping("/pay")
    public String payNotify(@PathVariable("mchNo") String mchNo,
                            @PathVariable("channelMchNo") String channelMchNo,
                            @RequestHeader(value = "signature-data", required = false) String sign,
                            @RequestBody String body) {
        merchantContextLoader.bindMchNoForCallback(mchNo);
        return llianpayPayCallbackService.payHandle(mchNo, channelMchNo, body, sign);
    }

    /// 连连退款回调
    @Operation(summary = "连连退款回调")
    @PostMapping("/refund")
    public String refundNotify(@PathVariable("mchNo") String mchNo,
                               @PathVariable("channelMchNo") String channelMchNo,
                               @RequestHeader(value = "signature-data", required = false) String sign,
                               @RequestBody String body) {
        merchantContextLoader.bindMchNoForCallback(mchNo);
        return llianpayRefundCallbackService.refundHandle(mchNo, channelMchNo, body, sign);
    }
}
