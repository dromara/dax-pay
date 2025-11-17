package org.dromara.daxpay.payment.common.controller.assist;

import cn.bootx.platform.core.annotation.ClientCode;
import cn.bootx.platform.core.annotation.RequestGroup;
import cn.bootx.platform.core.annotation.RequestPath;
import cn.bootx.platform.core.rest.Res;
import cn.bootx.platform.core.rest.result.Result;
import org.dromara.daxpay.payment.common.code.DaxPayCode;
import org.dromara.daxpay.payment.pay.service.assist.ChannelAuthService;
import org.dromara.daxpay.payment.pay.service.assist.PaymentAssistService;
import org.dromara.daxpay.payment.unipay.param.assist.GenerateAuthUrlParam;
import org.dromara.daxpay.payment.unipay.result.assist.AuthResult;
import org.dromara.daxpay.payment.unipay.result.assist.AuthUrlResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 通道认证服务
 * @author xxm
 * @since 2024/9/24
 */
@Validated
@Tag(name = "通道认证服务")
@RestController
@ClientCode({DaxPayCode.Client.ADMIN, DaxPayCode.Client.MERCHANT})
@RequestGroup(groupCode = "ChannelAuth", groupName = "通道认证", moduleCode = "paymentAssist")
@RequestMapping("/assist/channel/auth")
@RequiredArgsConstructor
public class ChannelAuthAssistController {

    private final ChannelAuthService channelAuthService;

    private final PaymentAssistService paymentAssistService;

    @RequestPath("获取授权链接")
    @Operation(summary = "获取授权链接")
    @PostMapping("/generateAuthUrl")
    public Result<AuthUrlResult> generateAuthUrl(@RequestBody GenerateAuthUrlParam param){
        paymentAssistService.initMchAndApp(param.getAppId());
        return Res.ok(channelAuthService.generateAuthUrl(param));
    }

    @RequestPath("通过查询码获取认证结果")
    @Operation(summary = "通过查询码获取认证结果")
    @GetMapping("/queryAuthResult")
    public Result<AuthResult> queryAuthResult(@NotBlank(message = "查询码不可为空") String queryCode) {
        return Res.ok(channelAuthService.queryAuthResult(queryCode));
    }
}
