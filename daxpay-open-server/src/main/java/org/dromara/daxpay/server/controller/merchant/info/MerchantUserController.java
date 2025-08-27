package org.dromara.daxpay.server.controller.merchant.info;

import cn.bootx.platform.core.annotation.ClientCode;
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
import cn.bootx.platform.iam.result.user.UserInfoResult;
import org.dromara.daxpay.service.merchant.param.info.MerchantUserQuery;
import org.dromara.daxpay.service.merchant.result.info.MerchantUserResult;
import org.dromara.daxpay.service.merchant.service.info.MerchantUserService;
import org.dromara.daxpay.service.common.code.DaxPayCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.dromara.core.trans.anno.TransMethodResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 商户用户管理
 * @author xxm
 * @since 2024/8/23
 */
@ClientCode({DaxPayCode.Client.ADMIN})
@Tag(name = "商户用户管理")
@RestController
@RequestGroup(groupCode = "MerchantUser", groupName = "商户用户管理", moduleCode = "merchant")
@RequestMapping("/merchant/user")
@RequiredArgsConstructor
public class MerchantUserController {

    private final MerchantUserService merchantUserService;

    @TransMethodResult
    @RequestPath("分页")
    @Operation(summary = "分页")
    @GetMapping("/page")
    public Result<PageResult<MerchantUserResult>> page(PageParam pageParam, MerchantUserQuery query) {
        return Res.ok(merchantUserService.page(pageParam, query));
    }


    @RequestPath("根据用户id查询用户 ")
    @Operation(summary = "根据用户id查询用户")
    @GetMapping("/findById")
    public Result<UserInfoResult> findById(@NotNull(message = "主键不可为空") Long id) {
        return Res.ok(merchantUserService.findById(id));
    }

    @RequestPath("修改用户")
    @Operation(summary = "修改用户")
    @PostMapping("/update")
    @OperateLog(title = "修改用户", businessType = OperateLog.BusinessType.UPDATE, saveParam = true)
    public Result<Void> update(@RequestBody @Validated(ValidationGroup.edit.class) UserInfoParam userInfoParam) {
        merchantUserService.update(userInfoParam);
        return Res.ok();
    }

    @RequestPath("重置密码")
    @Operation(summary = "重置密码")
    @PostMapping("/restartPassword")
    @OperateLog(title = "重置密码", businessType = OperateLog.BusinessType.UPDATE)
    public Result<Void> restartPassword(@RequestBody @Validated RestartPwdParam param) {
        merchantUserService.restartPassword(param.getUserId(), param.getNewPassword());
        return Res.ok();
    }

    @RequestPath("批量重置密码")
    @Operation(summary = "批量重置密码")
    @PostMapping("/restartPasswordBatch")
    @OperateLog(title = "批量重置密码", businessType = OperateLog.BusinessType.UPDATE)
    public Result<Void> restartPasswordBatch(@RequestBody @Validated RestartPwdBatchParam param) {
        merchantUserService.restartPasswordBatch(param.getUserIds(), param.getNewPassword());
        return Res.ok();
    }

    @RequestPath("封禁用户")
    @Operation(summary = "封禁用户")
    @PostMapping("/ban")
    @OperateLog(title = "封禁用户", businessType = OperateLog.BusinessType.GRANT, saveParam = true)
    public Result<Void> ban(@NotNull(message = "用户不可为空") Long userId) {
        merchantUserService.ban(userId);
        return Res.ok();
    }

    @RequestPath("批量封禁用户")
    @Operation(summary = "批量封禁用户")
    @PostMapping("/banBatch")
    @OperateLog(title = "批量封禁用户", businessType = OperateLog.BusinessType.GRANT, saveParam = true)
    public Result<Void> banBatch(@RequestBody @NotEmpty(message = "用户集合不可为空") List<Long> userIds) {
        merchantUserService.banBatch(userIds);
        return Res.ok();
    }

    @RequestPath("解锁用户")
    @Operation(summary = "解锁用户")
    @PostMapping("/unlock")
    @OperateLog(title = "解锁用户", businessType = OperateLog.BusinessType.GRANT, saveParam = true)
    public Result<Void> unlock(@NotNull(message = "用户不可为空") Long userId) {
        merchantUserService.unlock(userId);
        return Res.ok();
    }

    @RequestPath("批量解锁用户")
    @Operation(summary = "批量解锁用户")
    @PostMapping("/unlockBatch")
    @OperateLog(title = "批量解锁用户", businessType = OperateLog.BusinessType.GRANT, saveParam = true)
    public Result<Void> unlockBatch(@RequestBody @NotEmpty(message = "用户集合不可为空") List<Long> userIds) {
        merchantUserService.unlockBatch(userIds);
        return Res.ok();
    }

}
