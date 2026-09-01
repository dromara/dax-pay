package cn.daxpay.open.payment.app.admin.controller.security;

import cn.daxpay.open.platform.core.annotation.PermCode;
import cn.daxpay.open.platform.core.code.PermCodes;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.result.Result;
import cn.daxpay.open.platform.system.param.config.security.PlatformApiSecurityConfigParam;
import cn.daxpay.open.platform.system.param.config.security.PlatformPaySecurityConfigParam;
import cn.daxpay.open.platform.system.result.config.security.PlatformApiSecurityConfigResult;
import cn.daxpay.open.platform.system.result.config.security.PlatformPaySecurityConfigResult;
import cn.daxpay.open.platform.system.service.config.security.PlatformSecurityConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/// 小程序管理端-支付安全配置
///
/// 镜像自 admin 版 [PayRiskSecurityConfigController]: 同权限码同 Service, 供小程序端经 /app-admin/* 前缀访问。
/// API 安全配置端点一并镜像, 小程序端一期仅消费风控策略(pay-security), api-security 留作后续扩展。
@PermCode(menuCode = PermCodes.Payment.Risk.Security.MENU)
@Validated
@Tag(name = "小程序管理端-支付安全配置")
@RestController
@RequestMapping("/app-admin/risk/security")
@RequiredArgsConstructor
public class AppAdminPayRiskSecurityConfigController {

    private final PlatformSecurityConfigService platformSecurityConfigService;

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "获取API安全配置")
    @GetMapping("/api-security/get")
    public Result<PlatformApiSecurityConfigResult> getApiSecurityConfig() {
        return Res.ok(platformSecurityConfigService.findApiSecurityConfig());
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "更新API安全配置")
    @PostMapping("/api-security/update")
    public Result<Void> updateApiSecurityConfig(@RequestBody @Validated PlatformApiSecurityConfigParam param) {
        platformSecurityConfigService.updateApiSecurityConfig(param);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "获取风控策略配置")
    @GetMapping("/pay-security/get")
    public Result<PlatformPaySecurityConfigResult> getPaySecurityConfig() {
        return Res.ok(platformSecurityConfigService.findPaySecurityConfig());
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "更新风控策略配置")
    @PostMapping("/pay-security/update")
    public Result<Void> updatePaySecurityConfig(@RequestBody @Validated PlatformPaySecurityConfigParam param) {
        platformSecurityConfigService.updatePaySecurityConfig(param);
        return Res.ok();
    }
}
