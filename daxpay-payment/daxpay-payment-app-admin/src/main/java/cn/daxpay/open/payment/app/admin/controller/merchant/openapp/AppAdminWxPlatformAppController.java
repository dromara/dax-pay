package cn.daxpay.open.payment.app.admin.controller.merchant.openapp;

import cn.daxpay.open.payment.wx.param.platform.WxPlatformAppParam;
import cn.daxpay.open.payment.wx.result.platform.WxPlatformAppResult;
import cn.daxpay.open.payment.wx.service.platform.WxPlatformAppService;
import cn.daxpay.open.platform.core.annotation.PermCode;
import cn.daxpay.open.platform.core.code.PermCodes;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.result.Result;
import cn.daxpay.open.platform.core.validation.ValidationGroup;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/// 小程序管理端-平台微信应用
///
/// 镜像自 admin 版 `WxPlatformAppController`(路径 /admin/wx/platform-app), 端点语义与权限码完全一致,
/// 供小程序端支付应用(微信)平台维度的查询/新增/编辑/删除使用。
@PermCode(menuCode = PermCodes.Payment.Wx.PlatformApp.MENU)
@Validated
@Tag(name = "小程序管理端-平台微信应用")
@RestController
@RequestMapping("/app-admin/wx/platform-app")
@RequiredArgsConstructor
public class AppAdminWxPlatformAppController {

    private final WxPlatformAppService wxPlatformAppService;

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
        wxPlatformAppService.add(param);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "修改平台微信应用")
    @PostMapping("/update")
    public Result<Void> update(@RequestBody @Validated(ValidationGroup.edit.class) WxPlatformAppParam param) {
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
}
