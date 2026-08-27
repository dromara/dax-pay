package cn.daxpay.open.payment.merchant.controller.store;

import cn.daxpay.open.payment.merchant.param.store.MchStoreInfoParam;
import cn.daxpay.open.payment.merchant.param.store.MchStoreInfoQuery;
import cn.daxpay.open.payment.merchant.result.store.MchStoreInfoResult;
import cn.daxpay.open.payment.merchant.service.store.MchStoreInfoService;
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

import java.util.List;

/// # 门店信息管理
///
@PermCode(menuCode = PermCodes.Merchant.Store.MENU)
@Validated
@Tag(name = "门店信息管理")
@RestController
@RequestMapping("/mch/store")
@RequiredArgsConstructor
public class MchStoreInfoController {
    private final MchStoreInfoService mchStoreInfoService;

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "新增门店")
    @PostMapping("/add")
    public Result<Void> add(@RequestBody @Validated(ValidationGroup.add.class) MchStoreInfoParam param) {
        mchStoreInfoService.add(param);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "修改门店")
    @PostMapping("/update")
    public Result<Void> update(@RequestBody @Validated(ValidationGroup.edit.class) MchStoreInfoParam param) {
        mchStoreInfoService.update(param);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "门店分页")
    @GetMapping("/page")
    public Result<PageResult<MchStoreInfoResult>> page(PageParam pageParam, MchStoreInfoQuery query) {
        return Res.ok(mchStoreInfoService.page(pageParam, query));
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "门店列表")
    @GetMapping("/list")
    public Result<List<MchStoreInfoResult>> list() {
        return Res.ok(mchStoreInfoService.list());
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "根据id查询门店")
    @GetMapping("/get")
    public Result<MchStoreInfoResult> findById(@NotNull(message = "{validation.field.id.notNull}") Long id) {
        return Res.ok(mchStoreInfoService.findById(id));
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "删除门店")
    @PostMapping("/delete")
    public Result<Void> delete(@NotNull(message = "{validation.field.id.notNull}") Long id) {
        mchStoreInfoService.delete(id);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "设为默认门店")
    @PostMapping("/set-default")
    public Result<Void> setDefault(@NotNull(message = "{validation.field.id.notNull}") Long id) {
        mchStoreInfoService.setDefault(id);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "取消默认门店")
    @PostMapping("/clear-default")
    public Result<Void> clearDefault(@NotNull(message = "{validation.field.id.notNull}") Long id) {
        mchStoreInfoService.clearDefault(id);
        return Res.ok();
    }
}
