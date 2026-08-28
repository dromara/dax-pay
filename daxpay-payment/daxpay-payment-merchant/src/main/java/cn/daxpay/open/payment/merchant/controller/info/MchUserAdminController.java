package cn.daxpay.open.payment.merchant.controller.info;

import cn.daxpay.open.platform.core.annotation.OperateLog;
import cn.daxpay.open.platform.core.annotation.PermCode;
import cn.daxpay.open.platform.core.code.PermCodes;
import cn.daxpay.open.platform.core.enums.common.OperateLogType;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import cn.daxpay.open.platform.core.rest.result.Result;
import cn.daxpay.open.platform.core.validation.ValidationGroup;
import cn.daxpay.open.payment.merchant.param.info.MerchantUserBatchParam;
import cn.daxpay.open.payment.merchant.param.info.MerchantUserParam;
import cn.daxpay.open.payment.merchant.param.info.MerchantUserQuery;
import cn.daxpay.open.payment.merchant.param.info.MerchantUserResetPwdBatchParam;
import cn.daxpay.open.payment.merchant.param.info.MerchantUserResetPwdParam;
import cn.daxpay.open.platform.iam.result.user.UserInfoResult;
import cn.daxpay.open.platform.iam.result.user.UserPasswordResult;
import cn.daxpay.open.payment.merchant.result.info.MerchantUserResult;
import cn.daxpay.open.payment.merchant.service.user.MerchantUserAdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/// # 商户用户管理
///
@PermCode(menuCode = PermCodes.Merchant.User.MENU)
@Validated
@Tag(name = "商户用户管理")
@RestController
/// 双路径: 运营端 /admin/merchant/user 与商户端 /mch/user 共用同一 Service；
    /// 客户端隔离由 ClientCode + @PermCode 保证；商户端 TenantLine 按 mchNo 行级隔离。
    @RequestMapping({"/admin/merchant/user", "/mch/user"})
@RequiredArgsConstructor
public class MchUserAdminController {

    private final MerchantUserAdminService merchantUserAdminService;

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "商户用户分页查询")
    @GetMapping("/page")
    public Result<PageResult<MerchantUserResult>> page(PageParam pageParam, MerchantUserQuery query) {
        return Res.ok(merchantUserAdminService.page(pageParam, query));
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "根据用户ID查询用户详情")
    @GetMapping("/get")
    public Result<UserInfoResult> findById(@NotNull(message = "{validation.field.id.notNull}") Long id) {
        return Res.ok(merchantUserAdminService.findById(id));
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "添加商户用户")
    @PostMapping("/add")
    @OperateLog(title = "新增商户用户", businessType = OperateLogType.ADD, saveParam = true, maskParam = true)
    public Result<UserPasswordResult> add(@RequestBody @Validated(ValidationGroup.add.class) MerchantUserParam param) {
        // 未指定密码时由后端生成随机初始密码, 响应中一次性返回明文供管理员转告用户
        return Res.ok(merchantUserAdminService.add(param));
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "修改商户用户")
    @PostMapping("/update")
    @OperateLog(title = "修改商户用户", businessType = OperateLogType.UPDATE, saveParam = true, maskParam = true)
    public Result<Void> update(@RequestBody @Validated(ValidationGroup.edit.class) MerchantUserParam param) {
        merchantUserAdminService.update(param);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "强制解绑商户用户邮箱")
    @PostMapping("/unbind-email")
    @OperateLog(title = "强制解绑商户用户邮箱", businessType = OperateLogType.UPDATE, saveParam = true, maskParam = true)
    public Result<Void> unbindEmail(@NotNull(message = "{validation.field.userId.notNull}") Long userId) {
        // 仅清空邮箱与验证状态, 不可指定新邮箱(邮箱变更只能由用户本人走绑定验证流程)
        merchantUserAdminService.unbindEmail(userId);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.ASSIGN_ROLE)
    @Operation(summary = "分配角色")
    @PostMapping("/assign-role")
    @OperateLog(title = "分配商户用户角色", businessType = OperateLogType.GRANT, saveParam = true, maskParam = true)
    public Result<Void> assignRole(@NotNull(message = "{validation.field.userId.notNull}") Long userId,
                                    @NotNull(message = "{validation.field.roleId.notNull}") Long roleId) {
        merchantUserAdminService.assignRole(userId, roleId);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.STATUS)
    @Operation(summary = "封禁商户用户")
    @PostMapping("/ban")
    @OperateLog(title = "封禁商户用户", businessType = OperateLogType.UPDATE, saveParam = true, maskParam = true)
    public Result<Void> ban(@NotNull(message = "{validation.field.userId.notNull}") Long userId) {
        merchantUserAdminService.ban(userId);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.STATUS)
    @Operation(summary = "批量封禁商户用户")
    @PostMapping("/ban-batch")
    @OperateLog(title = "批量封禁商户用户", businessType = OperateLogType.UPDATE, saveParam = true, maskParam = true)
    public Result<Void> banBatch(@RequestBody @NotEmpty(message = "{validation.field.userIds.notEmpty}") List<Long> userIds) {
        merchantUserAdminService.banBatch(userIds);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.STATUS)
    @Operation(summary = "解锁商户用户")
    @PostMapping("/unlock")
    @OperateLog(title = "解锁商户用户", businessType = OperateLogType.UPDATE, saveParam = true, maskParam = true)
    public Result<Void> unlock(@NotNull(message = "{validation.field.userId.notNull}") Long userId) {
        merchantUserAdminService.unlock(userId);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.STATUS)
    @Operation(summary = "批量解锁商户用户")
    @PostMapping("/unlock-batch")
    @OperateLog(title = "批量解锁商户用户", businessType = OperateLogType.UPDATE, saveParam = true, maskParam = true)
    public Result<Void> unlockBatch(@RequestBody @NotEmpty(message = "{validation.field.userIds.notEmpty}") List<Long> userIds) {
        merchantUserAdminService.unlockBatch(userIds);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.RESET_PASSWORD)
    @Operation(summary = "重置密码")
    @PostMapping("/restart-password")
    @OperateLog(title = "重置商户用户密码", businessType = OperateLogType.UPDATE, saveParam = true, maskParam = true)
    public Result<UserPasswordResult> restartPassword(@RequestBody @Validated MerchantUserResetPwdParam param) {
        // 由后端生成随机密码, 响应中一次性返回明文供管理员转告用户
        return Res.ok(merchantUserAdminService.restartPassword(param.getUserId()));
    }

    @PermCode(code = PermCodes.Action.RESET_PASSWORD)
    @Operation(summary = "批量重置密码")
    @PostMapping("/restart-password-batch")
    @OperateLog(title = "批量重置商户用户密码", businessType = OperateLogType.UPDATE, saveParam = true, maskParam = true)
    public Result<List<UserPasswordResult>> restartPasswordBatch(@RequestBody @Validated MerchantUserResetPwdBatchParam param) {
        // 每个用户独立生成随机密码, 响应中一次性返回明文列表
        return Res.ok(merchantUserAdminService.restartPasswordBatch(param.getUserIds()));
    }
}
