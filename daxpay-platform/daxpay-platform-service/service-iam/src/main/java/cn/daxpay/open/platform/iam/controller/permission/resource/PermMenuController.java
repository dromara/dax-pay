package cn.daxpay.open.platform.iam.controller.permission.resource;

import cn.daxpay.open.platform.core.annotation.*;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.result.Result;
import cn.daxpay.open.platform.core.validation.ValidationGroup;
import cn.daxpay.open.platform.iam.param.permission.resource.PermMenuParam;
import cn.daxpay.open.platform.iam.result.permission.resource.PermMenuResult;
import cn.daxpay.open.platform.iam.service.permission.resource.PermMenuService;
import cn.daxpay.open.platform.iam.service.upms.UserRolePremService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/// # 菜单权限
///
@PermCode(menuCode = "iam:perm:menu")
@Validated
@Tag(name = "菜单权限管理")
@RestController
@RequestMapping("/perm/menu")
@RequiredArgsConstructor
public class PermMenuController {

    private final PermMenuService permMenuService;

    private final UserRolePremService userRoleService;

    @PermCode(code = "manage", nameCn = "菜单管理", nameEn = "Menu Manage")
    @InternalPath
    @Operation(summary = "添加菜单权限")
    @PostMapping("/add")
    public Result<Void> add(@RequestBody @Validated(ValidationGroup.add.class) PermMenuParam param) {
        permMenuService.add(param);
        return Res.ok();
    }

    @PermCode(code = "manage", nameCn = "菜单管理", nameEn = "Menu Manage")
    @InternalPath
    @Operation(summary = "修改菜单权限")
    @PostMapping("/update")
    public Result<Void> update(@RequestBody @Validated(ValidationGroup.edit.class) PermMenuParam param) {
        permMenuService.update(param);
        return Res.ok();
    }

    @PermCode(code = "view", nameCn = "菜单查看", nameEn = "Menu View")
    @InternalPath
    @Operation(summary = "获取菜单树", description = "管理端接口，终端编码通过参数传递")
    @GetMapping("/tree")
    public Result<List<PermMenuResult>> menuTree(@NotBlank(message = "{validation.field.clientCode.notBlank}") @Parameter(description = "终端编码") String clientCode) {
        return Res.ok(permMenuService.tree(clientCode));
    }

    @PermCode(code = "view", nameCn = "菜单查看", nameEn = "Menu View")
    @Operation(summary = "获取当前用户菜单树", description = "登录用户获取个人菜单，终端编码从请求头读取")
    @GetMapping("/my")
    public Result<List<PermMenuResult>> myMenuTree() {
        return Res.ok(userRoleService.menuTreeByCurrentUser());
    }

    @PermCode(code = "view", nameCn = "菜单查看", nameEn = "Menu View")
    @Operation(summary = "根据id查询")
    @GetMapping("/get")
    public Result<PermMenuResult> findById(@NotNull(message = "{validation.field.id.notNull}") Long id) {
        return Res.ok(permMenuService.findById(id));
    }

    @PermCode(code = "manage", nameCn = "菜单管理", nameEn = "Menu Manage")
    @InternalPath
    @Operation(summary = "删除菜单权限")
    @PostMapping("/delete")
    public Result<Void> delete(@NotNull(message = "{validation.field.id.notNull}") Long id) {
        permMenuService.delete(id);
        return Res.ok();
    }

    @PermCode(code = "view", nameCn = "菜单查看", nameEn = "Menu View")
    @Operation(summary = "检查菜单编码是否存在")
    @GetMapping("/check-menu-code-exists")
    public Result<Boolean> checkMenuCodeExists(
            @NotBlank(message = "{validation.field.menuCode.notBlank}") @Parameter(description = "菜单编码") String menuCode,
            @NotBlank(message = "{validation.field.clientCode.notBlank}") @Parameter(description = "终端编码") String clientCode,
            @Parameter(description = "排除的菜单ID") Long excludeId) {
        return Res.ok(permMenuService.checkMenuCodeExists(menuCode, clientCode, excludeId));
    }

}
