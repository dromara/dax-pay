package org.dromara.daxpay.platform.iam.controller.role;

import org.dromara.daxpay.platform.core.annotation.PermCode;
import org.dromara.daxpay.platform.core.rest.Res;
import org.dromara.daxpay.platform.core.rest.dto.KeyValue;
import org.dromara.daxpay.platform.core.rest.param.PageParam;
import org.dromara.daxpay.platform.core.rest.result.PageResult;
import org.dromara.daxpay.platform.core.rest.result.Result;
import org.dromara.daxpay.platform.core.util.ValidationUtil;
import org.dromara.daxpay.platform.core.validation.ValidationGroup;
import org.dromara.daxpay.platform.iam.param.role.RoleParam;
import org.dromara.daxpay.platform.iam.param.role.RoleQuery;
import org.dromara.daxpay.platform.iam.result.role.RoleResult;
import org.dromara.daxpay.platform.iam.service.role.RoleQueryService;
import org.dromara.daxpay.platform.iam.service.role.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@PermCode(menuCode = "iam:role")
@Validated
@Tag(name = "角色管理")
@RestController
@RequestMapping("/role")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    private final RoleQueryService roleQueryService;

    @PermCode(code = "manage", nameCn = "角色管理", nameEn = "Role Manage")
    @Operation(summary = "添加角色")
    @PostMapping(value = "/add")
    public Result<Void> add(@RequestBody @Validated(ValidationGroup.add.class) RoleParam roleParam) {
        ValidationUtil.validateParam(roleParam);
        roleService.add(roleParam);
        return Res.ok();
    }

    @PermCode(code = "manage", nameCn = "角色管理", nameEn = "Role Manage")
    @Operation(summary = "修改角色")
    @PostMapping(value = "/update")
    public Result<Void> update(@RequestBody @Validated(ValidationGroup.edit.class) RoleParam roleParam) {
        ValidationUtil.validateParam(roleParam);
        roleService.update(roleParam);
        return Res.ok();
    }

    @PermCode(code = "view", nameCn = "角色查看", nameEn = "Role View")
    @Operation(summary = "分页查询")
    @GetMapping("/page")
    public Result<PageResult<RoleResult>> page(PageParam pageParam, RoleQuery query) {
        return Res.ok(roleQueryService.page(pageParam, query));
    }

    @PermCode(code = "manage", nameCn = "角色管理", nameEn = "Role Manage")
    @Operation(summary = "删除角色")
    @PostMapping(value = "/delete")
    public Result<Void> delete(@NotNull(message = "{validation.field.id.notNull}") Long id) {
        roleService.delete(id);
        return Res.ok();
    }

    @PermCode(code = "view", nameCn = "角色查看", nameEn = "Role View")
    @Operation(summary = "通过ID查询角色")
    @GetMapping(value = "/get")
    public Result<RoleResult> findById(@NotNull(message = "{validation.field.id.notNull}") Long id) {
        return Res.ok(roleQueryService.findById(id));
    }

    @PermCode(code = "view", nameCn = "角色查看", nameEn = "Role View")
    @Operation(summary = "查询所有的角色")
    @GetMapping(value = "/all")
    public Result<List<RoleResult>> findAll() {
        return Res.ok(roleQueryService.findAll());
    }

    @PermCode(code = "view", nameCn = "角色查看", nameEn = "Role View")
    @Operation(summary = "角色下拉框")
    @GetMapping(value = "/dropdown")
    public Result<List<KeyValue>> dropdown() {
        return Res.ok(roleQueryService.dropdown());
    }

    @PermCode(code = "view", nameCn = "角色查看", nameEn = "Role View")
    @Operation(summary = "编码是否被使用")
    @GetMapping("/exists-by-code")
    public Result<Boolean> existsByCode(@NotBlank(message = "{validation.field.code.notBlank}") @Parameter(description = "编码") String code) {
        return Res.ok(roleQueryService.existsByCode(code));
    }

    @PermCode(code = "view", nameCn = "角色查看", nameEn = "Role View")
    @Operation(summary = "编码是否被使用(不包含自己)")
    @GetMapping("/exists-by-code-not-id")
    public Result<Boolean> existsByCode(
        @NotBlank(message = "{validation.field.code.notBlank}") @Parameter(description = "编码") String code,
        @NotNull(message = "{validation.field.id.notNull}") @Parameter(description = "主键") Long id) {
        return Res.ok(roleQueryService.existsByCode(code, id));
    }

    @PermCode(code = "view", nameCn = "角色查看", nameEn = "Role View")
    @Operation(summary = "中文名称是否被使用")
    @GetMapping("/exists-by-name-cn")
    public Result<Boolean> existsByNameCn(@NotBlank(message = "{validation.field.nameCn.notBlank}") @Parameter(description = "中文名称") String nameCn) {
        return Res.ok(roleQueryService.existsByNameCn(nameCn));
    }

    @PermCode(code = "view", nameCn = "角色查看", nameEn = "Role View")
    @Operation(summary = "中文名称是否被使用(不包含自己)")
    @GetMapping("/exists-by-name-cn-not-id")
    public Result<Boolean> existsByNameCn(
        @NotBlank(message = "{validation.field.nameCn.notBlank}") @Parameter(description = "中文名称") String nameCn,
        @NotNull(message = "{validation.field.id.notNull}") @Parameter(description = "主键") Long id) {
        return Res.ok(roleQueryService.existsByNameCn(nameCn, id));
    }

    @PermCode(code = "view", nameCn = "角色查看", nameEn = "Role View")
    @Operation(summary = "英文名称是否被使用")
    @GetMapping("/exists-by-name-en")
    public Result<Boolean> existsByNameEn(@NotBlank(message = "{validation.field.nameEn.notBlank}") @Parameter(description = "英文名称") String nameEn) {
        return Res.ok(roleQueryService.existsByNameEn(nameEn));
    }

    @PermCode(code = "view", nameCn = "角色查看", nameEn = "Role View")
    @Operation(summary = "英文名称是否被使用(不包含自己)")
    @GetMapping("/exists-by-name-en-not-id")
    public Result<Boolean> existsByNameEn(
        @NotBlank(message = "{validation.field.nameEn.notBlank}") @Parameter(description = "英文名称") String nameEn,
        @NotNull(message = "{validation.field.id.notNull}") @Parameter(description = "主键") Long id) {
        return Res.ok(roleQueryService.existsByNameEn(nameEn, id));
    }
}
