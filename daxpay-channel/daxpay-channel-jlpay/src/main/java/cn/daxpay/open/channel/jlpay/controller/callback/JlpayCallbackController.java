package cn.daxpay.open.channel.jlpay.controller.callback;

import cn.daxpay.open.channel.jlpay.service.callback.JlpayPayCallbackService;
import cn.daxpay.open.payment.common.context.MerchantContextLoader;
import cn.daxpay.open.platform.core.annotation.IgnoreAuth;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/// # 嘉联支付回调通知控制器
///
/// 嘉联异步通知入口(支付, JSON body + SM2 签名请求头), 不走 Sa-Token 认证(由 @IgnoreAuth 注解放行)。
/// URL 中的 channelMchNo 用于回调时组装凭证(验签需平台公钥)。
/// 应答为嘉联规约 JSON(`{"ret_code":"00000"}`)。
///
/// 嘉联仅有支付异步通知(退款为同步终态, 无退款回调端点)。
/// 验签串中的 apiName 为嘉联发起通知的原始请求 URI(含 query), 须原样透传给子应用验签。
@Tag(name = "嘉联支付回调通知控制器")
@RestController
@RequiredArgsConstructor
@RequestMapping("/unipay/callback/{mchNo}/{channelMchNo}/jlpay")
@IgnoreAuth
public class JlpayCallbackController {

    private final MerchantContextLoader merchantContextLoader;
    private final JlpayPayCallbackService jlpayPayCallbackService;

    /// 嘉联支付回调
    @Operation(summary = "嘉联支付回调")
    @PostMapping("/pay")
    public String payNotify(@PathVariable("mchNo") String mchNo,
                            @PathVariable("channelMchNo") String channelMchNo,
                            @RequestBody String body,
                            HttpServletRequest request) {
        merchantContextLoader.bindMchNoForCallback(mchNo);
        // 验签要素: 原始 URI(含 query) + 签名请求头
        String callbackPath = request.getRequestURI()
                + (request.getQueryString() != null ? "?" + request.getQueryString() : "");
        return jlpayPayCallbackService.payHandle(mchNo, channelMchNo, body, callbackPath,
                request.getHeader("x-jlpay-timestamp"),
                request.getHeader("x-jlpay-nonce"),
                request.getHeader("x-jlpay-sign"));
    }
}
