package cn.daxpay.open.channel.jeepay.controller.callback;

import cn.daxpay.open.channel.jeepay.service.callback.JeepayPayCallbackService;
import cn.daxpay.open.payment.common.context.MerchantContextLoader;
import cn.daxpay.open.platform.core.annotation.IgnoreAuth;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/// # Jeepay支付回调通知控制器
///
/// Jeepay 异步通知入口(支付回调, form 参数报文), 不走 Sa-Token 认证(由 @IgnoreAuth 注解放行)。
/// 验签密钥按通道商户号从 jee_pay_key_config 读取; path 带 channelMchNo 定位密钥,
/// 凭 mchOrderNo 反查 PayTrade。Jeepay 退款通知不接入(退款查询兜底), 仅有支付回调端点。
@Tag(name = "Jeepay支付回调通知控制器")
@RestController
@RequestMapping("/unipay/callback/{mchNo}/{channelMchNo}/jee_pay")
@RequiredArgsConstructor
@IgnoreAuth
public class JeepayCallbackController {

    private final MerchantContextLoader merchantContextLoader;

    private final JeepayPayCallbackService jeepayPayCallbackService;

    /// Jeepay 支付回调
    @Operation(summary = "Jeepay支付回调")
    @PostMapping("/pay")
    public String payNotify(@PathVariable("mchNo") String mchNo,
                            @PathVariable("channelMchNo") String channelMchNo,
                            HttpServletRequest request) {
        merchantContextLoader.bindMchNoForCallback(mchNo);
        return jeepayPayCallbackService.payHandle(channelMchNo, request);
    }
}
