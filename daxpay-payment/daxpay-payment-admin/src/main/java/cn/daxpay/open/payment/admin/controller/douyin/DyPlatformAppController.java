package cn.daxpay.open.payment.admin.controller.douyin;

import cn.daxpay.open.payment.douyin.convert.platform.DyPlatformAppAuthConfigConvert;
import cn.daxpay.open.payment.douyin.param.platform.DyPlatformAppAuthConfigParam;
import cn.daxpay.open.payment.douyin.param.platform.DyPlatformAppParam;
import cn.daxpay.open.payment.douyin.result.platform.DyPlatformAppAuthConfigResult;
import cn.daxpay.open.payment.douyin.result.platform.DyPlatformAppResult;
import cn.daxpay.open.payment.douyin.service.platform.DyPlatformAppAuthConfigService;
import cn.daxpay.open.payment.douyin.service.platform.DyPlatformAppService;
import cn.daxpay.open.platform.core.annotation.PermCode;
import cn.daxpay.open.platform.core.code.PermCodes;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.result.Result;
import cn.daxpay.open.platform.core.util.ValidationUtil;
import cn.daxpay.open.platform.core.validation.ValidationGroup;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/// # 平台抖音应用管理（运营端）
///
@PermCode(menuCode = PermCodes.Payment.Douyin.PlatformApp.MENU)
@Validated
@Tag(name = "平台抖音应用管理")
@RestController
@RequestMapping("/admin/douyin/platform-app")
@RequiredArgsConstructor
public class DyPlatformAppController {

    private final DyPlatformAppService dyPlatformAppService;
    private final DyPlatformAppAuthConfigService dyPlatformAppAuthConfigService;

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "查询平台抖音应用列表")
    @GetMapping("/list-all")
    public Result<List<DyPlatformAppResult>> listAll() {
        return Res.ok(dyPlatformAppService.listAll());
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "查询应用详情")
    @GetMapping("/find-by-id")
    public Result<DyPlatformAppResult> findById(
            @NotNull(message = "{validation.field.id.notNull}") Long id) {
        return Res.ok(dyPlatformAppService.findById(id));
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "抖音应用AppId是否已存在")
    @GetMapping("/exists-douyin-app-id")
    public Result<Boolean> existsDouyinAppId(
            @NotBlank(message = "{validation.field.douyinAppId.notBlank}") String douyinAppId) {
        return Res.ok(dyPlatformAppService.existsDouyinAppId(douyinAppId, null));
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "抖音应用AppId是否已存在(排除自身)")
    @GetMapping("/exists-douyin-app-id-not-id")
    public Result<Boolean> existsDouyinAppIdNotId(
            @NotBlank(message = "{validation.field.douyinAppId.notBlank}") String douyinAppId,
            @NotNull(message = "{validation.field.id.notNull}") Long id) {
        return Res.ok(dyPlatformAppService.existsDouyinAppId(douyinAppId, id));
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "新增平台抖音应用")
    @PostMapping("/add")
    public Result<Void> add(@RequestBody @Validated(ValidationGroup.add.class) DyPlatformAppParam param) {
        ValidationUtil.validateParam(param, ValidationGroup.add.class);
        dyPlatformAppService.add(param);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "修改平台抖音应用")
    @PostMapping("/update")
    public Result<Void> update(@RequestBody @Validated(ValidationGroup.edit.class) DyPlatformAppParam param) {
        ValidationUtil.validateParam(param, ValidationGroup.edit.class);
        dyPlatformAppService.update(param);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "删除平台抖音应用")
    @PostMapping("/delete")
    public Result<Void> delete(@NotNull(message = "{validation.field.id.notNull}") Long id) {
        dyPlatformAppService.delete(id);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "查询应用授权认证配置")
    @GetMapping("/find-auth-config-by-app-id")
    public Result<DyPlatformAppAuthConfigResult> findAuthConfigByAppId(
            @NotNull(message = "{validation.field.dyPlatformAppId.notNull}") Long dyPlatformAppId) {
        var config = dyPlatformAppAuthConfigService.findByDyPlatformAppId(dyPlatformAppId);
        return Res.ok(DyPlatformAppAuthConfigConvert.CONVERT.toResult(config));
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "保存应用授权认证配置")
    @PostMapping("/save-auth-config")
    public Result<Void> saveAuthConfig(@RequestBody @Validated DyPlatformAppAuthConfigParam param) {
        dyPlatformAppAuthConfigService.save(param);
        return Res.ok();
    }
}
