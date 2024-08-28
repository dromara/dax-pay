package cn.bootx.platform.iam.controller.role;

import cn.bootx.platform.core.annotation.IgnoreAuth;
import cn.bootx.platform.core.annotation.RequestGroup;
import cn.bootx.platform.core.annotation.RequestPath;
import cn.bootx.platform.core.rest.Res;
import cn.bootx.platform.core.rest.dto.KeyValue;
import cn.bootx.platform.core.rest.result.Result;
import cn.bootx.platform.core.util.ValidationUtil;
import cn.bootx.platform.iam.param.role.RoleParam;
import cn.bootx.platform.iam.result.role.RoleResult;
import cn.bootx.platform.iam.service.role.RoleQueryService;
import cn.bootx.platform.iam.service.role.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author xxm
 * @since 2021/6/9
 */
@Tag(name = "角色管理")
@RestController
@RequestMapping("/role")
@RequiredArgsConstructor
@RequestGroup(groupCode = "role", groupName = "角色管理", moduleCode = "iam")
public class RoleController {

    private final RoleService roleService;

    private final RoleQueryService roleQueryService;

    @IgnoreAuth
    @Operation(summary = "添加角色")
    @PostMapping(value = "/add")
    public Result<Void> add(@RequestBody RoleParam roleParam) {
        ValidationUtil.validateParam(roleParam);
        roleService.add(roleParam);
        return Res.ok();
    }

    @IgnoreAuth
    @Operation(summary = "删除角色")
    @PostMapping(value = "/delete")
    public Result<Void> delete(Long id) {
        roleService.delete(id);
        return Res.ok();
    }

    @IgnoreAuth
    @Operation(summary = "修改角色")
    @PostMapping(value = "/update")
    public Result<Void> update(@RequestBody RoleParam roleParam) {
        ValidationUtil.validateParam(roleParam);
        roleService.update(roleParam);
        return Res.ok();
    }

    @IgnoreAuth
    @Operation(summary = "角色树")
    @GetMapping("/tree")
    public Result<List<RoleResult>> tree(){
        return Res.ok(roleQueryService.tree());
    }

    @RequestPath("通过ID查询角色")
    @Operation(summary = "通过ID查询角色")
    @GetMapping(value = "/findById")
    public Result<RoleResult> findById(Long id) {
        return Res.ok(roleQueryService.findById(id));
    }

    @RequestPath("查询所有的角色")
    @Operation(summary = "查询所有的角色")
    @GetMapping(value = "/findAll")
    public Result<List<RoleResult>> findAll() {
        return Res.ok(roleQueryService.findAll());
    }

    @RequestPath("角色下拉框")
    @Operation(summary = "角色下拉框")
    @GetMapping(value = "/dropdown")
    public Result<List<KeyValue>> dropdown() {
        return Res.ok(roleQueryService.dropdown());
    }

    @RequestPath("编码是否被使用")
    @Operation(summary = "编码是否被使用")
    @GetMapping("/existsByCode")
    public Result<Boolean> existsByCode(String code) {
        return Res.ok(roleQueryService.existsByCode(code));
    }

    @RequestPath("编码是否被使用(不包含自己)")
    @Operation(summary = "编码是否被使用(不包含自己)")
    @GetMapping("/existsByCodeNotId")
    public Result<Boolean> existsByCode(String code, Long id) {
        return Res.ok(roleQueryService.existsByCode(code, id));
    }

    @RequestPath("名称是否被使用")
    @Operation(summary = "名称是否被使用")
    @GetMapping("/existsByName")
    public Result<Boolean> existsByName(String name) {
        return Res.ok(roleQueryService.existsByName(name));
    }

    @RequestPath("名称是否被使用(不包含自己)")
    @Operation(summary = "名称是否被使用(不包含自己)")
    @GetMapping("/existsByNameNotId")
    public Result<Boolean> existsByName(String name, Long id) {
        return Res.ok(roleQueryService.existsByName(name, id));
    }

}
