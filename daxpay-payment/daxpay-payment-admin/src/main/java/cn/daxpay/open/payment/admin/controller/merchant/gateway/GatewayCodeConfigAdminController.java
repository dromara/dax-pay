package cn.daxpay.open.payment.admin.controller.merchant.gateway;

import cn.daxpay.open.payment.merchant.param.gateway.GatewayCodeConfigParam;
import cn.daxpay.open.payment.merchant.result.gateway.GatewayCodeConfigResult;
import cn.daxpay.open.payment.merchant.service.gateway.GatewayCodeConfigService;
import cn.daxpay.open.platform.core.annotation.PermCode;
import cn.daxpay.open.platform.core.code.PermCodes;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/// # 码牌支付策略配置(管理)
@PermCode(menuCode = PermCodes.Merchant.GatewayCode.MENU)
@Validated
@Tag(name = "码牌支付策略配置")
@RestController
@RequestMapping("/admin/gateway/code-config")
@RequiredArgsConstructor
public class GatewayCodeConfigAdminController {

    private final GatewayCodeConfigService gatewayCodeConfigService;

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "按应用查询码牌支付配置")
    @GetMapping("/get-by-app-id")
    public Result<GatewayCodeConfigResult> getByAppId(
            @NotBlank(message = "{validation.field.appId.notBlank}") String appId) {
        return Res.ok(gatewayCodeConfigService.findByAppId(appId));
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "保存或更新码牌支付配置")
    @PostMapping("/save-or-update")
    public Result<Void> saveOrUpdate(@RequestBody @Validated GatewayCodeConfigParam param) {
        gatewayCodeConfigService.saveOrUpdate(param);
        return Res.ok();
    }
}
