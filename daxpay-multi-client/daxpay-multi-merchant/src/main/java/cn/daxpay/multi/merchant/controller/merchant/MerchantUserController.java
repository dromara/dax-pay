package cn.daxpay.multi.merchant.controller.merchant;

import cn.bootx.platform.core.annotation.OperateLog;
import cn.bootx.platform.core.annotation.RequestGroup;
import cn.bootx.platform.core.annotation.RequestPath;
import cn.bootx.platform.core.rest.Res;
import cn.bootx.platform.core.rest.param.PageParam;
import cn.bootx.platform.core.rest.result.PageResult;
import cn.bootx.platform.core.rest.result.Result;
import cn.bootx.platform.core.validation.ValidationGroup;
import cn.bootx.platform.iam.param.upms.UserRoleBatchParam;
import cn.bootx.platform.iam.param.upms.UserRoleParam;
import cn.bootx.platform.iam.param.user.RestartPwdBatchParam;
import cn.bootx.platform.iam.param.user.RestartPwdParam;
import cn.bootx.platform.iam.param.user.UserInfoParam;
import cn.bootx.platform.iam.param.user.UserInfoQuery;
import cn.bootx.platform.iam.result.role.RoleResult;
import cn.bootx.platform.iam.result.user.UserInfoResult;
import cn.bootx.platform.iam.result.user.UserWholeInfoResult;
import cn.daxpay.multi.merchant.service.merchant.MerchantUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 商户用户
 * @author xxm
 * @since 2024/8/23
 */
@RequestGroup(groupCode = "merchant", moduleCode = "PayConfig")
@Tag(name = "商户用户管理")
@RestController
@RequestMapping("/merchant/user")
@RequiredArgsConstructor
public class MerchantUserController {

    private final MerchantUserService merchantUserService;

    @RequestPath("用户列表")
    @Operation(summary = "用户列表")
    @GetMapping("/page")
    public Result<PageResult<UserWholeInfoResult>> page(PageParam pageParam, UserInfoQuery query) {
        return Res.ok(merchantUserService.page(pageParam, query));
    }

    @RequestPath("根据用户id查询商户号")
    @Operation(summary = "根据用户id查询商户号")
    @GetMapping("/findById")
    public Result<UserInfoResult> findById(@NotNull(message = "用户id不可为空") Long id) {
        return Res.ok(merchantUserService.findById(id));
    }


    @RequestPath("添加用户")
    @Operation(summary = "添加用户")
    @PostMapping("/add")
    public Result<Void> add(@RequestBody @Validated(ValidationGroup.add.class) UserInfoParam userInfoParam) {
        merchantUserService.add(userInfoParam);
        return Res.ok();
    }

    @RequestPath("修改用户")
    @Operation(summary = "修改用户")
    @PostMapping("/update")
    public Result<Void> update(@RequestBody @Validated(ValidationGroup.edit.class) UserInfoParam userInfoParam) {
        merchantUserService.update(userInfoParam);
        return Res.ok();
    }

    @RequestPath("重置密码")
    @Operation(summary = "重置密码")
    @OperateLog(title = "重置密码", businessType = OperateLog.BusinessType.UPDATE, saveParam = true)
    @PostMapping("/restartPassword")
    public Result<Void> restartPassword(@RequestBody @Validated RestartPwdParam param) {
        merchantUserService.restartPassword(param.getUserId(), param.getNewPassword());
        return Res.ok();
    }

    @RequestPath("批量重置密码")
    @Operation(summary = "批量重置密码")
    @OperateLog(title = "批量重置密码", businessType = OperateLog.BusinessType.UPDATE, saveParam = true)
    @PostMapping("/restartPasswordBatch")
    public Result<Void> restartPasswordBatch(@RequestBody @Validated RestartPwdBatchParam param) {
        merchantUserService.restartPasswordBatch(param.getUserIds(), param.getNewPassword());
        return Res.ok();
    }

    @RequestPath("分配角色")
    @Operation(summary = "分配角色")
    @PostMapping("/assignRole")
    public Result<Void> assignRole(@Validated @RequestBody UserRoleParam param) {
        merchantUserService.assignRole(param.getUserId(), param.getRoleIds());
        return Res.ok();
    }

    @Operation(summary = "批量给用户分配角色")
    @PostMapping("/assignRoleBatch")
    public Result<Void> assignRoleBatch(@Validated @RequestBody UserRoleBatchParam param) {
        merchantUserService.assignRoleBatch(param.getUserIds(), param.getRoleIds());
        return Res.ok();
    }


    @RequestPath("根据用户ID获取到角色集合")
    @Operation(summary = "根据用户ID获取到角色集合")
    @GetMapping(value = "/findRolesByUser")
    public Result<List<RoleResult>> findRolesByUser(Long userId) {
        return Res.ok(merchantUserService.findRolesByUser(userId));
    }

    @RequestPath("根据用户ID获取到角色id集合")
    @Operation(summary = "根据用户ID获取到角色id集合")
    @GetMapping(value = "/findRoleIdsByUser")
    public Result<List<Long>> findRoleIdsByUser(Long userId) {
        return Res.ok(merchantUserService.findRoleIdsByUser(userId));
    }

}
