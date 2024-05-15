package cn.bootx.platform.iam.controller;

import cn.bootx.platform.common.core.rest.Res;
import cn.bootx.platform.common.core.rest.ResResult;
import cn.bootx.platform.iam.core.permission.service.PermMenuService;
import cn.bootx.platform.iam.core.upms.service.RolePermService;
import cn.bootx.platform.iam.dto.permission.PermMenuDto;
import cn.bootx.platform.iam.param.permission.PermMenuParam;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 菜单和权限码
 * @author xxm
 * @since 2020/5/11 9:36
 */
@Tag(name = "菜单和权限码")
@RestController
@RequestMapping("/perm/menu")
@RequiredArgsConstructor
public class PermMenuController {

    private final PermMenuService permissionService;

    private final RolePermService rolePermissionService;

    @Operation(summary = "添加菜单权限")
    @PostMapping("/add")
    public ResResult<PermMenuDto> add(@RequestBody PermMenuParam param) {
        return Res.ok(permissionService.add(param));
    }

    @Operation(summary = "修改菜单权限")
    @PostMapping("/update")
    public ResResult<PermMenuDto> update(@RequestBody PermMenuParam param) {
        return Res.ok(permissionService.update(param));
    }

    @Operation(summary = "获取菜单树")
    @GetMapping("/menuTree")
    public ResResult<List<PermMenuDto>> menuTree(String clientCode) {
        return Res.ok(rolePermissionService.findMenuTree(clientCode));
    }

    @Operation(summary = "资源(权限码)列表")
    @GetMapping("/resourceList")
    public ResResult<List<PermMenuDto>> resourceList(Long menuId) {
        return Res.ok(permissionService.findResourceByMenuId(menuId));
    }

    @Operation(summary = "获取菜单和权限码树")
    @GetMapping("/menuAndPermCodeTree")
    public ResResult<List<PermMenuDto>> menuAndPermCodeTree(String clientCode) {
        return Res.ok(rolePermissionService.findMenuAndPermCodeTree(clientCode));
    }

    @Operation(summary = "根据id查询")
    @GetMapping("/findById")
    public ResResult<PermMenuDto> findById(Long id) {
        return Res.ok(permissionService.findById(id));
    }

    @Operation(summary = "删除")
    @DeleteMapping("/delete")
    public ResResult<Void> delete(Long id) {
        permissionService.delete(id);
        return Res.ok();
    }

    @Operation(summary = "编码是否被使用")
    @GetMapping("/existsByPermCode")
    public ResResult<Boolean> existsByPermCode(String permCode) {
        return Res.ok(permissionService.existsByPermCode(permCode));
    }

    @Operation(summary = "编码是否被使用(不包含自己)")
    @GetMapping("/existsByPermCodeNotId")
    public ResResult<Boolean> existsByPermCode(String permCode, Long id) {
        return Res.ok(permissionService.existsByPermCode(permCode, id));
    }
}
