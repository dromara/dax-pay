package cn.bootx.platform.iam.controller;

import cn.bootx.platform.common.core.annotation.OperateLog;
import cn.bootx.platform.common.core.rest.PageResult;
import cn.bootx.platform.common.core.rest.Res;
import cn.bootx.platform.common.core.rest.ResResult;
import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.iam.core.user.service.UserAdminService;
import cn.bootx.platform.iam.core.user.service.UserQueryService;
import cn.bootx.platform.iam.dto.user.UserInfoDto;
import cn.bootx.platform.iam.dto.user.UserInfoWhole;
import cn.bootx.platform.iam.param.user.UserInfoParam;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
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
public class UserAdminController {

    private final UserAdminService userAdminService;

    private final UserQueryService userQueryService;

    @Operation(summary = "根据用户id查询用户")
    @GetMapping("/findById")
    public ResResult<UserInfoDto> findById(Long id) {
        return Res.ok(userQueryService.findById(id));
    }

    @Operation(summary = "查询用户详情")
    @GetMapping("/getUserInfoWhole")
    public ResResult<UserInfoWhole> getUserInfoWhole(Long id) {
        return Res.ok(userAdminService.getUserInfoWhole(id));
    }

    @Operation(summary = "根据邮箱查询用户")
    @GetMapping("/getByEmail")
    public ResResult<UserInfoDto> getByEmail(String email) {
        return Res.ok(userQueryService.findByEmail(email));
    }

    @Operation(summary = "根据手机号查询用户")
    @GetMapping("/getByPhone")
    public ResResult<UserInfoDto> getByPhone(String phone) {
        return Res.ok(userQueryService.findByPhone(phone));
    }

    @Operation(summary = "添加用户")
    @PostMapping("/add")
    public ResResult<Void> add(@RequestBody UserInfoParam userInfoParam) {
        userAdminService.add(userInfoParam);
        return Res.ok();
    }

    @Operation(summary = "修改用户")
    @PostMapping("/update")
    public ResResult<Void> update(@RequestBody UserInfoParam userInfoParam) {
        userAdminService.update(userInfoParam);
        return Res.ok();
    }

    @Operation(summary = "重置密码")
    @OperateLog(title = "重置密码", businessType = OperateLog.BusinessType.UPDATE, saveParam = true)
    @PostMapping("/restartPassword")
    public ResResult<Void> restartPassword(@NotNull(message = "用户不可为空") Long userId,
            @NotBlank(message = "新密码不能为空") String newPassword) {
        userAdminService.restartPassword(userId, newPassword);
        return Res.ok();
    }

    @Operation(summary = "批量重置密码")
    @OperateLog(title = "批量重置密码", businessType = OperateLog.BusinessType.UPDATE, saveParam = true)
    @PostMapping("/restartPasswordBatch")
    public ResResult<Void> restartPasswordBatch(@NotEmpty(message = "用户不可为空") @RequestBody List<Long> userIds,
            @NotBlank(message = "新密码不能为空") String newPassword) {
        userAdminService.restartPasswordBatch(userIds, newPassword);
        return Res.ok();
    }

    @OperateLog(title = "封禁用户", businessType = OperateLog.BusinessType.UPDATE, saveParam = true)
    @Operation(summary = "封禁用户")
    @PostMapping("/ban")
    public ResResult<Void> ban(Long userId) {
        userAdminService.ban(userId);
        return Res.ok();
    }

    @OperateLog(title = "批量封禁用户", businessType = OperateLog.BusinessType.UPDATE, saveParam = true)
    @Operation(summary = "批量封禁用户")
    @PostMapping("/banBatch")
    public ResResult<Void> banBatch(@RequestBody @NotEmpty(message = "用户集合不可为空") List<Long> userIds) {
        userAdminService.banBatch(userIds);
        return Res.ok();
    }

    @OperateLog(title = "解锁用户", businessType = OperateLog.BusinessType.UPDATE, saveParam = true)
    @Operation(summary = "解锁用户")
    @PostMapping("/unlock")
    public ResResult<Void> unlock(@NotNull(message = "用户不可为空") Long userId) {
        userAdminService.unlock(userId);
        return Res.ok();
    }

    @OperateLog(title = "批量解锁用户", businessType = OperateLog.BusinessType.UPDATE, saveParam = true)
    @Operation(summary = "批量解锁用户")
    @PostMapping("/unlockBatch")
    public ResResult<Void> unlockBatch(@RequestBody @NotEmpty(message = "用户集合不可为空") List<Long> userIds) {
        userAdminService.unlockBatch(userIds);
        return Res.ok();
    }

    @Operation(summary = "分页")
    @GetMapping("/page")
    public ResResult<PageResult<UserInfoDto>> page(PageParam pageParam, UserInfoParam userInfoParam) {
        return Res.ok(userAdminService.page(pageParam, userInfoParam));
    }

}
