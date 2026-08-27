package cn.daxpay.open.platform.iam.controller.appadmin;

import cn.daxpay.open.platform.core.annotation.PermCode;
import cn.daxpay.open.platform.core.code.PermCodes;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import cn.daxpay.open.platform.core.rest.result.Result;
import cn.daxpay.open.platform.core.validation.ValidationGroup;
import cn.daxpay.open.platform.iam.param.upms.UserRoleParam;
import cn.daxpay.open.platform.iam.param.user.RestartPwdParam;
import cn.daxpay.open.platform.iam.param.user.UserInfoParam;
import cn.daxpay.open.platform.iam.param.user.UserInfoQuery;
import cn.daxpay.open.platform.iam.result.user.UserInfoResult;
import cn.daxpay.open.platform.iam.result.user.UserPasswordResult;
import cn.daxpay.open.platform.iam.result.user.UserWholeInfoResult;
import cn.daxpay.open.platform.iam.service.upms.UserRoleService;
import cn.daxpay.open.platform.iam.service.user.UserAdminService;
import cn.daxpay.open.platform.iam.service.user.UserQueryService;
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

/// # 用户管理(小程序管理端)
///
/// 小程序管理端的镜像接口, 与 [UserAdminController] / [UserRoleController] 同权限码同 Service,
/// 仅路径前缀不同。批量接口(ban-batch / unlock-batch / restart-password-batch)不镜像。
@PermCode(menuCode = PermCodes.Iam.User.MENU)
@Validated
@Tag(name = "用户管理(小程序管理端)")
@RestController
@RequestMapping("/app-admin/iam/user")
@RequiredArgsConstructor
public class AppAdminIamUserController {
    private final UserAdminService userAdminService;

    private final UserQueryService userQueryService;

    private final UserRoleService userRoleService;

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

    @PermCode(code = PermCodes.Action.STATUS)
    @Operation(summary = "封禁用户")
    @PostMapping("/ban")
    public Result<Void> ban(@NotNull(message = "{validation.field.userId.notNull}") Long userId) {
        userAdminService.ban(userId);
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
    @Operation(summary = "锁定用户")
    @PostMapping("/lock")
    public Result<Void> lock(@NotNull(message = "{validation.field.userId.notNull}") Long userId) {
        userAdminService.lock(userId);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "用户分页")
    @GetMapping("/page")
    public Result<PageResult<UserWholeInfoResult>> page(PageParam pageParam, UserInfoQuery query) {
        return Res.ok(userAdminService.page(pageParam, query));
    }

    @PermCode(code = PermCodes.Action.ASSIGN_ROLE)
    @Operation(summary = "给用户分配角色")
    @PostMapping("/assign-role")
    public Result<Void> saveAssign(@Validated @RequestBody UserRoleParam param) {
        userRoleService.saveAssign(param.getUserId(), param.getRoleId(), false);
        return Res.ok();
    }
}