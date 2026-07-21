package cn.daxpay.open.payment.unipay.trade.controller;

import cn.daxpay.open.platform.capability.douyin.auth.result.DouyinJsapiConfigResult;
import cn.daxpay.open.platform.capability.douyin.auth.service.DouyinOpenTokenService;
import cn.daxpay.open.platform.core.annotation.IgnoreAuth;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.result.Result;
import cn.daxpay.open.platform.system.entity.config.platform.auth.PlatformDouyinH5AuthConfig;
import cn.daxpay.open.platform.system.service.config.auth.PlatformDouyinH5AuthConfigService;
import cn.hutool.core.util.StrUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/// # 抖音 H5 JSAPI 调起前置 - sdk.config 验签接口
///
/// 给前端 H5(抖音 APP webview) 调用: 前端拉起 `ttcjpay.dypay` 前必须先 `window.DouyinOpenJSBridge.config({...})`
/// 通过签名验证, 此接口返回该签名包(clientKey + timestamp + nonceStr + signature)。
///
/// 签名基于平台级 [PlatformDouyinH5AuthConfig](clientKey/clientSecret) 换取的 jsapi_ticket 计算,
/// 不依赖商户上下文, 故接口路径不挂 `@PaymentVerify`, 仅 `@IgnoreAuth` 跳过登录即可。
///
/// 算法: `MD5(jsapi_ticket={}&nonce_str={}&timestamp={}&url={})`, 字典序拼接。
///
/// 参考文档:
/// - JS 接入指南: https://developer.open-douyin.com/docs/resource/zh-CN/dop/develop/sdk/web-app/js/js-access
/// - 验证签名:   https://developer.open-douyin.com/docs/resource/zh-CN/dop/develop/sdk/web-app/js/signature
@IgnoreAuth
@Tag(name = "抖音 JSAPI 调起辅助")
@RestController
@RequestMapping("/unipay/assist/channel/douyin")
@RequiredArgsConstructor
public class DouyinJsapiController {

    private final PlatformDouyinH5AuthConfigService platformDouyinH5AuthConfigService;
    private final DouyinOpenTokenService douyinOpenTokenService;

    @Operation(summary = "获取抖音 JS-SDK sdk.config 验签包")
    @Parameter(name = "url", description = "调用 JS 接口页面的完整 URL(不含 # 及后面部分)", required = true)
    @GetMapping("/jsapi-config")
    public Result<DouyinJsapiConfigResult> getJsapiConfig(@RequestParam("url") String url) {
        if (StrUtil.isBlank(url)) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "error.channel.douyin.jsapiConfigParamBlank");
        }
        PlatformDouyinH5AuthConfig config = platformDouyinH5AuthConfigService.getDouyinH5AuthConfig();
        if (StrUtil.hasBlank(config.getClientKey(), config.getClientSecret())) {
            // 抖音 H5 应用认证配置不完整, 请先在「平台配置」中配置 clientKey/clientSecret
            throw new BizInfoException(CommonErrorCode.SYSTEM_ERROR,
                    "error.channel.douyin.h5AuthNotConfigured");
        }
        DouyinJsapiConfigResult result = douyinOpenTokenService.buildJsapiConfig(
                config.getClientKey(), config.getClientSecret(), url);
        return Res.ok(result);
    }
}
