package cn.daxpay.open.payment.admin.controller.douyin;

import cn.daxpay.open.payment.douyin.convert.merchant.DyMchAppAuthConfigConvert;
import cn.daxpay.open.payment.douyin.param.merchant.DyMchAppAuthConfigParam;
import cn.daxpay.open.payment.douyin.param.merchant.DyMchAppParam;
import cn.daxpay.open.payment.douyin.result.merchant.DyMchAppAuthConfigResult;
import cn.daxpay.open.payment.douyin.result.merchant.DyMchAppResult;
import cn.daxpay.open.payment.douyin.service.merchant.DyMchAppAuthConfigService;
import cn.daxpay.open.payment.douyin.service.merchant.DyMchAppService;
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

/// # 商户抖音应用管理（运营端）
///
@PermCode(menuCode = PermCodes.Payment.Douyin.MchApp.MENU)
@Validated
@Tag(name = "商户抖音应用管理")
@RestController
@RequestMapping("/admin/douyin/mch-app")
@RequiredArgsConstructor
public class DyMchAppController {

    private final DyMchAppService dyMchAppService;
    private final DyMchAppAuthConfigService dyMchAppAuthConfigService;

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "按商户号查询抖音应用列表")
    @GetMapping("/list-by-mch-no")
    public Result<List<DyMchAppResult>> listByMchNo(
            @NotBlank(message = "{validation.field.mchNo.notBlank}") String mchNo) {
        return Res.ok(dyMchAppService.listByMchNo(mchNo));
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "查询应用详情")
    @GetMapping("/find-by-id")
    public Result<DyMchAppResult> findById(
            @NotNull(message = "{validation.field.id.notNull}") Long id) {
        return Res.ok(dyMchAppService.findById(id));
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "抖音应用AppId是否已存在")
    @GetMapping("/exists-douyin-app-id")
    public Result<Boolean> existsDouyinAppId(
            @NotBlank(message = "{validation.field.mchNo.notBlank}") String mchNo,
            @NotBlank(message = "{validation.field.douyinAppId.notBlank}") String douyinAppId) {
        return Res.ok(dyMchAppService.existsDouyinAppId(mchNo, douyinAppId, null));
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "抖音应用AppId是否已存在(排除自身)")
    @GetMapping("/exists-douyin-app-id-not-id")
    public Result<Boolean> existsDouyinAppIdNotId(
            @NotBlank(message = "{validation.field.mchNo.notBlank}") String mchNo,
            @NotBlank(message = "{validation.field.douyinAppId.notBlank}") String douyinAppId,
            @NotNull(message = "{validation.field.id.notNull}") Long id) {
        return Res.ok(dyMchAppService.existsDouyinAppId(mchNo, douyinAppId, id));
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "新增商户抖音应用")
    @PostMapping("/add")
    public Result<Void> add(@RequestBody @Validated(ValidationGroup.add.class) DyMchAppParam param) {
        ValidationUtil.validateParam(param, ValidationGroup.add.class);
        // mchNo 必须来自 param（运营端无商户上下文）
        dyMchAppService.add(param);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "修改商户抖音应用")
    @PostMapping("/update")
    public Result<Void> update(@RequestBody @Validated(ValidationGroup.edit.class) DyMchAppParam param) {
        ValidationUtil.validateParam(param, ValidationGroup.edit.class);
        dyMchAppService.update(param);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "删除商户抖音应用")
    @PostMapping("/delete")
    public Result<Void> delete(@NotNull(message = "{validation.field.id.notNull}") Long id) {
        dyMchAppService.delete(id);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "查询应用授权认证配置")
    @GetMapping("/find-auth-config-by-app-id")
    public Result<DyMchAppAuthConfigResult> findAuthConfigByAppId(
            @NotNull(message = "{validation.field.dyMchAppId.notNull}") Long dyMchAppId) {
        var config = dyMchAppAuthConfigService.findByDyMchAppId(dyMchAppId);
        return Res.ok(DyMchAppAuthConfigConvert.CONVERT.toResult(config));
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "保存应用授权认证配置")
    @PostMapping("/save-auth-config")
    public Result<Void> saveAuthConfig(@RequestBody @Validated DyMchAppAuthConfigParam param) {
        dyMchAppAuthConfigService.save(param);
        return Res.ok();
    }
}
