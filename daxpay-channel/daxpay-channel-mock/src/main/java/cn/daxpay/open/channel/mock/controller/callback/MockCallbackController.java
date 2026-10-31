package cn.daxpay.open.channel.mock.controller.callback;

import cn.daxpay.open.channel.mock.service.callback.MockCallbackService;
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

/// # Mock 模拟通道回调通知控制器
///
/// mock 子应用模拟网关扮演三方平台回推异步通知的入口(支付/退款共用同一端点,
/// 形态照抄易支付 [cn.daxpay.open.channel.easypay.controller.callback.EasyPayCallbackController]),
/// 不走 Sa-Token 认证(由 @IgnoreAuth 注解放行)。
/// 报文为明文单层 JSON(event_type/summary/out_biz_no/data) + Mock-Sign/Mock-Timestamp/Mock-Nonce 三签名头,
/// 主应用不持密钥不验签, body 与三头原文原样转发 one 验签解析。
/// 路径 `{mchNo}/{channelMchNo}`: mchNo 装载租户上下文; channelMchNo 供回调记录留痕与凭证装配。
@Tag(name = "Mock模拟通道回调通知控制器")
@RestController
@RequestMapping("/unipay/callback/{mchNo}/{channelMchNo}/mock")
@RequiredArgsConstructor
@IgnoreAuth
public class MockCallbackController {

    private final MerchantContextLoader merchantContextLoader;
    private final MockCallbackService mockCallbackService;

    /// mock 回调(支付/退款统一入口, 由服务层按外层 event_type 前缀区分)
    @Operation(summary = "Mock模拟通道支付/退款回调")
    @PostMapping
    public String notify(@PathVariable String mchNo,
                         @PathVariable String channelMchNo,
                         @RequestBody String body,
                         @RequestHeader(value = "Mock-Sign", required = false) String sign,
                         @RequestHeader(value = "Mock-Timestamp", required = false) String timestamp,
                         @RequestHeader(value = "Mock-Nonce", required = false) String nonce) {
        // 显式装载商户租户上下文(不校验启用, 保证禁用商户历史单可回调)
        merchantContextLoader.bindMchNoForCallback(mchNo);
        return mockCallbackService.handle(channelMchNo, body, sign, timestamp, nonce);
    }
}
