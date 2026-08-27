package cn.daxpay.open.platform.iam.controller.user;

import cn.daxpay.open.platform.core.annotation.PermCode;
import cn.daxpay.open.platform.core.code.PermCodes;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import cn.daxpay.open.platform.core.rest.result.Result;
import cn.daxpay.open.platform.core.validation.ValidationGroup;
import cn.daxpay.open.platform.iam.param.user.RestartPwdBatchParam;
import cn.daxpay.open.platform.iam.param.user.RestartPwdParam;
import cn.daxpay.open.platform.iam.param.user.UserBatchParam;
import cn.daxpay.open.platform.iam.param.user.UserInfoParam;
import cn.daxpay.open.platform.iam.param.user.UserInfoQuery;
import cn.daxpay.open.platform.iam.result.user.UserInfoResult;
import cn.daxpay.open.platform.iam.result.user.UserPasswordResult;
import cn.daxpay.open.platform.iam.result.user.UserWholeInfoResult;
import cn.daxpay.open.platform.iam.service.user.UserAdminService;
import cn.daxpay.open.platform.iam.service.user.UserQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@PermCode(menuCode = PermCodes.Iam.User.MENU)
@Validated
@Tag(name = "管理用户(管理员级别)")
@RestController
@RequestMapping("/user/admin")
@RequiredArgsConstructor
public class UserAdminController {
    private final UserAdminService userAdminService;

    private final UserQueryService userQueryService;

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "根据用户id查询用户")
    @GetMapping("/get")
    public Result<UserInfoResult> findById(@NotNull(message = "{validation.field.id.notNull}") Long id) {
        return Res.ok(userQueryService.findById(id));
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "添加用户")
    @PostMapping("/add")
    public Result<UserPasswordResult> add(@RequestBody @Validated(ValidationGroup.add.class) UserInfoParam userInfoParam) {
        // 未指定密码时由后端生成随机初始密码, 响应中一次性返回明文供管理员转告用户
        return Res.ok(userAdminService.add(userInfoParam));
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "修改用户")
    @PostMapping("/update")
    public Result<Void> update(@RequestBody @Validated(ValidationGroup.edit.class) UserInfoParam userInfoParam) {
        userAdminService.update(userInfoParam);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.RESET_PASSWORD)
    @Operation(summary = "重置密码")
    @PostMapping("/restart-password")
    public Result<UserPasswordResult> restartPassword(@RequestBody @Validated RestartPwdParam param) {
        // 未指定密码时由后端生成随机密码, 响应中一次性返回明文供管理员转告用户
        return Res.ok(userAdminService.restartPassword(param.getUserId(), param.getNewPassword()));
    }

    @PermCode(code = PermCodes.Action.RESET_PASSWORD)
    @Operation(summary = "批量重置密码")
    @PostMapping("/restart-password-batch")
    public Result<List<UserPasswordResult>> restartPasswordBatch(@RequestBody @Validated RestartPwdBatchParam param) {
        // 每个用户独立生成随机密码, 响应中一次性返回明文列表
        return Res.ok(userAdminService.restartPasswordBatch(param.getUserIds(), param.getNewPassword()));
    }

    @PermCode(code = PermCodes.Action.STATUS)
    @Operation(summary = "封禁用户")
    @PostMapping("/ban")
    public Result<Void> ban(@NotNull(message = "{validation.field.userId.notNull}") Long userId) {
        userAdminService.ban(userId);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.STATUS)
    @Operation(summary = "批量封禁用户")
    @PostMapping("/ban-batch")
    public Result<Void> banBatch(@RequestBody @Validated UserBatchParam param) {
        userAdminService.banBatch(param.getUserIds());
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.STATUS)
    @Operation(summary = "解锁用户")
    @PostMapping("/unlock")
    public Result<Void> unlock(@NotNull(message = "{validation.field.userId.notNull}") Long userId) {
        userAdminService.unlock(userId);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.STATUS)
    @Operation(summary = "批量解锁用户")
    @PostMapping("/unlock-batch")
    public Result<Void> unlockBatch(@RequestBody @Validated UserBatchParam param) {
        userAdminService.unlockBatch(param.getUserIds());
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.STATUS)
    @Operation(summary = "锁定用户")
    @PostMapping("/lock")
    public Result<Void> lock(@NotNull(message = "{validation.field.userId.notNull}") Long userId) {
        userAdminService.lock(userId);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.STATUS)
    @Operation(summary = "批量锁定用户")
    @PostMapping("/lock-batch")
    public Result<Void> lockBatch(@RequestBody @Validated UserBatchParam param) {
        userAdminService.lockBatch(param.getUserIds());
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "用户分页")
    @GetMapping("/page")
    public Result<PageResult<UserWholeInfoResult>> page(PageParam pageParam, UserInfoQuery query) {
        return Res.ok(userAdminService.page(pageParam, query));
    }
}
