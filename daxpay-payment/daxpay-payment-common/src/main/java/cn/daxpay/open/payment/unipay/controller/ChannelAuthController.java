package cn.daxpay.open.payment.unipay.controller;

import cn.daxpay.open.platform.core.annotation.IgnoreAuth;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.result.Result;
import cn.daxpay.open.platform.core.util.ValidationUtil;
import cn.daxpay.open.payment.unipay.param.assist.AuthCodeParam;
import cn.daxpay.open.payment.unipay.param.assist.GenerateAuthUrlParam;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.unipay.result.assist.AuthResult;
import cn.daxpay.open.payment.unipay.result.assist.AuthUrlResult;
import cn.daxpay.open.payment.common.util.DaxRes;
import cn.daxpay.open.payment.old.pay.anno.PaymentVerify;
import cn.daxpay.open.payment.old.pay.service.assist.ChannelAuthService;
import cn.daxpay.open.payment.common.context.PaymentAssistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/// # 通道认证服务
///
@IgnoreAuth
@Tag(name = "通道认证服务")
@RestController
@RequestMapping("/unipay/assist/channel/auth")
@RequiredArgsConstructor
public class ChannelAuthController {

    private final ChannelAuthService channelAuthService;

    private final PaymentAssistService paymentAssistService;

    @PaymentVerify
    @Operation(summary = "获取授权链接")
    @PostMapping("/generate-auth-url")
    public DaxResult<AuthUrlResult> generateAuthUrl(@RequestBody GenerateAuthUrlParam param){
        ValidationUtil.validateParam(param);
        paymentAssistService.initMchAndApp(param.getAppId());
        return DaxRes.ok(channelAuthService.generateAuthUrl(param));
    }

    @Operation(summary = "通过AuthCode获取认证结果")
    @PostMapping("/auth")
    public Result<AuthResult> auth(@RequestBody AuthCodeParam param){
        return Res.ok(channelAuthService.auth(param));
    }

    @Operation(summary = "通过AuthCode获取并设置认证结果")
    @PostMapping("/auth-and-set")
    public Result<Void> authAndSet(@RequestBody AuthCodeParam param){
        channelAuthService.auth(param);
        return Res.ok();
    }

}
