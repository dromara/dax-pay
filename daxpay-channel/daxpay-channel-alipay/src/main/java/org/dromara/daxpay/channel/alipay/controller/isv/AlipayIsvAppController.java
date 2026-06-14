package org.dromara.daxpay.channel.alipay.controller.isv;

import org.dromara.daxpay.platform.core.annotation.PermCode;
import org.dromara.daxpay.platform.core.rest.Res;
import org.dromara.daxpay.platform.core.rest.result.Result;
import org.dromara.daxpay.platform.core.util.ValidationUtil;
import org.dromara.daxpay.platform.core.validation.ValidationGroup;
import org.dromara.daxpay.channel.alipay.param.isv.AlipayIsvAppParam;
import org.dromara.daxpay.channel.alipay.param.isv.AlipayIsvAppAuthConfigParam;
import org.dromara.daxpay.channel.alipay.param.isv.AlipayIsvAppKeyConfigParam;
import org.dromara.daxpay.channel.alipay.result.isv.AlipayIsvAppResult;
import org.dromara.daxpay.channel.alipay.result.isv.AlipayIsvAppAuthConfigResult;
import org.dromara.daxpay.channel.alipay.result.isv.AlipayIsvAppKeyConfigResult;
import org.dromara.daxpay.channel.alipay.service.isv.AlipayIsvAppService;
import org.dromara.daxpay.channel.alipay.service.isv.AlipayIsvAppAuthConfigService;
import org.dromara.daxpay.channel.alipay.service.isv.AlipayIsvAppKeyConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/// # 支付宝服务商应用管理
///
/// 提供服务商应用及其密钥配置、授权认证配置的 REST API，包含查询列表、详情、唯一性校验和增删改操作。
///
@PermCode(menuCode = "payment:alipay:isv")
@Validated
@Tag(name = "支付宝服务商应用管理")
@RestController
@RequestMapping("/admin/alipay/isv-app")
@RequiredArgsConstructor
public class AlipayIsvAppController {

    private final AlipayIsvAppService alipayIsvAppService;
    private final AlipayIsvAppKeyConfigService alipayIsvAppKeyConfigService;
    private final AlipayIsvAppAuthConfigService alipayIsvAppAuthConfigService;

    @PermCode(code = "view", nameCn = "支付宝服务商查看", nameEn = "Alipay ISV View")
    @Operation(summary = "查询服务商应用列表")
    @GetMapping("/list-all")
    public Result<List<AlipayIsvAppResult>> listAll() {
        return Res.ok(alipayIsvAppService.listAll());
    }

    @PermCode(code = "view", nameCn = "支付宝服务商查看", nameEn = "Alipay ISV View")
    @Operation(summary = "查询应用详情")
    @GetMapping("/find-by-id")
    public Result<AlipayIsvAppResult> findById(
            @NotNull(message = "{validation.field.id.notNull}") Long id) {
        return Res.ok(alipayIsvAppService.findById(id));
    }

    @PermCode(code = "view", nameCn = "支付宝服务商查看", nameEn = "Alipay ISV View")
    @Operation(summary = "支付宝应用ID是否已存在")
    @GetMapping("/exists-ali-app-id")
    public Result<Boolean> existsAliAppId(
            @NotBlank(message = "{validation.field.aliAppId.notBlank}") String aliAppId) {
        return Res.ok(alipayIsvAppService.existsAliAppId(aliAppId, null));
    }

    @PermCode(code = "view", nameCn = "支付宝服务商查看", nameEn = "Alipay ISV View")
    @Operation(summary = "支付宝应用ID是否已存在(排除自身)")
    @GetMapping("/exists-ali-app-id-not-id")
    public Result<Boolean> existsAliAppIdNotId(
            @NotBlank(message = "{validation.field.aliAppId.notBlank}") String aliAppId,
            @NotNull(message = "{validation.field.id.notNull}") Long id) {
        return Res.ok(alipayIsvAppService.existsAliAppId(aliAppId, id));
    }

    @PermCode(code = "add", nameCn = "支付宝服务商新增", nameEn = "Alipay ISV Add")
    @Operation(summary = "新增服务商应用")
    @PostMapping("/add")
    public Result<Void> add(@RequestBody @Validated(ValidationGroup.add.class) AlipayIsvAppParam param) {
        ValidationUtil.validateParam(param, ValidationGroup.add.class);
        alipayIsvAppService.add(param);
        return Res.ok();
    }

    @PermCode(code = "edit", nameCn = "支付宝服务商编辑", nameEn = "Alipay ISV Edit")
    @Operation(summary = "修改服务商应用")
    @PostMapping("/update")
    public Result<Void> update(@RequestBody @Validated(ValidationGroup.edit.class) AlipayIsvAppParam param) {
        ValidationUtil.validateParam(param, ValidationGroup.edit.class);
        alipayIsvAppService.update(param);
        return Res.ok();
    }

    @PermCode(code = "edit", nameCn = "支付宝服务商编辑", nameEn = "Alipay ISV Edit")
    @Operation(summary = "删除服务商应用")
    @PostMapping("/delete")
    public Result<Void> delete(@NotNull(message = "{validation.field.id.notNull}") Long id) {
        alipayIsvAppService.delete(id);
        return Res.ok();
    }

    @PermCode(code = "view", nameCn = "支付宝服务商查看", nameEn = "Alipay ISV View")
    @Operation(summary = "查询应用密钥配置")
    @GetMapping("/find-key-config-by-app-id")
    public Result<AlipayIsvAppKeyConfigResult> findKeyConfigByAppId(
            @NotNull(message = "{validation.field.id.notNull}") Long appId) {
        return Res.ok(alipayIsvAppKeyConfigService.findByAppId(appId));
    }

    @PermCode(code = "edit", nameCn = "支付宝服务商编辑", nameEn = "Alipay ISV Edit")
    @Operation(summary = "保存应用密钥配置")
    @PostMapping("/save-key-config")
    public Result<Void> saveKeyConfig(@RequestBody @Validated AlipayIsvAppKeyConfigParam param) {
        alipayIsvAppKeyConfigService.save(param);
        return Res.ok();
    }

    @PermCode(code = "view", nameCn = "支付宝服务商查看", nameEn = "Alipay ISV View")
    @Operation(summary = "查询应用授权认证配置")
    @GetMapping("/find-auth-config-by-app-id")
    public Result<AlipayIsvAppAuthConfigResult> findAuthConfigByAppId(
            @NotNull(message = "{validation.field.id.notNull}") Long appId) {
        return Res.ok(alipayIsvAppAuthConfigService.findByAppId(appId));
    }

    @PermCode(code = "edit", nameCn = "支付宝服务商编辑", nameEn = "Alipay ISV Edit")
    @Operation(summary = "保存应用授权认证配置")
    @PostMapping("/save-auth-config")
    public Result<Void> saveAuthConfig(@RequestBody @Validated AlipayIsvAppAuthConfigParam param) {
        alipayIsvAppAuthConfigService.save(param);
        return Res.ok();
    }
}
