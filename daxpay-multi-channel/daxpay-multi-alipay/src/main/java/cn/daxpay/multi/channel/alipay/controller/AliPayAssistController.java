package cn.daxpay.multi.channel.alipay.controller;

import cn.bootx.platform.core.annotation.IgnoreAuth;
import cn.daxpay.multi.channel.alipay.service.extra.AliPayAuthService;
import cn.daxpay.multi.core.param.assist.GenerateAuthUrlParam;
import cn.daxpay.multi.core.param.assist.GetOpenIdParam;
import cn.daxpay.multi.core.param.assist.QueryOpenIdParam;
import cn.daxpay.multi.core.result.DaxResult;
import cn.daxpay.multi.core.result.assist.AuthUrlResult;
import cn.daxpay.multi.core.result.assist.OpenIdResult;
import cn.daxpay.multi.core.util.DaxRes;
import cn.daxpay.multi.service.common.anno.PaymentVerify;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 支付宝支撑性接口
 * @author xxm
 * @since 2024/9/19
 */
@PaymentVerify
@IgnoreAuth
@Tag(name = "支付宝支撑性接口")
@RestController
@RequestMapping("/unipay/assist/alipay")
@RequiredArgsConstructor
public class AliPayAssistController {
    private final AliPayAuthService aliPayAuthService;


    @Operation(summary = "返回获取OpenId授权页面地址和标识码")
    @PostMapping("/generateAuthUrl")
    public DaxResult<AuthUrlResult> generateAuthUrl(@RequestBody GenerateAuthUrlParam param){
        return DaxRes.ok(aliPayAuthService.generateAuthUrl(param));
    }

    @Operation(summary = "根据标识码查询OpenId")
    @PostMapping("/queryOpenId")
    public DaxResult<OpenIdResult> queryOpenId(@RequestBody QueryOpenIdParam param){
        return DaxRes.ok(aliPayAuthService.queryOpenId(param.getCode()));
    }

    @Operation(summary = "获取OpenId或者userid用户标识")
    @PostMapping("/getOpenId")
    public DaxResult<String> getOpenId(@RequestBody GetOpenIdParam param){
        return DaxRes.ok(aliPayAuthService.getOpenIdOrUserId(param.getAuthCode()));
    }
}
