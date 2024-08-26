package cn.bootx.platform.iam.controller.upms;

import cn.bootx.platform.core.annotation.RequestGroup;
import cn.bootx.platform.core.annotation.RequestPath;
import cn.bootx.platform.core.rest.Res;
import cn.bootx.platform.core.rest.result.Result;
import cn.bootx.platform.iam.param.permission.PermCodeAssignParam;
import cn.bootx.platform.iam.result.permission.PermCodeResult;
import cn.bootx.platform.iam.service.upms.RoleCodeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 角色权限码管理
 * @author xxm
 * @since 2024/7/7
 */
@Validated
@Tag(name = "角色权限码管理")
@RestController
@RequestMapping("/role/code")
@RequiredArgsConstructor
@RequestGroup(groupCode = "upms", groupName = "权限分配管理", moduleCode = "iam")
public class RoleCodeController {
    private final RoleCodeService roleCodeService;

    @RequestPath("保存请求权限关系")
    @Operation(summary = "保存请求权限关系")
    @PostMapping("/save")
    public Result<Boolean> save(@RequestBody @Validated PermCodeAssignParam param) {
        roleCodeService.saveAssign(param);
        return Res.ok(true);
    }

    @RequestPath("指定角色下的请求权限树(分配时用)")
    @Operation(summary = "指定角色下的请求权限树(分配时用)")
    @GetMapping("/treeByRole")
    public Result<List<PermCodeResult>> treeByRole(Long roleId) {
        return Res.ok(roleCodeService.treeByRoleAssign(roleId));
    }

    @RequestPath("查询当前角色已经选择的菜单id")
    @Operation(summary = "查询当前角色已经选择的菜单id")
    @GetMapping("/findIdsByRole")
    public Result<List<Long>> findIdsByRole(Long roleId) {
        return Res.ok(roleCodeService.findCodeIdsByRole(roleId));
    }

}
