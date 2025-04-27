package cn.bootx.platform.iam.controller.upms;

import cn.bootx.platform.core.annotation.OperateLog;
import cn.bootx.platform.core.annotation.RequestGroup;
import cn.bootx.platform.core.annotation.RequestPath;
import cn.bootx.platform.core.rest.Res;
import cn.bootx.platform.core.rest.result.Result;
import cn.bootx.platform.iam.param.upms.UserRoleBatchParam;
import cn.bootx.platform.iam.param.upms.UserRoleParam;
import cn.bootx.platform.iam.result.role.RoleResult;
import cn.bootx.platform.iam.service.upms.UserRoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author xxm
 * @since 2020/5/1 18:09
 */
@Validated
@Tag(name = "用户角色管理")
@RestController
@RequestMapping("/user/role")
@AllArgsConstructor
@RequestGroup(groupCode = "upms", moduleCode = "iam")
public class UserRoleController {

    private final UserRoleService userRoleService;

    @RequestPath("给用户分配角色")
    @Operation(summary = "给用户分配角色")
    @PostMapping(value = "/saveAssign")
    @OperateLog(title = "给用户分配角色", businessType = OperateLog.BusinessType.GRANT, saveParam = true)
    public Result<Void> saveAssign(@Validated @RequestBody UserRoleParam param) {
        userRoleService.saveAssign(param.getUserId(), param.getRoleIds(),false);
        return Res.ok();
    }

    @RequestPath("给用户分配角色(批量)")
    @Operation(summary = "给用户分配角色(批量)")
    @PostMapping(value = "/saveAssignBatch")
    @OperateLog(title = "给用户分配角色(批量)", businessType = OperateLog.BusinessType.GRANT, saveParam = true)
    public Result<Void> saveAssignBatch(@RequestBody @Validated UserRoleBatchParam param) {
        userRoleService.saveAssignBatch(param.getUserIds(), param.getRoleIds());
        return Res.ok();
    }

    @RequestPath("根据用户ID获取到角色集合")
    @Operation(summary = "根据用户ID获取到角色集合")
    @GetMapping(value = "/findRolesByUser")
    public Result<List<RoleResult>> findRolesByUser(@NotNull(message = "用户ID不可为空") @Parameter(description = "用户ID") Long userId) {
        return Res.ok(userRoleService.findRolesByUser(userId));
    }

    @RequestPath("根据用户ID获取到角色id集合")
    @Operation(summary = "根据用户ID获取到角色id集合")
    @GetMapping(value = "/findRoleIdsByUser")
    public Result<List<Long>> findRoleIdsByUser(@NotNull(message = "用户ID不可为空") @Parameter(description = "用户ID") Long userId) {
        return Res.ok(userRoleService.findRoleIdsByUser(userId));
    }

}
