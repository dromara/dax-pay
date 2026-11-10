package cn.daxpay.open.platform.iam.controller.user;

import cn.daxpay.open.platform.core.annotation.OperateLog;
import cn.daxpay.open.platform.core.annotation.PermCode;
import cn.daxpay.open.platform.core.code.PermCodes;
import cn.daxpay.open.platform.core.enums.common.OperateLogType;
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
import cn.daxpay.open.platform.iam.result.user.UserPasswordResult;
import cn.daxpay.open.platform.iam.result.user.UserWholeInfoResult;
import cn.daxpay.open.platform.iam.service.user.UserAdminService;
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

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "根据用户id查询用户")
    @GetMapping("/get")
    public Result<UserWholeInfoResult> findById(@NotNull(message = "{validation.field.id.notNull}") Long id) {
        return Res.ok(userAdminService.findDetail(id));
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @OperateLog(title = "新增运营用户", businessType = OperateLogType.ADD, saveParam = true, maskParam = true)
    @Operation(summary = "添加用户")
    @PostMapping("/add")
    public Result<UserPasswordResult> add(@RequestBody @Validated(ValidationGroup.add.class) UserInfoParam userInfoParam) {
        // 未指定密码时由后端生成随机初始密码, 响应中一次性返回明文供管理员转告用户
        return Res.ok(userAdminService.add(userInfoParam));
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @OperateLog(title = "修改运营用户", businessType = OperateLogType.UPDATE, saveParam = true, maskParam = true)
    @Operation(summary = "修改用户")
    @PostMapping("/update")
    public Result<Void> update(@RequestBody @Validated(ValidationGroup.edit.class) UserInfoParam userInfoParam) {
        userAdminService.update(userInfoParam);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @OperateLog(title = "强制解绑运营用户邮箱", businessType = OperateLogType.UPDATE, saveParam = true)
    @Operation(summary = "强制解绑用户邮箱")
    @PostMapping("/unbind-email")
    public Result<Void> unbindEmail(@NotNull(message = "{validation.field.userId.notNull}") Long userId) {
        // 仅清空邮箱与验证状态, 不可指定新邮箱(邮箱变更只能由用户本人走绑定验证流程)
        userAdminService.unbindEmail(userId);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.RESET_PASSWORD)
    // 不落返回值: 响应含一次性明文初始密码
    @OperateLog(title = "重置运营用户密码", businessType = OperateLogType.UPDATE, saveParam = true)
    @Operation(summary = "重置密码")
    @PostMapping("/restart-password")
    public Result<UserPasswordResult> restartPassword(@RequestBody @Validated RestartPwdParam param) {
        // 由后端生成随机密码, 响应中一次性返回明文供管理员转告用户
        return Res.ok(userAdminService.restartPassword(param.getUserId()));
    }

    @PermCode(code = PermCodes.Action.RESET_PASSWORD)
    // 不落返回值: 响应含一次性明文初始密码列表
    @OperateLog(title = "批量重置运营用户密码", businessType = OperateLogType.UPDATE, saveParam = true)
    @Operation(summary = "批量重置密码")
    @PostMapping("/restart-password-batch")
    public Result<List<UserPasswordResult>> restartPasswordBatch(@RequestBody @Validated RestartPwdBatchParam param) {
        // 每个用户独立生成随机密码, 响应中一次性返回明文列表
        return Res.ok(userAdminService.restartPasswordBatch(param.getUserIds()));
    }

    @PermCode(code = PermCodes.Action.STATUS)
    @OperateLog(title = "封禁用户", businessType = OperateLogType.UPDATE, saveParam = true)
    @Operation(summary = "封禁用户")
    @PostMapping("/ban")
    public Result<Void> ban(@NotNull(message = "{validation.field.userId.notNull}") Long userId) {
        userAdminService.ban(userId);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.STATUS)
    @OperateLog(title = "批量封禁用户", businessType = OperateLogType.UPDATE, saveParam = true)
    @Operation(summary = "批量封禁用户")
    @PostMapping("/ban-batch")
    public Result<Void> banBatch(@RequestBody @Validated UserBatchParam param) {
        userAdminService.banBatch(param.getUserIds());
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.STATUS)
    @OperateLog(title = "解锁用户", businessType = OperateLogType.UPDATE, saveParam = true)
    @Operation(summary = "解锁用户")
    @PostMapping("/unlock")
    public Result<Void> unlock(@NotNull(message = "{validation.field.userId.notNull}") Long userId) {
        userAdminService.unlock(userId);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.STATUS)
    @OperateLog(title = "批量解锁用户", businessType = OperateLogType.UPDATE, saveParam = true)
    @Operation(summary = "批量解锁用户")
    @PostMapping("/unlock-batch")
    public Result<Void> unlockBatch(@RequestBody @Validated UserBatchParam param) {
        userAdminService.unlockBatch(param.getUserIds());
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.STATUS)
    @OperateLog(title = "锁定用户", businessType = OperateLogType.UPDATE, saveParam = true)
    @Operation(summary = "锁定用户")
    @PostMapping("/lock")
    public Result<Void> lock(@NotNull(message = "{validation.field.userId.notNull}") Long userId) {
        userAdminService.lock(userId);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.STATUS)
    @OperateLog(title = "批量锁定用户", businessType = OperateLogType.UPDATE, saveParam = true)
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

    @PermCode(code = PermCodes.Action.MANAGE)
    @OperateLog(title = "删除用户(注销)", businessType = OperateLogType.DELETE, saveParam = true)
    @Operation(summary = "删除用户(注销: 级联清理+脱敏+逻辑删, 挂靠商户的用户请先在对应商户的用户管理中删除)")
    @PostMapping("/delete")
    public Result<Void> delete(@NotNull(message = "{validation.field.userId.notNull}") Long userId) {
        // 注销式删除: 挂靠商户的用户被拒并引导去商户侧; 级联清理安全资产并脱敏, 事务提交后踢全部会话
        userAdminService.delete(userId);
        return Res.ok();
    }
}
