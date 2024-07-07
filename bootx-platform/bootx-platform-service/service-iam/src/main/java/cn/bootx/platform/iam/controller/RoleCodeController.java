package cn.bootx.platform.iam.controller;

import cn.bootx.platform.core.rest.Res;
import cn.bootx.platform.core.rest.result.Result;
import cn.bootx.platform.core.util.ValidationUtil;
import cn.bootx.platform.iam.param.permission.PermCodeAssignParam;
import cn.bootx.platform.iam.result.permission.PermCodeResult;
import cn.bootx.platform.iam.service.upms.RoleCodeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 角色权限码管理
 * @author xxm
 * @since 2024/7/7
 */
@Tag(name = "角色权限码管理")
@RestController
@RequestMapping("/role/code")
@RequiredArgsConstructor
public class RoleCodeController {
    private final RoleCodeService roleCodeService;

    @Operation(summary = "保存请求权限关系")
    @PostMapping("/save")
    public Result<Boolean> save(@RequestBody PermCodeAssignParam param) {
        ValidationUtil.validateParam(param);
        roleCodeService.saveAssign(param);
        return Res.ok(true);
    }

    @Operation(summary = "指定角色下的请求权限树(分配时用)")
    @GetMapping("/treeByRole")
    public Result<List<PermCodeResult>> treeByRole(Long roleId) {
        return Res.ok(roleCodeService.treeByRoleAssign(roleId));
    }

    @Operation(summary = "查询当前角色已经选择的菜单id")
    @GetMapping("/findIdsByRole")
    public Result<List<String>> findIdsByRole(Long roleId) {
        return Res.ok(roleCodeService.findCodesByRole(roleId));
    }

}
