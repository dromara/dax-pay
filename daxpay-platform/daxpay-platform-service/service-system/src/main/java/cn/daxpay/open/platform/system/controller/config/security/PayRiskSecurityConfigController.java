package cn.daxpay.open.platform.system.controller.config.security;

import cn.daxpay.open.platform.core.code.PermCodes;
import cn.daxpay.open.platform.core.annotation.PermCode;
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
import org.springframework.web.bind.annotation.*;

/// # 支付安全配置
///
/// 管理支付链路的安全策略：开放接口防重放（API安全）与支付风控开关（风控策略）。
/// 配置实体与系统域安全配置共用 [PlatformSecurityConfigService]，仅接口按业务域
/// 归属拆分到支付管理菜单下。系统域安全配置（密码/登录/会话/双因素/IAM防重放）
/// 仍由 [PlatformSecurityConfigController] 提供。
@PermCode(menuCode = PermCodes.Payment.Risk.Security.MENU)
@Validated
@Tag(name = "支付安全配置")
@RestController
@RequestMapping("/payment/risk/security")
@RequiredArgsConstructor
public class PayRiskSecurityConfigController {

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
