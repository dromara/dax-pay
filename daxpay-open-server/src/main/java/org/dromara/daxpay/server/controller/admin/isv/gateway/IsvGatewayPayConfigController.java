package org.dromara.daxpay.server.controller.admin.isv.gateway;

import cn.bootx.platform.core.annotation.ClientCode;
import cn.bootx.platform.core.annotation.RequestGroup;
import cn.bootx.platform.core.annotation.RequestPath;
import cn.bootx.platform.core.rest.Res;
import cn.bootx.platform.core.rest.result.Result;
import cn.bootx.platform.core.validation.ValidationGroup;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.dromara.daxpay.service.common.code.DaxPayCode;
import org.dromara.daxpay.service.isv.param.gateway.IsvGatewayPayConfigParam;
import org.dromara.daxpay.service.isv.result.gateway.IsvGatewayPayConfigResult;
import org.dromara.daxpay.service.isv.service.gateway.IsvGatewayPayConfigService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 网关支付配置
 * @author xxm
 * @since 2025/3/24
 */
@Validated
@ClientCode({DaxPayCode.Client.ADMIN})
@Tag(name = "网关支付配置(服务商)")
@RestController
@RequestMapping("/isv/gateway/config")
@RequestGroup(groupCode = "IsvGatewayPayConfig", groupName = "网关支付配置(服务商)", moduleCode = "GatewayPay")
@RequiredArgsConstructor
public class IsvGatewayPayConfigController {
    private final IsvGatewayPayConfigService gatewayPayConfigService;

    @RequestPath("获取网关支付配置")
    @Operation(summary = "获取网关支付配置")
    @GetMapping("/getConfig")
    public Result<IsvGatewayPayConfigResult> getConfig() {
        return Res.ok(gatewayPayConfigService.getConfig());
    }

    @RequestPath("更新网关支付配置")
    @Operation(summary = "更新网关支付配置")
    @PostMapping("/update")
    public Result<Void> update(@Validated(ValidationGroup.edit.class) @RequestBody IsvGatewayPayConfigParam param) {
        gatewayPayConfigService.update(param);
        return Res.ok();
    }
}
