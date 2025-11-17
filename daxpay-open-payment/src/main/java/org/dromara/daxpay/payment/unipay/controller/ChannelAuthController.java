package org.dromara.daxpay.payment.unipay.controller;

import cn.bootx.platform.core.annotation.IgnoreAuth;
import cn.bootx.platform.core.rest.Res;
import cn.bootx.platform.core.rest.result.Result;
import cn.bootx.platform.core.util.ValidationUtil;
import org.dromara.daxpay.payment.common.result.DaxResult;
import org.dromara.daxpay.payment.common.util.DaxRes;
import org.dromara.daxpay.payment.pay.anno.PaymentVerify;
import org.dromara.daxpay.payment.pay.service.assist.ChannelAuthService;
import org.dromara.daxpay.payment.pay.service.assist.PaymentAssistService;
import org.dromara.daxpay.payment.unipay.param.assist.AuthCodeParam;
import org.dromara.daxpay.payment.unipay.param.assist.GenerateAuthUrlParam;
import org.dromara.daxpay.payment.unipay.result.assist.AuthResult;
import org.dromara.daxpay.payment.unipay.result.assist.AuthUrlResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 通道认证服务
 * @author xxm
 * @since 2024/9/24
 */
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
    @PostMapping("/generateAuthUrl")
    public DaxResult<AuthUrlResult> generateAuthUrl(@RequestBody GenerateAuthUrlParam param){
        ValidationUtil.validateParam(param);
        paymentAssistService.initMchAndApp(param.getAppId());
        return DaxRes.ok(channelAuthService.generateAuthUrl(param));
    }

    @Operation(summary = "通过AuthCode获取认证结果")
    @PostMapping("/auth")
    public Result<AuthResult> auth(@RequestBody AuthCodeParam param){
        paymentAssistService.initMchAndApp(param.getAppId());
        return Res.ok(channelAuthService.auth(param));
    }

    @Operation(summary = "通过AuthCode获取并设置认证结果")
    @PostMapping("/authAndSet")
    public Result<Void> authAndSet(@RequestBody AuthCodeParam param){
        paymentAssistService.initMchAndApp(param.getAppId());
        channelAuthService.auth(param);
        return Res.ok();
    }

}
