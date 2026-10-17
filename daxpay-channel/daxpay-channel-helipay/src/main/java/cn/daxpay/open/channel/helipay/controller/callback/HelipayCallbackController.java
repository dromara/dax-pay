package cn.daxpay.open.channel.helipay.controller.callback;

import cn.daxpay.open.channel.helipay.service.callback.HelipayPayCallbackService;
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

/// # 合利宝支付回调通知控制器
///
/// 合利宝异步通知入口(支付回调, form 参数报文), 不走 Sa-Token 认证(由 @IgnoreAuth 注解放行)。
/// 验签密钥按通道商户号从 helipay_key_config 读取; path 带 channelMchNo 定位密钥,
/// 凭 rt2_orderId 反查 PayTrade。合利宝退款为同步结果模式不设退款回调, 仅有支付回调端点。
@Tag(name = "合利宝支付回调通知控制器")
@RestController
@RequestMapping("/unipay/callback/{mchNo}/{channelMchNo}/helipay")
@RequiredArgsConstructor
@IgnoreAuth
public class HelipayCallbackController {

    private final MerchantContextLoader merchantContextLoader;

    private final HelipayPayCallbackService helipayPayCallbackService;

    /// 合利宝支付回调
    @Operation(summary = "合利宝支付回调")
    @PostMapping("/pay")
    public String payNotify(@PathVariable("mchNo") String mchNo,
                            @PathVariable("channelMchNo") String channelMchNo,
                            HttpServletRequest request) {
        merchantContextLoader.bindMchNoForCallback(mchNo);
        return helipayPayCallbackService.payHandle(channelMchNo, request);
    }
}
