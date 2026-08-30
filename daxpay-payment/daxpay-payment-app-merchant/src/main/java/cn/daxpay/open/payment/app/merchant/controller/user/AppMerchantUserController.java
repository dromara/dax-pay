package cn.daxpay.open.payment.app.merchant.controller.user;

import cn.daxpay.open.payment.app.merchant.service.user.AppMerchantUserService;
import cn.daxpay.open.payment.merchant.param.info.MerchantUserParam;
import cn.daxpay.open.payment.merchant.param.info.MerchantUserQuery;
import cn.daxpay.open.payment.merchant.param.info.MerchantUserResetPwdParam;
import cn.daxpay.open.payment.merchant.result.info.MerchantUserResult;
import cn.daxpay.open.platform.core.annotation.OperateLog;
import cn.daxpay.open.platform.core.annotation.PermCode;
import cn.daxpay.open.platform.core.code.PermCodes;
import cn.daxpay.open.platform.core.enums.common.OperateLogType;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import cn.daxpay.open.platform.core.rest.result.Result;
import cn.daxpay.open.platform.core.validation.ValidationGroup;
import cn.daxpay.open.platform.iam.result.user.UserInfoResult;
import cn.daxpay.open.platform.iam.result.user.UserPasswordResult;
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

/// # 商户用户管理(商户移动端)
///
/// 商户自助管理本商户的子账号。与运营端/商户 Web 端三端同码(menuCode=`merchant:user`),
/// mchNo 由商户端上下文装载, 不信任入参; App 端不做批量操作。
@PermCode(menuCode = PermCodes.Merchant.User.MENU)
@Validated
@Tag(name = "商户用户管理(商户移动端)")
@RestController
@RequestMapping("/app-mch/user")
@RequiredArgsConstructor
public class AppMerchantUserController {

    private final AppMerchantUserService merchantUserService;

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "商户用户分页查询")
    @GetMapping("/page")
    public Result<PageResult<MerchantUserResult>> page(PageParam pageParam, MerchantUserQuery query) {
        return Res.ok(merchantUserService.page(pageParam, query));
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "根据用户ID查询用户详情")
    @GetMapping("/get")
    public Result<UserInfoResult> findById(@NotNull(message = "{validation.field.id.notNull}") Long id) {
        return Res.ok(merchantUserService.findById(id));
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "添加商户用户")
    @PostMapping("/add")
    @OperateLog(title = "新增商户用户", businessType = OperateLogType.ADD, saveParam = true, maskParam = true)
    public Result<UserPasswordResult> add(@RequestBody @Validated(ValidationGroup.add.class) MerchantUserParam param) {
        // 未指定密码时由后端生成随机初始密码, 响应中一次性返回明文供管理员转告用户
        return Res.ok(merchantUserService.add(param));
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "修改商户用户")
    @PostMapping("/update")
    @OperateLog(title = "修改商户用户", businessType = OperateLogType.UPDATE, saveParam = true, maskParam = true)
    public Result<Void> update(@RequestBody @Validated(ValidationGroup.edit.class) MerchantUserParam param) {
        merchantUserService.update(param);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "强制解绑商户用户邮箱")
    @PostMapping("/unbind-email")
    @OperateLog(title = "强制解绑商户用户邮箱", businessType = OperateLogType.UPDATE, saveParam = true, maskParam = true)
    public Result<Void> unbindEmail(@NotNull(message = "{validation.field.userId.notNull}") Long userId) {
        // 仅清空邮箱与验证状态, 不可指定新邮箱(邮箱变更只能由用户本人走绑定验证流程)
        merchantUserService.unbindEmail(userId);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.ASSIGN_ROLE)
    @Operation(summary = "分配角色")
    @PostMapping("/assign-role")
    @OperateLog(title = "分配商户用户角色", businessType = OperateLogType.GRANT, saveParam = true, maskParam = true)
    public Result<Void> assignRole(
            @NotNull(message = "{validation.field.userId.notNull}") Long userId,
            @NotNull(message = "{validation.field.roleId.notNull}") Long roleId) {
        merchantUserService.assignRole(userId, roleId);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.STATUS)
    @Operation(summary = "封禁商户用户")
    @PostMapping("/ban")
    @OperateLog(title = "封禁商户用户", businessType = OperateLogType.UPDATE, saveParam = true, maskParam = true)
    public Result<Void> ban(@NotNull(message = "{validation.field.userId.notNull}") Long userId) {
        merchantUserService.ban(userId);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.STATUS)
    @Operation(summary = "解锁商户用户")
    @PostMapping("/unlock")
    @OperateLog(title = "解锁商户用户", businessType = OperateLogType.UPDATE, saveParam = true, maskParam = true)
    public Result<Void> unlock(@NotNull(message = "{validation.field.userId.notNull}") Long userId) {
        merchantUserService.unlock(userId);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.RESET_PASSWORD)
    @Operation(summary = "重置密码")
    @PostMapping("/restart-password")
    @OperateLog(title = "重置商户用户密码", businessType = OperateLogType.UPDATE, saveParam = true, maskParam = true)
    public Result<UserPasswordResult> restartPassword(@RequestBody @Validated MerchantUserResetPwdParam param) {
        // 由后端生成随机密码, 响应中一次性返回明文供管理员转告用户
        return Res.ok(merchantUserService.restartPassword(param.getUserId()));
    }
}
