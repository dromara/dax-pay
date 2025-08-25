package org.dromara.daxpay.controller.gateway;

import cn.bootx.platform.core.annotation.ClientCode;
import cn.bootx.platform.core.annotation.RequestGroup;
import cn.bootx.platform.core.annotation.RequestPath;
import cn.bootx.platform.core.rest.Res;
import cn.bootx.platform.core.rest.result.Result;
import cn.bootx.platform.core.validation.ValidationGroup;
import org.dromara.daxpay.service.common.code.DaxPayCode;
import org.dromara.daxpay.service.merchant.param.gateway.GatewayPayConfigParam;
import org.dromara.daxpay.service.merchant.result.gateway.GatewayPayConfigResult;
import org.dromara.daxpay.service.merchant.service.gateway.GatewayPayConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 网关支付配置
 * @author xxm
 * @since 2025/3/24
 */
@Validated
@Tag(name = "网关支付配置")
@RestController
@RequestMapping("/gateway/config")
@ClientCode({DaxPayCode.Client.ADMIN, DaxPayCode.Client.MERCHANT})
@RequestGroup(groupCode = "AgentGatewayPayConfig", groupName = "网关支付配置", moduleCode = "GatewayPay", moduleName = "(DaxPay)网关支付配置")
@RequiredArgsConstructor
public class GatewayPayConfigController {
    private final GatewayPayConfigService gatewayPayConfigService;

    @RequestPath("获取网关支付配置")
    @Operation(summary = "获取网关支付配置")
    @GetMapping("/getConfig")
    public Result<GatewayPayConfigResult> getConfig(@NotBlank(message = "商户应用ID不可为空") String appId) {
        return Res.ok(gatewayPayConfigService.findByAppId(appId).toResult());
    }

    @RequestPath("更新网关支付配置")
    @Operation(summary = "更新网关支付配置")
    @PostMapping("/update")
    public Result<Void> update(@Validated(ValidationGroup.edit.class) @RequestBody GatewayPayConfigParam param) {
        gatewayPayConfigService.update(param);
        return Res.ok();
    }
}
