package org.dromara.daxpay.platform.iam.controller.upms;

import org.dromara.daxpay.platform.core.annotation.PermCode;
import org.dromara.daxpay.platform.core.rest.Res;
import org.dromara.daxpay.platform.core.rest.result.Result;
import org.dromara.daxpay.platform.iam.param.upms.UserRoleBatchParam;
import org.dromara.daxpay.platform.iam.param.upms.UserRoleParam;
import org.dromara.daxpay.platform.iam.result.role.RoleResult;
import org.dromara.daxpay.platform.iam.service.upms.UserRoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@PermCode(menuCode = "iam:user:manager")
@Validated
@Tag(name = "用户角色管理")
@RestController
@RequestMapping("/user/role")
@AllArgsConstructor
public class UserRoleController {

    private final UserRoleService userRoleService;

    @PermCode(code = "assignRole", nameCn = "分配角色", nameEn = "Assign Role")
    @Operation(summary = "给用户分配角色")
    @PostMapping(value = "/save-assign")
    public Result<Void> saveAssign(@Validated @RequestBody UserRoleParam param) {
        userRoleService.saveAssign(param.getUserId(), param.getRoleId(),false);
        return Res.ok();
    }

    @PermCode(code = "assignRole", nameCn = "分配角色", nameEn = "Assign Role")
    @Operation(summary = "给用户分配角色(批量)")
    @PostMapping(value = "/save-assign-batch")
    public Result<Void> saveAssignBatch(@RequestBody @Validated UserRoleBatchParam param) {
        userRoleService.saveAssignBatch(param.getUserIds(), param.getRoleId());
        return Res.ok();
    }

    @PermCode(code = "view", nameCn = "用户查看", nameEn = "User View")
    @Operation(summary = "根据用户ID获取角色")
    @GetMapping(value = "/find-roles-by-user")
    public Result<RoleResult> findRolesByUser(@NotNull(message = "{validation.field.userId.notNull}") @Parameter(description = "用户ID") Long userId) {
        return Res.ok(userRoleService.findRolesByUser(userId));
    }

    @PermCode(code = "assignRole", nameCn = "分配角色", nameEn = "Assign Role")
    @Operation(summary = "根据用户ID获取到可分配角色集合")
    @GetMapping(value = "/find-assignable-roles-by-user")
    public Result<List<RoleResult>> findAssignableRolesByUser(@NotNull(message = "{validation.field.userId.notNull}") @Parameter(description = "用户ID") Long userId) {
        return Res.ok(userRoleService.findAssignableRolesByUser(userId));
    }

    @PermCode(code = "assignRole", nameCn = "分配角色", nameEn = "Assign Role")
    @Operation(summary = "根据用户ID获取到角色id集合")
    @GetMapping(value = "/find-role-ids-by-user")
    public Result<List<Long>> findRoleIdsByUser(@NotNull(message = "{validation.field.userId.notNull}") @Parameter(description = "用户ID") Long userId) {
        return Res.ok(userRoleService.findRoleIdsByUser(userId));
    }

}

