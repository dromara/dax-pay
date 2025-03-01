package cn.bootx.platform.iam.controller.user;

import cn.bootx.platform.core.annotation.OperateLog;
import cn.bootx.platform.core.annotation.RequestGroup;
import cn.bootx.platform.core.annotation.RequestPath;
import cn.bootx.platform.core.rest.Res;
import cn.bootx.platform.core.rest.param.PageParam;
import cn.bootx.platform.core.rest.result.PageResult;
import cn.bootx.platform.core.rest.result.Result;
import cn.bootx.platform.core.validation.ValidationGroup;
import cn.bootx.platform.iam.param.user.RestartPwdBatchParam;
import cn.bootx.platform.iam.param.user.RestartPwdParam;
import cn.bootx.platform.iam.param.user.UserInfoParam;
import cn.bootx.platform.iam.param.user.UserInfoQuery;
import cn.bootx.platform.iam.result.user.UserInfoResult;
import cn.bootx.platform.iam.result.user.UserWholeInfoResult;
import cn.bootx.platform.iam.service.user.UserAdminService;
import cn.bootx.platform.iam.service.user.UserQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author xxm
 * @since 2021/9/6
 */
@Validated
@Tag(name = "管理用户(管理员级别)")
@RestController
@RequestMapping("/user/admin")
@RequiredArgsConstructor
@RequestGroup(groupCode = "user", groupName = "用户管理", moduleCode = "iam")
public class UserAdminController {

    private final UserAdminService userAdminService;

    private final UserQueryService userQueryService;

    @RequestPath("根据用户id查询用户 ")
    @Operation(summary = "根据用户id查询用户")
    @GetMapping("/findById")
    public Result<UserInfoResult> findById(@NotNull(message = "主键不可为空") Long id) {
        return Res.ok(userQueryService.findById(id));
    }

    @RequestPath("添加用户")
    @Operation(summary = "添加用户")
    @PostMapping("/add")
    @OperateLog(title = "添加用户", businessType = OperateLog.BusinessType.ADD, saveParam = true)
    public Result<Void> add(@RequestBody @Validated(ValidationGroup.add.class) UserInfoParam userInfoParam) {
        userAdminService.add(userInfoParam);
        return Res.ok();
    }

    @RequestPath("修改用户")
    @Operation(summary = "修改用户")
    @PostMapping("/update")
    @OperateLog(title = "修改用户", businessType = OperateLog.BusinessType.UPDATE, saveParam = true)
    public Result<Void> update(@RequestBody @Validated(ValidationGroup.edit.class) UserInfoParam userInfoParam) {
        userAdminService.update(userInfoParam);
        return Res.ok();
    }

    @RequestPath("重置密码")
    @Operation(summary = "重置密码")
    @PostMapping("/restartPassword")
    @OperateLog(title = "重置密码", businessType = OperateLog.BusinessType.UPDATE, saveParam = true)
    public Result<Void> restartPassword(@RequestBody @Validated RestartPwdParam param) {
        userAdminService.restartPassword(param.getUserId(), param.getNewPassword());
        return Res.ok();
    }

    @RequestPath("批量重置密码")
    @Operation(summary = "批量重置密码")
    @PostMapping("/restartPasswordBatch")
    @OperateLog(title = "批量重置密码", businessType = OperateLog.BusinessType.UPDATE, saveParam = true)
    public Result<Void> restartPasswordBatch(@RequestBody @Validated RestartPwdBatchParam param) {
        userAdminService.restartPasswordBatch(param.getUserIds(), param.getNewPassword());
        return Res.ok();
    }

    @RequestPath("封禁用户")
    @Operation(summary = "封禁用户")
    @PostMapping("/ban")
    @OperateLog(title = "封禁用户", businessType = OperateLog.BusinessType.GRANT, saveParam = true)
    public Result<Void> ban(@NotNull(message = "用户不可为空") Long userId) {
        userAdminService.ban(userId);
        return Res.ok();
    }

    @RequestPath("批量封禁用户")
    @Operation(summary = "批量封禁用户")
    @PostMapping("/banBatch")
    @OperateLog(title = "批量封禁用户", businessType = OperateLog.BusinessType.GRANT, saveParam = true)
    public Result<Void> banBatch(@RequestBody @NotEmpty(message = "用户集合不可为空") List<Long> userIds) {
        userAdminService.banBatch(userIds);
        return Res.ok();
    }

    @RequestPath("解锁用户")
    @Operation(summary = "解锁用户")
    @PostMapping("/unlock")
    @OperateLog(title = "解锁用户", businessType = OperateLog.BusinessType.GRANT, saveParam = true)
    public Result<Void> unlock(@NotNull(message = "用户不可为空") Long userId) {
        userAdminService.unlock(userId);
        return Res.ok();
    }

    @RequestPath("批量解锁用户")
    @Operation(summary = "批量解锁用户")
    @PostMapping("/unlockBatch")
    @OperateLog(title = "批量解锁用户", businessType = OperateLog.BusinessType.GRANT, saveParam = true)
    public Result<Void> unlockBatch(@RequestBody @NotEmpty(message = "用户集合不可为空") List<Long> userIds) {
        userAdminService.unlockBatch(userIds);
        return Res.ok();
    }

    @RequestPath("用户分页")
    @Operation(summary = "用户分页")
    @GetMapping("/page")
    public Result<PageResult<UserWholeInfoResult>> page(PageParam pageParam, UserInfoQuery query) {
        return Res.ok(userAdminService.page(pageParam, query));
    }
}
