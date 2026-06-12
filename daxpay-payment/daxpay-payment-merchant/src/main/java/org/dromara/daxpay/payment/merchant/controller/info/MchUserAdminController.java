package org.dromara.daxpay.payment.merchant.controller.info;

import org.dromara.daxpay.platform.core.annotation.OperateLog;
import org.dromara.daxpay.platform.core.enums.common.OperateLogType;
import org.dromara.daxpay.platform.core.rest.Res;
import org.dromara.daxpay.platform.core.rest.param.PageParam;
import org.dromara.daxpay.platform.core.rest.result.PageResult;
import org.dromara.daxpay.platform.core.rest.result.Result;
import org.dromara.daxpay.platform.core.validation.ValidationGroup;
import org.dromara.daxpay.payment.merchant.param.info.MerchantUserBatchParam;
import org.dromara.daxpay.payment.merchant.param.info.MerchantUserParam;
import org.dromara.daxpay.payment.merchant.param.info.MerchantUserQuery;
import org.dromara.daxpay.payment.merchant.param.info.MerchantUserResetPwdBatchParam;
import org.dromara.daxpay.payment.merchant.param.info.MerchantUserResetPwdParam;
import org.dromara.daxpay.platform.iam.result.user.UserInfoResult;
import org.dromara.daxpay.payment.merchant.result.info.MerchantUserResult;
import org.dromara.daxpay.payment.merchant.service.user.MerchantUserAdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/// # 商户用户管理
///
@Validated
@Tag(name = "商户用户管理")
@RestController
@RequestMapping("/merchant/user")
@RequiredArgsConstructor
public class MchUserAdminController {

    private final MerchantUserAdminService merchantUserAdminService;

    @Operation(summary = "商户用户分页查询")
    @GetMapping("/page")
    public Result<PageResult<MerchantUserResult>> page(PageParam pageParam, MerchantUserQuery query) {
        return Res.ok(merchantUserAdminService.page(pageParam, query));
    }

    @Operation(summary = "根据用户ID查询用户详情")
    @GetMapping("/get")
    public Result<UserInfoResult> findById(@NotNull(message = "{validation.field.id.notNull}") Long id) {
        return Res.ok(merchantUserAdminService.findById(id));
    }

    @Operation(summary = "添加商户用户")
    @PostMapping("/add")
    @OperateLog(title = "新增商户用户", businessType = OperateLogType.ADD, saveParam = true)
    public Result<Void> add(@RequestBody @Validated(ValidationGroup.add.class) MerchantUserParam param) {
        merchantUserAdminService.add(param);
        return Res.ok();
    }

    @Operation(summary = "修改商户用户")
    @PostMapping("/update")
    @OperateLog(title = "修改商户用户", businessType = OperateLogType.UPDATE, saveParam = true)
    public Result<Void> update(@RequestBody @Validated(ValidationGroup.edit.class) MerchantUserParam param) {
        merchantUserAdminService.update(param);
        return Res.ok();
    }

    @Operation(summary = "分配角色")
    @PostMapping("/assign-role")
    @OperateLog(title = "分配商户用户角色", businessType = OperateLogType.GRANT, saveParam = true)
    public Result<Void> assignRole(@NotNull(message = "{validation.field.userId.notNull}") Long userId,
                                    @NotNull(message = "{validation.field.roleId.notNull}") Long roleId) {
        merchantUserAdminService.assignRole(userId, roleId);
        return Res.ok();
    }

    @Operation(summary = "封禁商户用户")
    @PostMapping("/ban")
    @OperateLog(title = "封禁商户用户", businessType = OperateLogType.UPDATE, saveParam = true)
    public Result<Void> ban(@NotNull(message = "{validation.field.userId.notNull}") Long userId) {
        merchantUserAdminService.ban(userId);
        return Res.ok();
    }

    @Operation(summary = "批量封禁商户用户")
    @PostMapping("/ban-batch")
    @OperateLog(title = "批量封禁商户用户", businessType = OperateLogType.UPDATE, saveParam = true)
    public Result<Void> banBatch(@RequestBody @NotEmpty(message = "{validation.field.userIds.notEmpty}") List<Long> userIds) {
        merchantUserAdminService.banBatch(userIds);
        return Res.ok();
    }

    @Operation(summary = "解锁商户用户")
    @PostMapping("/unlock")
    @OperateLog(title = "解锁商户用户", businessType = OperateLogType.UPDATE, saveParam = true)
    public Result<Void> unlock(@NotNull(message = "{validation.field.userId.notNull}") Long userId) {
        merchantUserAdminService.unlock(userId);
        return Res.ok();
    }

    @Operation(summary = "批量解锁商户用户")
    @PostMapping("/unlock-batch")
    @OperateLog(title = "批量解锁商户用户", businessType = OperateLogType.UPDATE, saveParam = true)
    public Result<Void> unlockBatch(@RequestBody @NotEmpty(message = "{validation.field.userIds.notEmpty}") List<Long> userIds) {
        merchantUserAdminService.unlockBatch(userIds);
        return Res.ok();
    }

    @Operation(summary = "重置密码")
    @PostMapping("/restart-password")
    @OperateLog(title = "重置商户用户密码", businessType = OperateLogType.UPDATE, saveParam = true)
    public Result<Void> restartPassword(@RequestBody @Validated MerchantUserResetPwdParam param) {
        merchantUserAdminService.restartPassword(param.getUserId(), param.getNewPassword());
        return Res.ok();
    }

    @Operation(summary = "批量重置密码")
    @PostMapping("/restart-password-batch")
    @OperateLog(title = "批量重置商户用户密码", businessType = OperateLogType.UPDATE, saveParam = true)
    public Result<Void> restartPasswordBatch(@RequestBody @Validated MerchantUserResetPwdBatchParam param) {
        merchantUserAdminService.restartPasswordBatch(param.getUserIds(), param.getNewPassword());
        return Res.ok();
    }
}
