package org.dromara.daxpay.platform.iam.controller.user;

import org.dromara.daxpay.platform.core.annotation.PermCode;
import org.dromara.daxpay.platform.core.rest.Res;
import org.dromara.daxpay.platform.core.rest.param.PageParam;
import org.dromara.daxpay.platform.core.rest.result.PageResult;
import org.dromara.daxpay.platform.core.rest.result.Result;
import org.dromara.daxpay.platform.core.validation.ValidationGroup;
import org.dromara.daxpay.platform.iam.param.user.RestartPwdBatchParam;
import org.dromara.daxpay.platform.iam.param.user.RestartPwdParam;
import org.dromara.daxpay.platform.iam.param.user.UserBatchParam;
import org.dromara.daxpay.platform.iam.param.user.UserInfoParam;
import org.dromara.daxpay.platform.iam.param.user.UserInfoQuery;
import org.dromara.daxpay.platform.iam.result.user.UserInfoResult;
import org.dromara.daxpay.platform.iam.result.user.UserWholeInfoResult;
import org.dromara.daxpay.platform.iam.service.user.UserAdminService;
import org.dromara.daxpay.platform.iam.service.user.UserQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@PermCode(menuCode = "iam:user:manager")
@Validated
@Tag(name = "管理用户(管理员级别)")
@RestController
@RequestMapping("/user/admin")
@RequiredArgsConstructor
public class UserAdminController {

    private final UserAdminService userAdminService;

    private final UserQueryService userQueryService;

    @PermCode(code = "view", nameCn = "用户查看", nameEn = "User View")
    @Operation(summary = "根据用户id查询用户")
    @GetMapping("/get")
    public Result<UserInfoResult> findById(@NotNull(message = "{validation.field.id.notNull}") Long id) {
        return Res.ok(userQueryService.findById(id));
    }

    @PermCode(code = "add", nameCn = "用户新增", nameEn = "User Add")
    @Operation(summary = "添加用户")
    @PostMapping("/add")
    public Result<Void> add(@RequestBody @Validated(ValidationGroup.add.class) UserInfoParam userInfoParam) {
        userAdminService.add(userInfoParam);
        return Res.ok();
    }

    @PermCode(code = "edit", nameCn = "用户编辑", nameEn = "User Edit")
    @Operation(summary = "修改用户")
    @PostMapping("/update")
    public Result<Void> update(@RequestBody @Validated(ValidationGroup.edit.class) UserInfoParam userInfoParam) {
        userAdminService.update(userInfoParam);
        return Res.ok();
    }

    @PermCode(code = "resetPassword", nameCn = "重置密码", nameEn = "Reset Password")
    @Operation(summary = "重置密码")
    @PostMapping("/restart-password")
    public Result<Void> restartPassword(@RequestBody @Validated RestartPwdParam param) {
        userAdminService.restartPassword(param.getUserId(), param.getNewPassword());
        return Res.ok();
    }

    @PermCode(code = "resetPassword", nameCn = "重置密码", nameEn = "Reset Password")
    @Operation(summary = "批量重置密码")
    @PostMapping("/restart-password-batch")
    public Result<Void> restartPasswordBatch(@RequestBody @Validated RestartPwdBatchParam param) {
        userAdminService.restartPasswordBatch(param.getUserIds(), param.getNewPassword());
        return Res.ok();
    }

    @PermCode(code = "status", nameCn = "用户状态管理", nameEn = "User Status Manage")
    @Operation(summary = "封禁用户")
    @PostMapping("/ban")
    public Result<Void> ban(@NotNull(message = "{validation.field.userId.notNull}") Long userId) {
        userAdminService.ban(userId);
        return Res.ok();
    }

    @PermCode(code = "status", nameCn = "用户状态管理", nameEn = "User Status Manage")
    @Operation(summary = "批量封禁用户")
    @PostMapping("/ban-batch")
    public Result<Void> banBatch(@RequestBody @Validated UserBatchParam param) {
        userAdminService.banBatch(param.getUserIds());
        return Res.ok();
    }

    @PermCode(code = "status", nameCn = "用户状态管理", nameEn = "User Status Manage")
    @Operation(summary = "解锁用户")
    @PostMapping("/unlock")
    public Result<Void> unlock(@NotNull(message = "{validation.field.userId.notNull}") Long userId) {
        userAdminService.unlock(userId);
        return Res.ok();
    }

    @PermCode(code = "status", nameCn = "用户状态管理", nameEn = "User Status Manage")
    @Operation(summary = "批量解锁用户")
    @PostMapping("/unlock-batch")
    public Result<Void> unlockBatch(@RequestBody @Validated UserBatchParam param) {
        userAdminService.unlockBatch(param.getUserIds());
        return Res.ok();
    }

    @PermCode(code = "status", nameCn = "用户状态管理", nameEn = "User Status Manage")
    @Operation(summary = "锁定用户")
    @PostMapping("/lock")
    public Result<Void> lock(@NotNull(message = "{validation.field.userId.notNull}") Long userId) {
        userAdminService.lock(userId);
        return Res.ok();
    }

    @PermCode(code = "status", nameCn = "用户状态管理", nameEn = "User Status Manage")
    @Operation(summary = "批量锁定用户")
    @PostMapping("/lock-batch")
    public Result<Void> lockBatch(@RequestBody @Validated UserBatchParam param) {
        userAdminService.lockBatch(param.getUserIds());
        return Res.ok();
    }

    @PermCode(code = "view", nameCn = "用户查看", nameEn = "User View")
    @Operation(summary = "用户分页")
    @GetMapping("/page")
    public Result<PageResult<UserWholeInfoResult>> page(PageParam pageParam, UserInfoQuery query) {
        return Res.ok(userAdminService.page(pageParam, query));
    }
}
