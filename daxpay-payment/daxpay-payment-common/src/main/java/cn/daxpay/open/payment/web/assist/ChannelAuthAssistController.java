package cn.daxpay.open.payment.web.assist;

import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.result.Result;
import cn.daxpay.open.payment.core.assist.AuthSessionStore;
import cn.daxpay.open.payment.core.assist.ChannelAuthService;
import cn.daxpay.open.payment.core.assist.PlatformAuthService;
import cn.daxpay.open.payment.unipay.param.assist.GenerateAuthUrlParam;
import cn.daxpay.open.payment.unipay.result.assist.AuthResult;
import cn.daxpay.open.payment.unipay.result.assist.AuthUrlResult;
import cn.daxpay.open.platform.core.enums.unipay.ChannelAuthTypeEnum;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

/// # 通道认证服务(管理端)
@Validated
@Tag(name = "通道认证服务")
@RestController
@RequestMapping("/assist/channel/auth")
@RequiredArgsConstructor
public class ChannelAuthAssistController {

    private final AuthSessionStore authSessionStore;
    private final PlatformAuthService platformAuthService;
    private final ChannelAuthService channelAuthService;

    @Operation(summary = "获取授权链接")
    @PostMapping("/generate-auth-url")
    public Result<AuthUrlResult> generateAuthUrl(@RequestBody GenerateAuthUrlParam param) {
        // 支付宝: 平台级 H5 中间页(不依赖商户上下文)
        if (isAlipayAuth(param.getAuthType())) {
            return Res.ok(platformAuthService.generateAlipayAuthUrl());
        }
        return Res.ok(channelAuthService.generateAuthUrl(param));
    }

    @Operation(summary = "通过查询码获取认证结果")
    @GetMapping("/query-auth-result")
    public Result<AuthResult> queryAuthResult(@NotBlank(message = "{validation.field.queryCode.notBlank}") String queryCode) {
        return Res.ok(authSessionStore.queryAuthResult(queryCode));
    }

    /// 是否支付宝认证类型(平台级支付宝走 H5 中间页)
    private boolean isAlipayAuth(String authType) {
        return Objects.equals(authType, ChannelAuthTypeEnum.ALIPAY.getCode());
    }
}
