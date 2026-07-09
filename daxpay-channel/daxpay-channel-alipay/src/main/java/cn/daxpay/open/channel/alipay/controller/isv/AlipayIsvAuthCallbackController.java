package cn.daxpay.open.channel.alipay.controller.isv;

import cn.daxpay.open.channel.alipay.param.isv.AlipayIsvAuthParam;
import cn.daxpay.open.channel.alipay.service.isv.AlipayIsvAuthService;
import cn.daxpay.open.platform.core.annotation.IgnoreAuth;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/// # 支付宝服务商代运营授权回调(H5 无鉴权)
///
/// 支付宝代运营授权完成后跳转 H5 落地页, 落地页携带 app_auth_code 与 state 调用本接口完成换 token。
///
@IgnoreAuth
@Validated
@Tag(name = "支付宝服务商代运营授权回调")
@RestController
@RequestMapping("/unipay/assist/alipay/isv/auth")
@RequiredArgsConstructor
public class AlipayIsvAuthCallbackController {

    private final AlipayIsvAuthService alipayIsvAuthService;

    @Operation(summary = "授权码换取应用授权令牌")
    @PostMapping("/callback")
    public Result<Void> callback(@RequestBody @Validated AlipayIsvAuthParam param) {
        alipayIsvAuthService.auth(param);
        return Res.ok();
    }
}
