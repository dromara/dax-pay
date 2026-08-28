package cn.daxpay.open.payment.admin.controller.wx;

import cn.daxpay.open.payment.wx.param.merchant.WxMchAppParam;
import cn.daxpay.open.payment.wx.result.merchant.WxMchAppResult;
import cn.daxpay.open.payment.wx.service.merchant.WxMchAppService;
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
import org.springframework.web.bind.annotation.*;

import java.util.List;

/// # 商户微信应用管理（运营端）
///
@PermCode(menuCode = PermCodes.Payment.Wx.MchApp.MENU)
@Validated
@Tag(name = "商户微信应用管理")
@RestController
@RequestMapping("/admin/wx/mch-app")
@RequiredArgsConstructor
public class WxMchAppController {

    private final WxMchAppService wxMchAppService;

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "按商户号查询微信应用列表")
    @GetMapping("/list-by-mch-no")
    public Result<List<WxMchAppResult>> listByMchNo(
            @NotBlank(message = "{validation.field.mchNo.notBlank}") String mchNo) {
        return Res.ok(wxMchAppService.listByMchNo(mchNo));
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "查询应用详情")
    @GetMapping("/find-by-id")
    public Result<WxMchAppResult> findById(
            @NotNull(message = "{validation.field.id.notNull}") Long id) {
        return Res.ok(wxMchAppService.findById(id));
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "微信应用AppId是否已存在")
    @GetMapping("/exists-wx-app-id")
    public Result<Boolean> existsWxAppId(
            @NotBlank(message = "{validation.field.mchNo.notBlank}") String mchNo,
            @NotBlank(message = "{validation.field.wxAppId.notBlank}") String wxAppId) {
        return Res.ok(wxMchAppService.existsWxAppId(mchNo, wxAppId, null));
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "微信应用AppId是否已存在(排除自身)")
    @GetMapping("/exists-wx-app-id-not-id")
    public Result<Boolean> existsWxAppIdNotId(
            @NotBlank(message = "{validation.field.mchNo.notBlank}") String mchNo,
            @NotBlank(message = "{validation.field.wxAppId.notBlank}") String wxAppId,
            @NotNull(message = "{validation.field.id.notNull}") Long id) {
        return Res.ok(wxMchAppService.existsWxAppId(mchNo, wxAppId, id));
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "新增商户微信应用")
    @PostMapping("/add")
    public Result<Void> add(@RequestBody @Validated(ValidationGroup.add.class) WxMchAppParam param) {
        // mchNo 必须来自 param（运营端无商户上下文）
        wxMchAppService.add(param);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "修改商户微信应用")
    @PostMapping("/update")
    public Result<Void> update(@RequestBody @Validated(ValidationGroup.edit.class) WxMchAppParam param) {
        wxMchAppService.update(param);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "删除商户微信应用")
    @PostMapping("/delete")
    public Result<Void> delete(@NotNull(message = "{validation.field.id.notNull}") Long id) {
        wxMchAppService.delete(id);
        return Res.ok();
    }

}
