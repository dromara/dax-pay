package cn.daxpay.open.payment.app.admin.controller.merchant.appinfo;

import cn.daxpay.open.payment.app.admin.service.merchant.appinfo.AppAdminMchAppInfoService;
import cn.daxpay.open.payment.merchant.param.appinfo.MchAppInfoParam;
import cn.daxpay.open.payment.merchant.param.appinfo.MchAppInfoQuery;
import cn.daxpay.open.payment.merchant.result.appinfo.MchAppInfoResult;
import cn.daxpay.open.platform.core.annotation.PermCode;
import cn.daxpay.open.platform.core.code.PermCodes;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import cn.daxpay.open.platform.core.rest.result.Result;
import cn.daxpay.open.platform.core.validation.ValidationGroup;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/// 运营移动端-商户应用配置
@PermCode(menuCode = PermCodes.Merchant.App.MENU)
@Validated
@Tag(name = "运营移动端-商户应用配置")
@RestController
@RequestMapping("/app-admin/merchant/app-info")
@RequiredArgsConstructor
public class AppAdminMchAppInfoController {

    private final AppAdminMchAppInfoService mchAppInfoService;

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "新增商户应用")
    @PostMapping("/add")
    public Result<Void> add(@RequestBody @Validated(ValidationGroup.add.class) MchAppInfoParam param) {
        mchAppInfoService.add(param);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "修改商户应用")
    @PostMapping("/update")
    public Result<Void> update(@RequestBody @Validated(ValidationGroup.edit.class) MchAppInfoParam param) {
        mchAppInfoService.update(param);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "商户应用分页")
    @GetMapping("/page")
    public Result<PageResult<MchAppInfoResult>> page(PageParam pageParam, MchAppInfoQuery query) {
        return Res.ok(mchAppInfoService.page(pageParam, query));
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "根据id查询商户应用")
    @GetMapping("/get")
    public Result<MchAppInfoResult> findById(@NotNull(message = "{validation.field.id.notNull}") Long id) {
        return Res.ok(mchAppInfoService.findById(id));
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "删除商户应用")
    @PostMapping("/delete")
    public Result<Void> delete(@NotNull(message = "{validation.field.id.notNull}") Long id) {
        mchAppInfoService.delete(id);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "设置默认商户应用")
    @PostMapping("/set-default")
    public Result<Void> setDefault(@NotNull(message = "{validation.field.id.notNull}") Long id) {
        mchAppInfoService.setDefault(id);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "取消默认商户应用")
    @PostMapping("/clear-default")
    public Result<Void> clearDefault(@NotNull(message = "{validation.field.id.notNull}") Long id) {
        mchAppInfoService.clearDefault(id);
        return Res.ok();
    }
}
