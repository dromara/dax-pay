package cn.bootx.platform.iam.controller.upms;

import cn.bootx.platform.core.annotation.OperateLog;
import cn.bootx.platform.core.annotation.RequestGroup;
import cn.bootx.platform.core.annotation.RequestPath;
import cn.bootx.platform.core.rest.Res;
import cn.bootx.platform.core.rest.result.Result;
import cn.bootx.platform.iam.param.permission.PermPathAssignParam;
import cn.bootx.platform.iam.result.permission.SimplePermPathResult;
import cn.bootx.platform.iam.service.upms.RolePathService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author xxm
 * @since 2021/6/9
 */
@Validated
@Tag(name = "角色请求权限消息关系")
@RestController
@RequestMapping("/role/path")
@RequiredArgsConstructor
@RequestGroup(groupCode = "upms", moduleCode = "iam")
public class RolePathController {

    private final RolePathService rolePathService;

    @RequestPath("保存角色请求权限关联关系")
    @Operation(summary = "保存角色请求权限关联关系")
    @PostMapping("/save")
    @OperateLog(title = "保存角色请求权限关联关系", businessType = OperateLog.BusinessType.GRANT, saveParam = true)
    public Result<Void> save(@RequestBody @Validated PermPathAssignParam param) {
        rolePathService.saveAssign(param);
        return Res.ok();
    }

    @RequestPath("指定角色下的请求权限树(分配时用)")
    @Operation(summary = "指定角色下的请求权限树(分配时用)")
    @GetMapping("/treeByRole")
    public Result<List<SimplePermPathResult>> treeByRoleAndClient(
            @NotNull(message = "角色id不可为空") @Parameter(description = "角色id") Long roleId,
            @NotBlank(message = "终端编码不可为空") @Parameter(description = "终端编码") String clientCode) {
        return Res.ok(rolePathService.treeByRoleAssign(roleId,clientCode));
    }

    @RequestPath("查询当前角色已经选择的请求路径")
    @Operation(summary = "查询当前角色已经选择的请求路径")
    @GetMapping("/findIdsByRole")
    public Result<List<Long>> findIdsByRole(
            @NotNull(message = "角色id不可为空") @Parameter(description = "角色id") Long roleId,
            @NotBlank(message = "终端编码不可为空") @Parameter(description = "终端编码") String clientCode) {
        return Res.ok(rolePathService.findIdsByRole(roleId, clientCode));
    }

}
