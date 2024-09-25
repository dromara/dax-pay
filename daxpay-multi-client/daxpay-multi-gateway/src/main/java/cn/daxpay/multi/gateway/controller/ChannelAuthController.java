package cn.daxpay.multi.gateway.controller;

import cn.bootx.platform.core.annotation.IgnoreAuth;
import cn.bootx.platform.core.rest.Res;
import cn.bootx.platform.core.rest.result.Result;
import cn.daxpay.multi.core.param.assist.AuthCodeParam;
import cn.daxpay.multi.core.param.assist.GenerateAuthUrlParam;
import cn.daxpay.multi.core.result.DaxResult;
import cn.daxpay.multi.core.result.assist.AuthResult;
import cn.daxpay.multi.core.result.assist.AuthUrlResult;
import cn.daxpay.multi.core.util.DaxRes;
import cn.daxpay.multi.service.common.anno.PaymentVerify;
import cn.daxpay.multi.service.service.assist.ChannelAuthService;
import cn.daxpay.multi.service.service.assist.PaymentAssistService;
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
        return DaxRes.ok(channelAuthService.generateAuthUrl(param));
    }

    @PaymentVerify
    @Operation(summary = "通过AuthCode获取认证结果")
    @PostMapping("/auth")
    public DaxResult<AuthResult> auth(@RequestBody AuthCodeParam param){
        return DaxRes.ok(channelAuthService.auth(param));
    }

    @Operation(summary = "通过AuthCode获取并设置认证结果")
    @PostMapping("/authAndSet")
    public Result<Void> authAndSet(@RequestBody AuthCodeParam param){
        paymentAssistService.initMchAndApp(param.getAppId());
        channelAuthService.auth(param);
        return Res.ok();
    }

}
