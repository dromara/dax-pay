package cn.bootx.platform.iam.controller;

import cn.bootx.platform.common.core.rest.PageResult;
import cn.bootx.platform.common.core.rest.Res;
import cn.bootx.platform.common.core.rest.ResResult;
import cn.bootx.platform.common.core.rest.dto.KeyValue;
import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.core.util.ValidationUtil;
import cn.bootx.platform.iam.core.role.service.RoleService;
import cn.bootx.platform.iam.dto.role.RoleDto;
import cn.bootx.platform.iam.param.role.RoleParam;
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
public class RoleController {

    private final RoleService roleService;

    @Operation(summary = "添加角色（返回角色对象）")
    @PostMapping(value = "/add")
    public ResResult<Void> add(@RequestBody RoleParam roleParam) {
        ValidationUtil.validateParam(roleParam);
        roleService.add(roleParam);
        return Res.ok();
    }

    @Operation(summary = "删除角色")
    @DeleteMapping(value = "/delete")
    public ResResult<Void> delete(Long id) {
        roleService.delete(id);
        return Res.ok();
    }

    @Operation(summary = "角色树")
    @GetMapping("/tree")
    public ResResult<List<RoleDto>> tree(){
        return Res.ok(roleService.tree());
    }

    @Operation(summary = "修改角色（返回角色对象）")
    @PostMapping(value = "/update")
    public ResResult<RoleDto> update(@RequestBody RoleParam roleParam) {
        ValidationUtil.validateParam(roleParam);
        roleService.update(roleParam);
        return Res.ok();
    }

    @Operation(summary = "通过ID查询角色")
    @GetMapping(value = "/findById")
    public ResResult<RoleDto> findById(Long id) {
        return Res.ok(roleService.findById(id));
    }

    @Operation(summary = "查询所有的角色")
    @GetMapping(value = "/findAll")
    public ResResult<List<RoleDto>> findAll() {
        return Res.ok(roleService.findAll());
    }

    @Operation(summary = "角色下拉框")
    @GetMapping(value = "/dropdown")
    public ResResult<List<KeyValue>> dropdown() {
        return Res.ok(roleService.dropdown());
    }

    @Operation(summary = "分页查询角色")
    @GetMapping(value = "/page")
    public ResResult<PageResult<RoleDto>> page(PageParam pageParam, RoleParam roleParam) {
        return Res.ok(roleService.page(pageParam, roleParam));
    }

    @Operation(summary = "编码是否被使用")
    @GetMapping("/existsByCode")
    public ResResult<Boolean> existsByCode(String code) {
        return Res.ok(roleService.existsByCode(code));
    }

    @Operation(summary = "编码是否被使用(不包含自己)")
    @GetMapping("/existsByCodeNotId")
    public ResResult<Boolean> existsByCode(String code, Long id) {
        return Res.ok(roleService.existsByCode(code, id));
    }

    @Operation(summary = "名称是否被使用")
    @GetMapping("/existsByName")
    public ResResult<Boolean> existsByName(String name) {
        return Res.ok(roleService.existsByName(name));
    }

    @Operation(summary = "名称是否被使用(不包含自己)")
    @GetMapping("/existsByNameNotId")
    public ResResult<Boolean> existsByName(String name, Long id) {
        return Res.ok(roleService.existsByName(name, id));
    }

}
