package cn.daxpay.open.payment.app.admin.controller.merchant.info;

import cn.daxpay.open.payment.app.admin.service.merchant.info.AppAdminMerchantService;
import cn.daxpay.open.payment.merchant.param.info.MerchantInfoParam;
import cn.daxpay.open.payment.merchant.param.info.MerchantInfoQuery;
import cn.daxpay.open.payment.merchant.param.info.MerchantRegisterParam;
import cn.daxpay.open.payment.merchant.result.info.MerchantInfoResult;
import cn.daxpay.open.platform.core.annotation.PermCode;
import cn.daxpay.open.platform.core.code.PermCodes;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import cn.daxpay.open.platform.core.rest.result.Result;
import cn.daxpay.open.platform.core.validation.ValidationGroup;
import cn.daxpay.open.platform.iam.result.user.UserPasswordResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/// 运营移动端-商户配置
@PermCode(menuCode = PermCodes.Merchant.Info.MENU)
@Validated
@Tag(name = "运营移动端-商户配置")
@RestController
@RequestMapping("/app-admin/merchant")
@RequiredArgsConstructor
public class AppAdminMerchantController {

    private final AppAdminMerchantService merchantService;

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "新增商户")
    @PostMapping("/add")
    public Result<UserPasswordResult> add(@RequestBody @Validated(ValidationGroup.add.class) MerchantRegisterParam param) {
        // 未指定管理员密码时由后端生成随机初始密码, 响应中一次性返回明文供运营转告商户
        return Res.ok(merchantService.add(param));
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "修改商户")
    @PostMapping("/update")
    public Result<Void> update(@RequestBody @Validated(ValidationGroup.edit.class) MerchantInfoParam param) {
        merchantService.update(param);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "商户分页")
    @GetMapping("/page")
    public Result<PageResult<MerchantInfoResult>> page(PageParam pageParam, MerchantInfoQuery param) {
        return Res.ok(merchantService.page(pageParam, param));
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "根据id查询商户")
    @GetMapping("/get")
    public Result<MerchantInfoResult> findById(@NotNull(message = "{validation.field.id.notNull}") Long id) {
        return Res.ok(merchantService.findById(id));
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "根据商户号查询商户")
    @GetMapping("/get-by-mch-no")
    public Result<MerchantInfoResult> findByMchNo(@NotBlank(message = "{validation.field.mchNo.notBlank}") String mchNo) {
        return Res.ok(merchantService.findByMchNo(mchNo));
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "删除商户")
    @PostMapping("/delete")
    public Result<Void> delete(@NotNull(message = "{validation.field.id.notNull}") Long id) {
        merchantService.delete(id);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "启用商户")
    @PostMapping("/enable")
    public Result<Void> enable(@NotNull(message = "{validation.field.id.notNull}") Long id) {
        merchantService.enable(id);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "禁用商户")
    @PostMapping("/disable")
    public Result<Void> disable(@NotNull(message = "{validation.field.id.notNull}") Long id) {
        merchantService.disable(id);
        return Res.ok();
    }

    @Operation(summary = "商户下拉列表")
    @GetMapping("/dropdown")
    public Result<?> dropdown() {
        return Res.ok(merchantService.dropdown());
    }
}
