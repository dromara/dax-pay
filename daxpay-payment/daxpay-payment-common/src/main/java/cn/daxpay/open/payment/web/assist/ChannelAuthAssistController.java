package cn.daxpay.open.payment.web.assist;

import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.result.Result;
import cn.daxpay.open.payment.old.pay.service.assist.ChannelAuthService;
import cn.daxpay.open.payment.unipay.param.assist.GenerateAuthUrlParam;
import cn.daxpay.open.payment.unipay.result.assist.AuthResult;
import cn.daxpay.open.payment.unipay.result.assist.AuthUrlResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/// # 通道认证服务
///
@Validated
@Tag(name = "通道认证服务")
@RestController
@RequestMapping("/assist/channel/auth")
@RequiredArgsConstructor
public class ChannelAuthAssistController {

    private final ChannelAuthService channelAuthService;

    @Operation(summary = "获取授权链接")
    @PostMapping("/generate-auth-url")
    public Result<AuthUrlResult> generateAuthUrl(@RequestBody GenerateAuthUrlParam param){
        return Res.ok(channelAuthService.generateAuthUrl(param));
    }

    @Operation(summary = "通过查询码获取认证结果")
    @GetMapping("/query-auth-result")
    public Result<AuthResult> queryAuthResult(@NotBlank(message = "{validation.field.queryCode.notBlank}") String queryCode) {
        return Res.ok(channelAuthService.queryAuthResult(queryCode));
    }
}
