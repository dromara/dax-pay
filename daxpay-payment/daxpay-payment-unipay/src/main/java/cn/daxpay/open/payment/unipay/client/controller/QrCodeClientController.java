package cn.daxpay.open.payment.unipay.client.controller;

import cn.daxpay.open.payment.unipay.client.result.CodePayInfoResult;
import cn.daxpay.open.payment.unipay.client.result.CodePayOrderStatusResult;
import cn.daxpay.open.payment.unipay.client.service.CodePayAssistService;
import cn.daxpay.open.payment.unipay.param.device.CodePayAuthUrlParam;
import cn.daxpay.open.payment.unipay.param.device.CodePayMiniAuthParam;
import cn.daxpay.open.payment.unipay.param.device.CodePayParam;
import cn.daxpay.open.payment.unipay.result.assist.AuthResult;
import cn.daxpay.open.payment.unipay.result.assist.AuthUrlResult;
import cn.daxpay.open.payment.unipay.result.trade.pay.NormalPayResult;
import cn.daxpay.open.platform.core.annotation.IgnoreAuth;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/// # 码牌支付(公开/H5/小程序侧)
///
/// 供 H5 码牌支付页(/h/:code)扫码后调用, 无需登录态。
@IgnoreAuth
@Validated
@Tag(name = "码牌支付")
@RestController
@RequestMapping("/client/device/qrcode")
@RequiredArgsConstructor
public class QrCodeClientController {

    private final CodePayAssistService codePayAssistService;

    @Operation(summary = "根据码牌编码查询支付信息")
    @GetMapping("/get-by-code")
    public Result<CodePayInfoResult> getByCode(
            @NotBlank(message = "{validation.field.code.notBlank}") String code,
            @RequestParam(required = false) String clientEnv) {
        return Res.ok(codePayAssistService.getByCode(code, clientEnv));
    }

    @Operation(summary = "码牌发起支付")
    @PostMapping("/pay")
    public Result<NormalPayResult> pay(@RequestBody @Validated CodePayParam param) {
        return Res.ok(codePayAssistService.pay(param));
    }

    @Operation(summary = "码牌生成 OAuth 授权链接")
    @PostMapping("/generate-auth-url")
    public Result<AuthUrlResult> generateAuthUrl(@RequestBody @Validated CodePayAuthUrlParam param) {
        return Res.ok(codePayAssistService.generateAuthUrl(param));
    }

    /// 码牌小程序认证(换openId/userId, 同步返回)
    ///
    /// 微信/支付宝/抖音小程序前端通过 uni.login / my.getAuthCode / tt.login 获取授权码后,
    /// 同步调用此端点换取用户标识。无需 H5 OAuth 跳转, 无商户签名。
    @Operation(summary = "码牌小程序认证(换openId/userId, 同步返回)")
    @PostMapping("/mini-auth")
    public Result<AuthResult> miniAuth(@RequestBody @Validated CodePayMiniAuthParam param) {
        return Res.ok(codePayAssistService.miniAuth(param));
    }

    @Operation(summary = "查询码牌订单状态")
    @GetMapping("/order-status")
    public Result<CodePayOrderStatusResult> orderStatus(@NotBlank(message = "{validation.field.orderNo.notBlank}") String orderNo) {
        return Res.ok(codePayAssistService.orderStatus(orderNo));
    }
}
