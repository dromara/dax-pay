package org.dromara.daxpay.service.controller.unipay;

import cn.bootx.platform.core.annotation.IgnoreAuth;
import cn.bootx.platform.core.rest.Res;
import cn.bootx.platform.core.rest.result.Result;
import cn.bootx.platform.core.util.ValidationUtil;
import org.dromara.daxpay.core.param.assist.AuthCodeParam;
import org.dromara.daxpay.core.param.assist.GenerateAuthUrlParam;
import org.dromara.daxpay.core.result.DaxResult;
import org.dromara.daxpay.core.result.assist.AuthResult;
import org.dromara.daxpay.core.result.assist.AuthUrlResult;
import org.dromara.daxpay.core.util.DaxRes;
import org.dromara.daxpay.service.common.anno.PaymentVerify;
import org.dromara.daxpay.service.service.assist.ChannelAuthService;
import org.dromara.daxpay.service.service.assist.PaymentAssistService;
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
public class ChannelUniAuthController {

    private final ChannelAuthService channelAuthService;

    private final PaymentAssistService paymentAssistService;

    @Operation(summary = "获取授权链接")
    @PostMapping("/generateAuthUrl")
    public Result<AuthUrlResult> generateAuthUrl(@RequestBody GenerateAuthUrlParam param){
        ValidationUtil.validateParam(param);
        paymentAssistService.initMchApp(param.getAppId());
        return Res.ok(channelAuthService.generateAuthUrl(param));
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
        paymentAssistService.initMchApp(param.getAppId());
        channelAuthService.auth(param);
        return Res.ok();
    }

}
