package cn.bootx.platform.iam.controller.permission;

import cn.bootx.platform.core.annotation.IgnoreAuth;
import cn.bootx.platform.core.annotation.InternalPath;
import cn.bootx.platform.core.annotation.RequestGroup;
import cn.bootx.platform.core.annotation.RequestPath;
import cn.bootx.platform.core.entity.UserDetail;
import cn.bootx.platform.core.rest.Res;
import cn.bootx.platform.core.rest.result.Result;
import cn.bootx.platform.iam.param.permission.PermMenuParam;
import cn.bootx.platform.iam.result.permission.PermMenuResult;
import cn.bootx.platform.iam.service.permission.PermMenuService;
import cn.bootx.platform.iam.service.upms.UserRolePremService;
import cn.bootx.platform.starter.auth.util.SecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 菜单权限
 * @author xxm
 * @since 2020/5/11 9:36
 */
@Tag(name = "菜单权限管理")
@RestController
@RequestMapping("/perm/menu")
@RequiredArgsConstructor
@RequestGroup(groupCode = "perm", moduleCode = "iam")
public class PermMenuController {

    private final PermMenuService permMenuService;

    private final UserRolePremService userRoleService;

    @InternalPath
    @Operation(summary = "添加菜单权限")
    @PostMapping("/add")
    public Result<Void> add(@RequestBody PermMenuParam param) {
        permMenuService.add(param);
        return Res.ok();
    }

    @InternalPath
    @Operation(summary = "修改菜单权限")
    @PostMapping("/update")
    public Result<Void> update(@RequestBody PermMenuParam param) {
        permMenuService.update(param);
        return Res.ok();
    }


    @IgnoreAuth
    @Operation(summary = "获取菜单树")
    @GetMapping("/tree")
    public Result<List<PermMenuResult>> menuTree(String clientCode) {
        UserDetail user = SecurityUtil.getUser();
        if (user.isAdmin()){
            return Res.ok(permMenuService.tree(clientCode));
        }
        return Res.ok(userRoleService.menuTreeByUser(user.getId(),clientCode));
    }

    @RequestPath("根据id查询菜单")
    @Operation(summary = "根据id查询")
    @GetMapping("/findById")
    public Result<PermMenuResult> findById(Long id) {
        return Res.ok(permMenuService.findById(id));
    }

    @InternalPath
    @Operation(summary = "删除")
    @PostMapping("/delete")
    public Result<Void> delete(Long id) {
        permMenuService.delete(id);
        return Res.ok();
    }

}
