package cn.daxpay.open.payment.admin.controller.wx;

import cn.daxpay.open.payment.wx.convert.WxPlatformAppAuthConfigConvert;
import cn.daxpay.open.payment.wx.param.WxPlatformAppAuthConfigParam;
import cn.daxpay.open.payment.wx.param.WxPlatformAppParam;
import cn.daxpay.open.payment.wx.result.WxPlatformAppAuthConfigResult;
import cn.daxpay.open.payment.wx.result.WxPlatformAppResult;
import cn.daxpay.open.payment.wx.service.WxPlatformAppAuthConfigService;
import cn.daxpay.open.payment.wx.service.WxPlatformAppService;
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

/// # 平台微信应用管理（运营端）
///
@PermCode(menuCode = PermCodes.Payment.Wx.PlatformApp.MENU)
@Validated
@Tag(name = "平台微信应用管理")
@RestController
@RequestMapping("/admin/wx/platform-app")
@RequiredArgsConstructor
public class WxPlatformAppController {

    private final WxPlatformAppService wxPlatformAppService;
    private final WxPlatformAppAuthConfigService wxPlatformAppAuthConfigService;

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "查询平台微信应用列表")
    @GetMapping("/list-all")
    public Result<List<WxPlatformAppResult>> listAll() {
        return Res.ok(wxPlatformAppService.listAll());
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "查询应用详情")
    @GetMapping("/find-by-id")
    public Result<WxPlatformAppResult> findById(
            @NotNull(message = "{validation.field.id.notNull}") Long id) {
        return Res.ok(wxPlatformAppService.findById(id));
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "微信应用AppId是否已存在")
    @GetMapping("/exists-wx-app-id")
    public Result<Boolean> existsWxAppId(
            @NotBlank(message = "{validation.field.wxAppId.notBlank}") String wxAppId) {
        return Res.ok(wxPlatformAppService.existsWxAppId(wxAppId, null));
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "微信应用AppId是否已存在(排除自身)")
    @GetMapping("/exists-wx-app-id-not-id")
    public Result<Boolean> existsWxAppIdNotId(
            @NotBlank(message = "{validation.field.wxAppId.notBlank}") String wxAppId,
            @NotNull(message = "{validation.field.id.notNull}") Long id) {
        return Res.ok(wxPlatformAppService.existsWxAppId(wxAppId, id));
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "新增平台微信应用")
    @PostMapping("/add")
    public Result<Void> add(@RequestBody @Validated(ValidationGroup.add.class) WxPlatformAppParam param) {
        ValidationUtil.validateParam(param, ValidationGroup.add.class);
        wxPlatformAppService.add(param);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "修改平台微信应用")
    @PostMapping("/update")
    public Result<Void> update(@RequestBody @Validated(ValidationGroup.edit.class) WxPlatformAppParam param) {
        ValidationUtil.validateParam(param, ValidationGroup.edit.class);
        wxPlatformAppService.update(param);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "删除平台微信应用")
    @PostMapping("/delete")
    public Result<Void> delete(@NotNull(message = "{validation.field.id.notNull}") Long id) {
        wxPlatformAppService.delete(id);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "查询应用授权认证配置")
    @GetMapping("/find-auth-config-by-app-id")
    public Result<WxPlatformAppAuthConfigResult> findAuthConfigByAppId(
            @NotNull(message = "{validation.field.wxPlatformAppId.notNull}") Long wxPlatformAppId) {
        var config = wxPlatformAppAuthConfigService.findByWxPlatformAppId(wxPlatformAppId);
        return Res.ok(WxPlatformAppAuthConfigConvert.CONVERT.toResult(config));
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "保存应用授权认证配置")
    @PostMapping("/save-auth-config")
    public Result<Void> saveAuthConfig(@RequestBody @Validated WxPlatformAppAuthConfigParam param) {
        wxPlatformAppAuthConfigService.save(param);
        return Res.ok();
    }
}
