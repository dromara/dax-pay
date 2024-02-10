package cn.bootx.platform.daxpay.gateway.controller;

import cn.bootx.platform.common.core.annotation.CountTime;
import cn.bootx.platform.common.core.annotation.IgnoreAuth;
import cn.bootx.platform.common.core.rest.Res;
import cn.bootx.platform.common.core.rest.ResResult;
import cn.bootx.platform.daxpay.param.assist.WxAccessTokenParam;
import cn.bootx.platform.daxpay.param.assist.WxAuthUrlParam;
import cn.bootx.platform.daxpay.param.assist.WxJsapiPrePayParam;
import cn.bootx.platform.daxpay.result.DaxResult;
import cn.bootx.platform.daxpay.result.assist.WxAccessTokenResult;
import cn.bootx.platform.daxpay.result.assist.WxAuthUrlResult;
import cn.bootx.platform.daxpay.result.assist.WxJsapiPrePayResult;
import cn.bootx.platform.daxpay.service.annotation.PaymentApi;
import cn.bootx.platform.daxpay.service.core.payment.assist.service.UniPayAssistService;
import cn.bootx.platform.daxpay.util.DaxRes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 支付支撑接口
 * @author xxm
 * @since 2024/2/10
 */
@IgnoreAuth
@Tag(name = "支付支撑接口")
@RestController
@RequestMapping("/unipay/assist")
@RequiredArgsConstructor
public class UniPayAssistController {
    private final UniPayAssistService uniPayAssistService;

    @CountTime
    @PaymentApi("getWxAuthUrl")
    @Operation(summary = "获取微信oauth2授权的url")
    @PostMapping("/getWxAuthUrl")
    public DaxResult<WxAuthUrlResult> getWxAuthUrl(@RequestBody WxAuthUrlParam param){
        return DaxRes.ok(uniPayAssistService.getWxAuthUrl(param));
    }

    @CountTime
    @PaymentApi("getWxAccessToken")
    @Operation(summary = "获取微信AccessToken数据")
    @PostMapping("/getWxAccessToken")
    public ResResult<WxAccessTokenResult> getWxAccessToken(@RequestBody WxAccessTokenParam param){
        return Res.ok(uniPayAssistService.getWxAccessToken(param));
    }

    @CountTime
    @PaymentApi("getWxJsapiPrePay")
    @Operation(summary = "获取微信预支付信息")
    @PostMapping("/getWxJsapiPrePay")
    public ResResult<WxJsapiPrePayResult> getWxJsapiPrePay(@RequestBody WxJsapiPrePayParam param){
        return Res.ok(uniPayAssistService.getWxJsapiPrePay(param));
    }


}
