package cn.daxpay.open.channel.xingyi.controller.callback;

import cn.daxpay.open.channel.xingyi.service.callback.XingyiPayCallbackService;
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

/// # 星驿付支付回调通知控制器
///
/// 星驿付异步通知入口(支付回调, JSON 报文), 不走 Sa-Token 认证(由 @IgnoreAuth 注解放行)。
/// URL 中的 channelMchNo 用于回调时组装凭证(验签需商户公钥)。
/// 应答为星驿付约定 JSON 串 `{"rspCod":"000000","rspMsg":"success"}`(由子应用生成后原样返回)。
///
/// 星驿付仅有支付异步通知(退款为同步接口), 仅提供支付回调端点。
@Tag(name = "星驿付支付回调通知控制器")
@RestController
@RequiredArgsConstructor
@RequestMapping("/unipay/callback/{mchNo}/{channelMchNo}/xingyi_pay")
@IgnoreAuth
public class XingyiCallbackController {

    private final MerchantContextLoader merchantContextLoader;

    private final XingyiPayCallbackService xingyiPayCallbackService;

    /// 星驿付支付回调
    @Operation(summary = "星驿付支付回调")
    @PostMapping("/pay")
    public String payNotify(@PathVariable("mchNo") String mchNo,
                            @PathVariable("channelMchNo") String channelMchNo,
                            @RequestBody String body) {
        merchantContextLoader.bindMchNoForCallback(mchNo);
        return xingyiPayCallbackService.payHandle(mchNo, channelMchNo, body);
    }
}
