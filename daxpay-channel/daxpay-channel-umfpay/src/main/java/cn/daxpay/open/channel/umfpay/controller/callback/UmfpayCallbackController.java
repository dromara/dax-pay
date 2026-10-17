package cn.daxpay.open.channel.umfpay.controller.callback;

import cn.daxpay.open.channel.umfpay.service.callback.UmfpayPayCallbackService;
import cn.daxpay.open.payment.common.context.MerchantContextLoader;
import cn.daxpay.open.platform.core.annotation.IgnoreAuth;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/// # 联动优势支付回调通知控制器
///
/// 联动优势异步通知入口(**GET query 参数 + RSA 签名**), 不走 Sa-Token 认证(由 @IgnoreAuth 注解放行)。
/// URL 中的 channelMchNo 用于回调时组装凭证(验签需平台公钥)。
/// 应答为 HTML META 页(由子应用生成后原样返回, text/html)。
///
/// 联动优势无退款异步通知(mer_refund 同步终态), 仅提供支付回调端点。
@Tag(name = "联动优势支付回调通知控制器")
@RestController
@RequiredArgsConstructor
@RequestMapping("/unipay/callback/{mchNo}/{channelMchNo}/umfpay")
@IgnoreAuth
public class UmfpayCallbackController {

    private final MerchantContextLoader merchantContextLoader;
    private final UmfpayPayCallbackService umfpayPayCallbackService;

    /// 联动优势支付回调(GET 参数)
    @Operation(summary = "联动优势支付回调")
    @GetMapping(value = "/pay", produces = MediaType.TEXT_HTML_VALUE)
    public String payNotify(@PathVariable("mchNo") String mchNo,
                            @PathVariable("channelMchNo") String channelMchNo,
                            @RequestParam Map<String, String> params) {
        merchantContextLoader.bindMchNoForCallback(mchNo);
        return umfpayPayCallbackService.payHandle(mchNo, channelMchNo, params);
    }
}
