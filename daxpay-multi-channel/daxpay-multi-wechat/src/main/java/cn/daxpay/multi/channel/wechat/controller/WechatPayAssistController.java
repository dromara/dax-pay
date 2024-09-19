package cn.daxpay.multi.channel.wechat.controller;

import cn.bootx.platform.core.annotation.IgnoreAuth;
import cn.daxpay.multi.channel.wechat.result.assist.WxTokenAndOpenIdResult;
import cn.daxpay.multi.channel.wechat.service.assist.WechatAuthService;
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
 * 微信支撑性接口
 * @author xxm
 * @since 2024/9/19
 */
@PaymentVerify
@IgnoreAuth
@Tag(name = "微信支撑性接口")
@RestController
@RequestMapping("/unipay/assist/wechat")
@RequiredArgsConstructor
public class WechatPayAssistController {
    private final WechatAuthService wechatAuthService;

    @Operation(summary = "返回获取OpenId授权页面地址和标识码")
    @PostMapping("/generateAuthUrl")
    public DaxResult<AuthUrlResult> generateAuthUrl(@RequestBody GenerateAuthUrlParam param){
        return DaxRes.ok(wechatAuthService.generateAuthUrl(param));
    }

    @Operation(summary = "根据标识码查询OpenId")
    @PostMapping("/queryOpenId")
    public DaxResult<OpenIdResult> queryOpenId(@RequestBody QueryOpenIdParam param){
        return DaxRes.ok(wechatAuthService.queryOpenId(param.getCode()));
    }

    @Operation(summary = "获取OpenId或者userid用户标识")
    @PostMapping("/getTokenAndOpenId")
    public DaxResult<WxTokenAndOpenIdResult> getOpenIdAndAccessToken(@RequestBody GetOpenIdParam param){
        return DaxRes.ok(wechatAuthService.getTokenAndOpenId(param.getAuthCode()));
    }

}
