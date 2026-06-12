package org.dromara.daxpay.payment.admin.controller.merchant.appinfo;

import org.dromara.daxpay.platform.core.annotation.PermCode;
import org.dromara.daxpay.platform.core.rest.Res;
import org.dromara.daxpay.platform.core.rest.param.PageParam;
import org.dromara.daxpay.platform.core.rest.result.PageResult;
import org.dromara.daxpay.platform.core.rest.result.Result;
import org.dromara.daxpay.platform.core.util.ValidationUtil;
import org.dromara.daxpay.platform.core.validation.ValidationGroup;
import org.dromara.daxpay.payment.merchant.param.appinfo.MchAppInfoParam;
import org.dromara.daxpay.payment.merchant.param.appinfo.MchAppInfoQuery;
import org.dromara.daxpay.payment.merchant.result.appinfo.MchAppInfoResult;
import org.dromara.daxpay.payment.merchant.service.appinfo.MchAppInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/// 商户应用配置(管理)
@PermCode(menuCode = "payment:merchant")
@Validated
@Tag(name = "商户应用配置(管理)")
@RestController
@RequestMapping("/admin/mch/app-info")
@RequiredArgsConstructor
public class MchAppInfoAdminController {
    private final MchAppInfoService mchAppInfoService;

    @PermCode(code = "add", nameCn = "商户新增", nameEn = "Merchant Add")
    @Operation(summary = "新增商户应用")
    @PostMapping("/add")
    public Result<Void> add(@RequestBody @Validated(ValidationGroup.add.class) MchAppInfoParam param){
        ValidationUtil.validateParam(param, ValidationGroup.add.class);
        mchAppInfoService.add(param);
        return Res.ok();
    }

    @PermCode(code = "edit", nameCn = "商户编辑", nameEn = "Merchant Edit")
    @Operation(summary = "修改商户应用")
    @PostMapping("/update")
    public Result<Void> update(@RequestBody @Validated(ValidationGroup.edit.class) MchAppInfoParam param){
        ValidationUtil.validateParam(param, ValidationGroup.edit.class);
        mchAppInfoService.update(param);
        return Res.ok();
    }

    @PermCode(code = "view", nameCn = "商户查看", nameEn = "Merchant View")
    @Operation(summary = "商户应用分页")
    @GetMapping("/page")
    public Result<PageResult<MchAppInfoResult>> page(PageParam pageParam, MchAppInfoQuery query){
        return Res.ok(mchAppInfoService.page(pageParam, query));
    }

    @PermCode(code = "view", nameCn = "商户查看", nameEn = "Merchant View")
    @Operation(summary = "根据id查询商户应用")
    @GetMapping("/get")
    public Result<MchAppInfoResult> findById(@NotNull(message = "{validation.field.id.notNull}")Long id){
        return Res.ok(mchAppInfoService.findById(id));
    }

    @PermCode(code = "delete", nameCn = "商户删除", nameEn = "Merchant Delete")
    @Operation(summary = "删除商户应用")
    @PostMapping("/delete")
    public Result<Void> delete(@NotNull(message = "{validation.field.id.notNull}") Long id){
        mchAppInfoService.delete(id);
        return Res.ok();
    }

    @PermCode(code = "edit", nameCn = "商户编辑", nameEn = "Merchant Edit")
    @Operation(summary = "设置默认商户应用")
    @PostMapping("/set-default")
    public Result<Void> setDefault(@NotNull(message = "{validation.field.id.notNull}") Long id){
        mchAppInfoService.setDefault(id);
        return Res.ok();
    }

    @PermCode(code = "edit", nameCn = "商户编辑", nameEn = "Merchant Edit")
    @Operation(summary = "取消默认商户应用")
    @PostMapping("/clear-default")
    public Result<Void> clearDefault(@NotNull(message = "{validation.field.id.notNull}") Long id){
        mchAppInfoService.clearDefault(id);
        return Res.ok();
    }
}
