package cn.bootx.platform.iam.controller.upms;

import cn.bootx.platform.core.annotation.RequestGroup;
import cn.bootx.platform.core.annotation.RequestPath;
import cn.bootx.platform.core.rest.Res;
import cn.bootx.platform.core.rest.result.Result;
import cn.bootx.platform.core.util.ValidationUtil;
import cn.bootx.platform.iam.param.permission.PermMenuAssignParam;
import cn.bootx.platform.iam.result.permission.PermMenuResult;
import cn.bootx.platform.iam.service.upms.RoleMenuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 角色权限(菜单)关联关系
 *
 * @author xxm
 * @since 2021/7/12
 */
@Tag(name = "角色菜单权限关系")
@RestController
@RequestMapping("/role/menu")
@RequiredArgsConstructor
@RequestGroup(groupCode = "upms", moduleCode = "iam")
public class RoleMenuController {

    private final RoleMenuService rolePermService;

    @RequestPath("保存请求权限关系")
    @Operation(summary = "保存请求权限关系")
    @PostMapping("/save")
    public Result<Boolean> save(@RequestBody PermMenuAssignParam param) {
        ValidationUtil.validateParam(param);
        rolePermService.saveAssign(param);
        return Res.ok(true);
    }


    @RequestPath("指定角色下的菜单权限树(分配时用)")
    @Operation(summary = "指定角色下的菜单权限树(分配时用)")
    @GetMapping("/treeByRole")
    public Result<List<PermMenuResult>> treeByRole(Long roleId, String clientCode) {
        return Res.ok(rolePermService.treeByRoleAssign(roleId,clientCode));
    }

    @RequestPath("查询当前角色已经选择的菜单id")
    @Operation(summary = "查询当前角色已经选择的菜单id")
    @GetMapping("/findIdsByRole")
    public Result<List<Long>> findIdsByRole(Long roleId, String clientCode) {
        return Res.ok(rolePermService.findIdsByRoleAndClient(roleId,clientCode));
    }

}
