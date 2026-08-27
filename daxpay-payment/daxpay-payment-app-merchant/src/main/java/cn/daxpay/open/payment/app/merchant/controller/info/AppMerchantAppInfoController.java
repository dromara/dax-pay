package cn.daxpay.open.payment.app.merchant.controller.info;

import cn.daxpay.open.payment.app.merchant.service.info.AppMerchantAppInfoService;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/// # 商户应用配置(商户移动端)
///
/// 面向商户移动端的应用管理。业务编排委托 [AppMerchantAppInfoService]。
@PermCode(menuCode = PermCodes.Merchant.App.MENU)
@Validated
@Tag(name = "商户应用配置(商户移动端)")
@RestController
@RequestMapping("/app-mch/app-info")
@RequiredArgsConstructor
public class AppMerchantAppInfoController {

    private final AppMerchantAppInfoService appInfoService;

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "商户应用分页")
    @GetMapping("/page")
    public Result<PageResult<MchAppInfoResult>> page(PageParam pageParam, MchAppInfoQuery query) {
        return Res.ok(appInfoService.page(pageParam, query));
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "商户应用列表")
    @GetMapping("/list")
    public Result<List<MchAppInfoResult>> list() {
        return Res.ok(appInfoService.list());
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "根据id查询商户应用")
    @GetMapping("/get")
    public Result<MchAppInfoResult> findById(@NotNull(message = "{validation.field.id.notNull}") Long id) {
        return Res.ok(appInfoService.findById(id));
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "根据应用AppId获取应用详情")
    @GetMapping("/get-by-app-id")
    public Result<MchAppInfoResult> findByAppId(@NotNull(message = "{validation.field.appId.notBlank}") String appId) {
        return Res.ok(appInfoService.findByAppId(appId));
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "新增商户应用")
    @PostMapping("/add")
    public Result<Void> add(@RequestBody @Validated(ValidationGroup.add.class) MchAppInfoParam param) {
        appInfoService.add(param);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "修改商户应用")
    @PostMapping("/update")
    public Result<Void> update(@RequestBody @Validated(ValidationGroup.edit.class) MchAppInfoParam param) {
        appInfoService.update(param);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "删除商户应用")
    @PostMapping("/delete")
    public Result<Void> delete(@NotNull(message = "{validation.field.id.notNull}") Long id) {
        appInfoService.delete(id);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "设置默认商户应用")
    @PostMapping("/set-default")
    public Result<Void> setDefault(@NotNull(message = "{validation.field.id.notNull}") Long id) {
        appInfoService.setDefault(id);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "取消默认商户应用")
    @PostMapping("/clear-default")
    public Result<Void> clearDefault(@NotNull(message = "{validation.field.id.notNull}") Long id) {
        appInfoService.clearDefault(id);
        return Res.ok();
    }
}
