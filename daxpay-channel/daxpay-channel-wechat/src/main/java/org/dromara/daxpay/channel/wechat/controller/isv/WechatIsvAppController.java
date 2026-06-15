package org.dromara.daxpay.channel.wechat.controller.isv;

import org.dromara.daxpay.platform.core.annotation.PermCode;
import org.dromara.daxpay.platform.core.rest.Res;
import org.dromara.daxpay.platform.core.rest.result.Result;
import org.dromara.daxpay.platform.core.util.ValidationUtil;
import org.dromara.daxpay.platform.core.validation.ValidationGroup;
import org.dromara.daxpay.channel.wechat.param.isv.WechatIsvAppParam;
import org.dromara.daxpay.channel.wechat.param.isv.WechatIsvAppAuthConfigParam;
import org.dromara.daxpay.channel.wechat.convert.isv.WechatIsvAppAuthConfigConvert;
import org.dromara.daxpay.channel.wechat.result.isv.WechatIsvAppResult;
import org.dromara.daxpay.channel.wechat.result.isv.WechatIsvAppAuthConfigResult;
import org.dromara.daxpay.channel.wechat.service.isv.WechatIsvAppService;
import org.dromara.daxpay.channel.wechat.service.isv.WechatIsvAppAuthConfigService;
import cn.hutool.core.util.StrUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/// # 微信服务商应用管理
///
@PermCode(menuCode = "payment:wechat:isv")
@Validated
@Tag(name = "微信服务商应用管理")
@RestController
@RequestMapping("/admin/wechat/isv-app")
@RequiredArgsConstructor
public class WechatIsvAppController {

    private final WechatIsvAppService wechatIsvAppService;
    private final WechatIsvAppAuthConfigService wechatIsvAppAuthConfigService;

    @PermCode(code = "view", nameCn = "微信服务商查看", nameEn = "WeChat ISV View")
    @Operation(summary = "查询服务商应用列表")
    @GetMapping("/list-all")
    public Result<List<WechatIsvAppResult>> listAll() {
        return Res.ok(wechatIsvAppService.listAll());
    }

    @PermCode(code = "view", nameCn = "微信服务商查看", nameEn = "WeChat ISV View")
    @Operation(summary = "查询应用详情")
    @GetMapping("/find-by-id")
    public Result<WechatIsvAppResult> findById(
            @NotNull(message = "{validation.field.id.notNull}") Long id) {
        return Res.ok(wechatIsvAppService.findById(id));
    }

    @PermCode(code = "view", nameCn = "微信服务商查看", nameEn = "WeChat ISV View")
    @Operation(summary = "微信应用AppId是否已存在")
    @GetMapping("/exists-wx-app-id")
    public Result<Boolean> existsWxAppId(
            @NotBlank(message = "{validation.field.wxAppId.notBlank}") String wxAppId) {
        return Res.ok(wechatIsvAppService.existsWxAppId(wxAppId, null));
    }

    @PermCode(code = "view", nameCn = "微信服务商查看", nameEn = "WeChat ISV View")
    @Operation(summary = "微信应用AppId是否已存在(排除自身)")
    @GetMapping("/exists-wx-app-id-not-id")
    public Result<Boolean> existsWxAppIdNotId(
            @NotBlank(message = "{validation.field.wxAppId.notBlank}") String wxAppId,
            @NotNull(message = "{validation.field.id.notNull}") Long id) {
        return Res.ok(wechatIsvAppService.existsWxAppId(wxAppId, id));
    }

    @PermCode(code = "add", nameCn = "微信服务商新增", nameEn = "WeChat ISV Add")
    @Operation(summary = "新增服务商应用")
    @PostMapping("/add")
    public Result<Void> add(@RequestBody @Validated(ValidationGroup.add.class) WechatIsvAppParam param) {
        ValidationUtil.validateParam(param, ValidationGroup.add.class);
        wechatIsvAppService.add(param);
        return Res.ok();
    }

    @PermCode(code = "edit", nameCn = "微信服务商编辑", nameEn = "WeChat ISV Edit")
    @Operation(summary = "修改服务商应用")
    @PostMapping("/update")
    public Result<Void> update(@RequestBody @Validated(ValidationGroup.edit.class) WechatIsvAppParam param) {
        ValidationUtil.validateParam(param, ValidationGroup.edit.class);
        wechatIsvAppService.update(param);
        return Res.ok();
    }

    @PermCode(code = "edit", nameCn = "微信服务商编辑", nameEn = "WeChat ISV Edit")
    @Operation(summary = "删除服务商应用")
    @PostMapping("/delete")
    public Result<Void> delete(@NotNull(message = "{validation.field.id.notNull}") Long id) {
        wechatIsvAppService.delete(id);
        return Res.ok();
    }

    @PermCode(code = "view", nameCn = "微信服务商查看", nameEn = "WeChat ISV View")
    @Operation(summary = "查询应用授权认证配置")
    @GetMapping("/find-auth-config-by-app-id")
    public Result<WechatIsvAppAuthConfigResult> findAuthConfigByAppId(
            @NotNull(message = "{validation.field.id.notNull}") Long wechatIsvAppId) {
        var config = wechatIsvAppAuthConfigService.findByWechatIsvAppId(wechatIsvAppId);
        var result = WechatIsvAppAuthConfigConvert.CONVERT.toResult(config);
        result.setAppSecretConfigured(StrUtil.isNotBlank(config.getAppSecret()));
        return Res.ok(result);
    }

    @PermCode(code = "edit", nameCn = "微信服务商编辑", nameEn = "WeChat ISV Edit")
    @Operation(summary = "保存应用授权认证配置")
    @PostMapping("/save-auth-config")
    public Result<Void> saveAuthConfig(@RequestBody @Validated WechatIsvAppAuthConfigParam param) {
        wechatIsvAppAuthConfigService.save(param);
        return Res.ok();
    }
}
