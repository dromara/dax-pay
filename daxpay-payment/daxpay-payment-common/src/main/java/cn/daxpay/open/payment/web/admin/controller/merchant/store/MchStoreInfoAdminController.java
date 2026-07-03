package cn.daxpay.open.payment.web.admin.controller.merchant.store;

import cn.daxpay.open.payment.merchant.param.store.MchStoreInfoParam;
import cn.daxpay.open.payment.merchant.param.store.MchStoreInfoQuery;
import cn.daxpay.open.payment.merchant.result.store.MchStoreInfoResult;
import cn.daxpay.open.payment.merchant.service.store.MchStoreInfoService;
import cn.daxpay.open.platform.core.annotation.PermCode;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import cn.daxpay.open.platform.core.rest.result.Result;
import cn.daxpay.open.platform.core.util.ValidationUtil;
import cn.daxpay.open.platform.core.validation.ValidationGroup;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/// 门店信息管理(管理端)
@PermCode(menuCode = "merchant:store")
@Validated
@Tag(name = "门店信息管理(管理端)")
@RestController
@RequestMapping("/admin/mch/store")
@RequiredArgsConstructor
public class MchStoreInfoAdminController {
    private final MchStoreInfoService mchStoreInfoService;

    @PermCode(code = "manage", nameCn = "门店管理", nameEn = "Store Manage")
    @Operation(summary = "新增门店")
    @PostMapping("/add")
    public Result<Void> add(@RequestBody @Validated(ValidationGroup.add.class) MchStoreInfoParam param) {
        ValidationUtil.validateParam(param, ValidationGroup.add.class);
        mchStoreInfoService.add(param);
        return Res.ok();
    }

    @PermCode(code = "manage", nameCn = "门店管理", nameEn = "Store Manage")
    @Operation(summary = "修改门店")
    @PostMapping("/update")
    public Result<Void> update(@RequestBody @Validated(ValidationGroup.edit.class) MchStoreInfoParam param) {
        ValidationUtil.validateParam(param, ValidationGroup.edit.class);
        mchStoreInfoService.update(param);
        return Res.ok();
    }

    @PermCode(code = "view", nameCn = "门店查看", nameEn = "Store View")
    @Operation(summary = "门店分页")
    @GetMapping("/page")
    public Result<PageResult<MchStoreInfoResult>> page(PageParam pageParam, MchStoreInfoQuery query) {
        return Res.ok(mchStoreInfoService.page(pageParam, query));
    }

    @PermCode(code = "view", nameCn = "门店查看", nameEn = "Store View")
    @Operation(summary = "根据id查询门店")
    @GetMapping("/get")
    public Result<MchStoreInfoResult> findById(@NotNull(message = "{validation.field.id.notNull}") Long id) {
        return Res.ok(mchStoreInfoService.findById(id));
    }

    @PermCode(code = "manage", nameCn = "门店管理", nameEn = "Store Manage")
    @Operation(summary = "删除门店")
    @PostMapping("/delete")
    public Result<Void> delete(@NotNull(message = "{validation.field.id.notNull}") Long id) {
        mchStoreInfoService.delete(id);
        return Res.ok();
    }
}
