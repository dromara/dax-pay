package cn.daxpay.open.platform.system.controller.config.mobile;

import cn.daxpay.open.platform.core.annotation.PermCode;
import cn.daxpay.open.platform.core.code.PermCodes;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.result.Result;
import cn.daxpay.open.platform.system.param.mobile.MobileAppParam;
import cn.daxpay.open.platform.system.result.mobile.MobileAppResult;
import cn.daxpay.open.platform.system.service.mobile.MobileAppService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/// # 移动端应用配置（运营端 API）
///
/// 平台级移动端应用(商户端/管理端/收银台小程序)的密钥、通知、用户绑定配置管理。
/// 按端类型(appType)+移动平台(platform)维度, 每组合一条配置记录。
@PermCode(menuCode = PermCodes.System.MobileApp.MENU)
@Validated
@Tag(name = "移动端应用配置管理")
@RestController
@RequestMapping("/platform/config/mobile-app")
@RequiredArgsConstructor
public class MobileAppController {

    private final MobileAppService mobileAppService;

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "查询全部(按端类型分组)")
    @GetMapping("/list")
    public Result<List<MobileAppResult>> list() {
        return Res.ok(mobileAppService.findAll());
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "按端类型查询所有平台配置")
    @GetMapping("/list-by-app-type")
    public Result<List<MobileAppResult>> listByAppType(
            @NotBlank(message = "{validation.field.appType.notBlank}") String appType) {
        return Res.ok(mobileAppService.findAllByAppType(appType));
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "查询单条详情")
    @GetMapping("/get")
    public Result<MobileAppResult> findById(
            @NotNull(message = "{validation.field.id.notNull}") Long id) {
        return Res.ok(mobileAppService.findById(id));
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "按端类型+平台查询(不存在返回 null)")
    @GetMapping("/get-by-type-platform")
    public Result<MobileAppResult> findByAppTypeAndPlatform(
            @NotBlank(message = "{validation.field.appType.notBlank}") String appType,
            @NotBlank(message = "{validation.field.platform.notBlank}") String platform) {
        return Res.ok(mobileAppService.findByAppTypeAndPlatform(appType, platform).orElse(null));
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "保存(按端类型+平台 upsert)")
    @PostMapping("/save")
    public Result<MobileAppResult> save(@RequestBody @Validated MobileAppParam param) {
        return Res.ok(mobileAppService.save(param));
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "更新启用状态")
    @PostMapping("/update-enabled")
    public Result<Void> updateEnabled(
            @NotNull(message = "{validation.field.id.notNull}") Long id,
            @NotNull(message = "{validation.field.enable.notNull}") Boolean enable) {
        mobileAppService.updateEnabled(id, enable);
        return Res.ok();
    }
}
