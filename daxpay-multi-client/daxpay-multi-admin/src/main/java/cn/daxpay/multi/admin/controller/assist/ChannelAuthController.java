package cn.daxpay.multi.admin.controller.assist;

import cn.bootx.platform.core.annotation.RequestGroup;
import cn.bootx.platform.core.annotation.RequestPath;
import cn.bootx.platform.core.rest.Res;
import cn.bootx.platform.core.rest.result.Result;
import cn.daxpay.multi.core.param.assist.GenerateAuthUrlParam;
import cn.daxpay.multi.core.result.assist.AuthResult;
import cn.daxpay.multi.core.result.assist.AuthUrlResult;
import cn.daxpay.multi.service.service.assist.ChannelAuthService;
import cn.daxpay.multi.service.service.assist.PaymentAssistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 通道认证服务
 * @author xxm
 * @since 2024/9/24
 */
@Tag(name = "通道认证服务")
@RestController
@RequestGroup(groupCode = "ChannelAuth", groupName = "通道认证", moduleCode = "assist", moduleName = "辅助功能")
@RequestMapping("/assist/channel/auth")
@RequiredArgsConstructor
public class ChannelAuthController {

    private final ChannelAuthService channelAuthService;

    private final PaymentAssistService paymentAssistService;

    @RequestPath("获取授权链接")
    @Operation(summary = "获取授权链接")
    @PostMapping("/generateAuthUrl")
    public Result<AuthUrlResult> generateAuthUrl(@RequestBody GenerateAuthUrlParam param){
        paymentAssistService.initMchAndApp(param.getAppId());
        return Res.ok(channelAuthService.generateAuthUrl(param));
    }

    @Operation(summary = "通过查询码获取认证结果")
    @RequestPath("通过查询码获取认证结果")
    @GetMapping("/queryAuthResult")
    public Result<AuthResult> queryAuthResult(String queryCode) {
        return Res.ok(channelAuthService.queryAuthResult(queryCode));
    }
}
