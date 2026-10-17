package cn.daxpay.open.channel.shouqianba.controller.callback;

import cn.daxpay.open.channel.shouqianba.service.callback.ShouqianbaPayCallbackService;
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

/// # 收钱吧支付回调通知控制器
///
/// 收钱吧异步通知入口(仅支付回调; 退款为同步终态无回调), 不走 Sa-Token 认证(由 @IgnoreAuth 注解放行)。
/// 报文为 POST JSON 原文, 签名在 authorization 头(Base64 RSA-SHA256), 验签转发子应用用官方公钥完成。
/// 应答为纯文本 `success`(非该串收钱吧会持续重发)。
@Tag(name = "收钱吧支付回调通知控制器")
@RestController
@RequiredArgsConstructor
@RequestMapping("/unipay/callback/{mchNo}/{channelMchNo}/shouqianba")
@IgnoreAuth
public class ShouqianbaCallbackController {

    private final MerchantContextLoader merchantContextLoader;
    private final ShouqianbaPayCallbackService shouqianbaPayCallbackService;

    /// 收钱吧支付回调
    @Operation(summary = "收钱吧支付回调")
    @PostMapping("/pay")
    public String payNotify(@PathVariable("mchNo") String mchNo,
                            @PathVariable("channelMchNo") String channelMchNo,
                            @RequestHeader(value = "Authorization", required = false) String authorization,
                            @RequestBody String body) {
        merchantContextLoader.bindMchNoForCallback(mchNo);
        return shouqianbaPayCallbackService.payHandle(mchNo, channelMchNo, authorization, body);
    }
}
