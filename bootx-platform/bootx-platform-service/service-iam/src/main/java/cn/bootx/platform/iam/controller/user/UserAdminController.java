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
import cn.bootx.platform.iam.service.service.UserAdminService;
import cn.bootx.platform.iam.service.service.UserQueryService;
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
    public Result<UserInfoResult> findById(Long id) {
        return Res.ok(userQueryService.findById(id));
    }

    @RequestPath("添加用户")
    @Operation(summary = "添加用户")
    @PostMapping("/add")
    public Result<Void> add(@RequestBody @Validated(ValidationGroup.add.class) UserInfoParam userInfoParam) {
        userAdminService.add(userInfoParam);
        return Res.ok();
    }

    @RequestPath("修改用户")
    @Operation(summary = "修改用户")
    @PostMapping("/update")
    public Result<Void> update(@RequestBody @Validated(ValidationGroup.edit.class) UserInfoParam userInfoParam) {
        userAdminService.update(userInfoParam);
        return Res.ok();
    }

    @RequestPath("重置密码")
    @Operation(summary = "重置密码")
    @OperateLog(title = "重置密码", businessType = OperateLog.BusinessType.UPDATE, saveParam = true)
    @PostMapping("/restartPassword")
    public Result<Void> restartPassword(@RequestBody @Validated RestartPwdParam param) {
        userAdminService.restartPassword(param.getUserId(), param.getNewPassword());
        return Res.ok();
    }

    @RequestPath("批量重置密码")
    @Operation(summary = "批量重置密码")
    @OperateLog(title = "批量重置密码", businessType = OperateLog.BusinessType.UPDATE, saveParam = true)
    @PostMapping("/restartPasswordBatch")
    public Result<Void> restartPasswordBatch(@RequestBody @Validated RestartPwdBatchParam param) {
        userAdminService.restartPasswordBatch(param.getUserIds(), param.getNewPassword());
        return Res.ok();
    }

    @RequestPath("封禁用户")
    @OperateLog(title = "封禁用户", businessType = OperateLog.BusinessType.UPDATE, saveParam = true)
    @Operation(summary = "封禁用户")
    @PostMapping("/ban")
    public Result<Void> ban(Long userId) {
        userAdminService.ban(userId);
        return Res.ok();
    }

    @RequestPath("批量封禁用户")
    @OperateLog(title = "批量封禁用户", businessType = OperateLog.BusinessType.UPDATE, saveParam = true)
    @Operation(summary = "批量封禁用户")
    @PostMapping("/banBatch")
    public Result<Void> banBatch(@RequestBody @NotEmpty(message = "用户集合不可为空") List<Long> userIds) {
        userAdminService.banBatch(userIds);
        return Res.ok();
    }

    @RequestPath("解锁用户")
    @OperateLog(title = "解锁用户", businessType = OperateLog.BusinessType.UPDATE, saveParam = true)
    @Operation(summary = "解锁用户")
    @PostMapping("/unlock")
    public Result<Void> unlock(@NotNull(message = "用户不可为空") Long userId) {
        userAdminService.unlock(userId);
        return Res.ok();
    }

    @RequestPath("批量解锁用户")
    @OperateLog(title = "批量解锁用户", businessType = OperateLog.BusinessType.UPDATE, saveParam = true)
    @Operation(summary = "批量解锁用户")
    @PostMapping("/unlockBatch")
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
