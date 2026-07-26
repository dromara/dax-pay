package cn.daxpay.open.payment.app.merchant.controller.store;

import cn.daxpay.open.payment.app.merchant.service.store.AppMerchantStoreInfoService;
import cn.daxpay.open.payment.merchant.param.store.MchStoreInfoParam;
import cn.daxpay.open.payment.merchant.param.store.MchStoreInfoQuery;
import cn.daxpay.open.payment.merchant.result.store.MchStoreInfoResult;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/// # 门店信息管理(商户移动端)
///
/// 面向商户移动端的门店管理。业务编排委托 [AppMerchantStoreInfoService]。
@Validated
@Tag(name = "门店信息管理(商户移动端)")
@RestController
@RequestMapping("/app-merchant/store")
@RequiredArgsConstructor
public class AppMerchantStoreInfoController {

    private final AppMerchantStoreInfoService storeInfoService;

    @Operation(summary = "门店分页")
    @GetMapping("/page")
    public Result<PageResult<MchStoreInfoResult>> page(PageParam pageParam, MchStoreInfoQuery query) {
        return Res.ok(storeInfoService.page(pageParam, query));
    }

    @Operation(summary = "根据id查询门店")
    @GetMapping("/get")
    public Result<MchStoreInfoResult> findById(@NotNull(message = "{validation.field.id.notNull}") Long id) {
        return Res.ok(storeInfoService.findById(id));
    }

    @Operation(summary = "新增门店")
    @PostMapping("/add")
    public Result<Void> add(@RequestBody @Validated(ValidationGroup.add.class) MchStoreInfoParam param) {
        storeInfoService.add(param);
        return Res.ok();
    }

    @Operation(summary = "修改门店")
    @PostMapping("/update")
    public Result<Void> update(@RequestBody @Validated(ValidationGroup.edit.class) MchStoreInfoParam param) {
        storeInfoService.update(param);
        return Res.ok();
    }

    @Operation(summary = "删除门店")
    @PostMapping("/delete")
    public Result<Void> delete(@NotNull(message = "{validation.field.id.notNull}") Long id) {
        storeInfoService.delete(id);
        return Res.ok();
    }
}
