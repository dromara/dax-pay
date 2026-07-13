package cn.daxpay.open.channel.wechat.controller.isv;

import cn.daxpay.open.platform.core.annotation.PermCode;
import cn.daxpay.open.platform.core.code.PermCodes;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.result.Result;
import cn.daxpay.open.platform.core.util.ValidationUtil;
import cn.daxpay.open.platform.core.validation.ValidationGroup;
import cn.daxpay.open.channel.wechat.param.isv.WechatIsvAppParam;
import cn.daxpay.open.channel.wechat.param.isv.WechatIsvAppAuthConfigParam;
import cn.daxpay.open.channel.wechat.convert.isv.WechatIsvAppAuthConfigConvert;
import cn.daxpay.open.channel.wechat.result.isv.WechatIsvAppResult;
import cn.daxpay.open.channel.wechat.result.isv.WechatIsvAppAuthConfigResult;
import cn.daxpay.open.channel.wechat.service.isv.WechatIsvAppService;
import cn.daxpay.open.channel.wechat.service.isv.WechatIsvAppAuthConfigService;
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
@PermCode(menuCode = PermCodes.Payment.WechatIsv.MENU)
@Validated
@Tag(name = "微信服务商应用管理")
@RestController
@RequestMapping("/admin/wechat/isv-app")
@RequiredArgsConstructor
public class WechatIsvAppController {

    private final WechatIsvAppService wechatIsvAppService;
    private final WechatIsvAppAuthConfigService wechatIsvAppAuthConfigService;

    @PermCode(code = PermCodes.Action.VIEW, nameCn = "微信服务商查看", nameEn = "WeChat ISV View")
    @Operation(summary = "查询服务商应用列表")
    @GetMapping("/list-all")
    public Result<List<WechatIsvAppResult>> listAll() {
        return Res.ok(wechatIsvAppService.listAll());
    }

    @PermCode(code = PermCodes.Action.VIEW, nameCn = "微信服务商查看", nameEn = "WeChat ISV View")
    @Operation(summary = "查询应用详情")
    @GetMapping("/find-by-id")
    public Result<WechatIsvAppResult> findById(
            @NotNull(message = "{validation.field.id.notNull}") Long id) {
        return Res.ok(wechatIsvAppService.findById(id));
    }

    @PermCode(code = PermCodes.Action.VIEW, nameCn = "微信服务商查看", nameEn = "WeChat ISV View")
    @Operation(summary = "微信应用AppId是否已存在")
    @GetMapping("/exists-wx-app-id")
    public Result<Boolean> existsWxAppId(
            @NotBlank(message = "{validation.field.wxAppId.notBlank}") String wxAppId) {
        return Res.ok(wechatIsvAppService.existsWxAppId(wxAppId, null));
    }

    @PermCode(code = PermCodes.Action.VIEW, nameCn = "微信服务商查看", nameEn = "WeChat ISV View")
    @Operation(summary = "微信应用AppId是否已存在(排除自身)")
    @GetMapping("/exists-wx-app-id-not-id")
    public Result<Boolean> existsWxAppIdNotId(
            @NotBlank(message = "{validation.field.wxAppId.notBlank}") String wxAppId,
            @NotNull(message = "{validation.field.id.notNull}") Long id) {
        return Res.ok(wechatIsvAppService.existsWxAppId(wxAppId, id));
    }

    @PermCode(code = PermCodes.Action.MANAGE, nameCn = "微信服务商管理", nameEn = "WeChat ISV Manage")
    @Operation(summary = "新增服务商应用")
    @PostMapping("/add")
    public Result<Void> add(@RequestBody @Validated(ValidationGroup.add.class) WechatIsvAppParam param) {
        ValidationUtil.validateParam(param, ValidationGroup.add.class);
        wechatIsvAppService.add(param);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.MANAGE, nameCn = "微信服务商管理", nameEn = "WeChat ISV Manage")
    @Operation(summary = "修改服务商应用")
    @PostMapping("/update")
    public Result<Void> update(@RequestBody @Validated(ValidationGroup.edit.class) WechatIsvAppParam param) {
        ValidationUtil.validateParam(param, ValidationGroup.edit.class);
        wechatIsvAppService.update(param);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.MANAGE, nameCn = "微信服务商管理", nameEn = "WeChat ISV Manage")
    @Operation(summary = "删除服务商应用")
    @PostMapping("/delete")
    public Result<Void> delete(@NotNull(message = "{validation.field.id.notNull}") Long id) {
        wechatIsvAppService.delete(id);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.VIEW, nameCn = "微信服务商查看", nameEn = "WeChat ISV View")
    @Operation(summary = "查询应用授权认证配置")
    @GetMapping("/find-auth-config-by-app-id")
    public Result<WechatIsvAppAuthConfigResult> findAuthConfigByAppId(
            @NotNull(message = "{validation.field.wechatIsvAppId.notNull}") Long wechatIsvAppId) {
        var config = wechatIsvAppAuthConfigService.findByWechatIsvAppId(wechatIsvAppId);
        return Res.ok(WechatIsvAppAuthConfigConvert.CONVERT.toResult(config));
    }

    @PermCode(code = PermCodes.Action.MANAGE, nameCn = "微信服务商管理", nameEn = "WeChat ISV Manage")
    @Operation(summary = "保存应用授权认证配置")
    @PostMapping("/save-auth-config")
    public Result<Void> saveAuthConfig(@RequestBody @Validated WechatIsvAppAuthConfigParam param) {
        wechatIsvAppAuthConfigService.save(param);
        return Res.ok();
    }
}
