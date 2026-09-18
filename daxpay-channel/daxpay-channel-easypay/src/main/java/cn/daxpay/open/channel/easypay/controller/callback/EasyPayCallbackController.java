package cn.daxpay.open.channel.easypay.controller.callback;

import cn.daxpay.open.channel.easypay.service.callback.EasyPayPayCallbackService;
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

/// # 易支付支付回调通知控制器
///
/// 易支付异步通知入口(支付回调, form 参数报文), 不走 Sa-Token 认证(由 @IgnoreAuth 注解放行)。
/// 验签公钥按通道商户号从 easy_pay_key_config 读取; path 带 channelMchNo 定位密钥,
/// 凭 out_trade_no 反查 PayTrade。易支付无退款异步通知(退款为同步接口), 仅有支付回调端点。
@Tag(name = "易支付支付回调通知控制器")
@RestController
@RequestMapping("/unipay/callback/{mchNo}/{channelMchNo}/easy_pay")
@RequiredArgsConstructor
@IgnoreAuth
public class EasyPayCallbackController {

    private final MerchantContextLoader merchantContextLoader;

    private final EasyPayPayCallbackService easyPayPayCallbackService;

    /// 易支付支付回调
    @Operation(summary = "易支付支付回调")
    @PostMapping("/pay")
    public String payNotify(@PathVariable("mchNo") String mchNo,
                            @PathVariable("channelMchNo") String channelMchNo,
                            HttpServletRequest request) {
        merchantContextLoader.bindMchNoForCallback(mchNo);
        return easyPayPayCallbackService.payHandle(channelMchNo, request);
    }
}
