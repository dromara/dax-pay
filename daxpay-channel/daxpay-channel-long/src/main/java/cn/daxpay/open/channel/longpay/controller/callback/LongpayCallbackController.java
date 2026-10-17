package cn.daxpay.open.channel.longpay.controller.callback;

import cn.daxpay.open.channel.longpay.service.callback.LongpayPayCallbackService;
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

/// # 建行龙支付回调通知控制器
///
/// 建行异步通知入口(服务器实时反馈, urlencoded 表单), 不走 Sa-Token 认证(由 @IgnoreAuth 注解放行)。
/// URL 中的 channelMchNo 用于回调时组装凭证(验签需建行公钥)。
/// 应答为建行规约纯文本 SUCCESS(由子应用生成后原样返回), 处理失败回 FAIL。
///
/// 注意: 建行回调地址通常在建行商户平台侧统一配置, 平台按规范生成 `{backendBaseUrl}/unipay/callback/...`
/// 形态地址; 商户平台若仅支持单一全局通知地址, 联调时以建行侧配置为准。
@Tag(name = "建行龙支付回调通知控制器")
@RestController
@RequiredArgsConstructor
@RequestMapping("/unipay/callback/{mchNo}/{channelMchNo}/long")
@IgnoreAuth
public class LongpayCallbackController {

    private final MerchantContextLoader merchantContextLoader;
    private final LongpayPayCallbackService longpayPayCallbackService;

    /// 建行龙支付回调
    @Operation(summary = "建行龙支付回调")
    @PostMapping("/pay")
    public String payNotify(@PathVariable("mchNo") String mchNo,
                            @PathVariable("channelMchNo") String channelMchNo,
                            @RequestBody String body) {
        merchantContextLoader.bindMchNoForCallback(mchNo);
        return longpayPayCallbackService.payHandle(mchNo, channelMchNo, body);
    }
}
