package cn.daxpay.open.payment.merchant.controller.info;

import cn.daxpay.open.platform.core.annotation.PermCode;
import cn.daxpay.open.platform.core.code.PermCodes;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.result.Result;
import cn.daxpay.open.platform.iam.result.role.RoleResult;
import cn.daxpay.open.payment.merchant.service.user.MerchantUserAdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/// # 商户用户角色查询
///
/// 商户端用户管理「分配角色」弹窗的读路径专用。原前端直连平台级 [UserRoleController]（/user/role/**,
/// 完整码 iam:user:assign-role）, 该码挂运营端 iam:user 菜单, 商户端角色无法绑定恒 403,
/// 故在 merchant:user 域下单独开路; 运营端不受影响, 仍走原控制器。
/// 写路径(保存分配)在 [MchUserAdminController#assignRole], 读写同域同码。
/// 目标用户须归属当前商户(防跨商户/跨端窥探角色清单), 校验住 [MerchantUserAdminService#findAssignableRolesByUser]。
@PermCode(menuCode = PermCodes.Merchant.User.MENU)
@Validated
@Tag(name = "商户用户角色管理")
@RestController
@RequestMapping("/mch/user/role")
@RequiredArgsConstructor
public class MchUserRoleController {

    private final MerchantUserAdminService merchantUserAdminService;

    @PermCode(code = PermCodes.Action.ASSIGN_ROLE)
    @Operation(summary = "根据用户ID获取到可分配角色集合")
    @GetMapping(value = "/find-assignable-roles-by-user")
    public Result<List<RoleResult>> findAssignableRolesByUser(@NotNull(message = "用户 ID 不能为空") @Parameter(description = "用户ID") Long userId) {
        return Res.ok(merchantUserAdminService.findAssignableRolesByUser(userId));
    }

    @PermCode(code = PermCodes.Action.ASSIGN_ROLE)
    @Operation(summary = "根据用户ID获取到角色id集合")
    @GetMapping(value = "/find-role-ids-by-user")
    public Result<List<Long>> findRoleIdsByUser(@NotNull(message = "用户 ID 不能为空") @Parameter(description = "用户ID") Long userId) {
        return Res.ok(merchantUserAdminService.findRoleIdsByUser(userId));
    }
}
