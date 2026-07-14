package cn.daxpay.open.platform.iam.controller.role;

import cn.daxpay.open.platform.core.code.PermCodes;
import cn.daxpay.open.platform.core.annotation.PermCode;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.dto.KeyValue;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import cn.daxpay.open.platform.core.rest.result.Result;
import cn.daxpay.open.platform.core.util.ValidationUtil;
import cn.daxpay.open.platform.core.validation.ValidationGroup;
import cn.daxpay.open.platform.iam.param.role.RoleParam;
import cn.daxpay.open.platform.iam.param.role.RoleQuery;
import cn.daxpay.open.platform.iam.result.role.RoleResult;
import cn.daxpay.open.platform.iam.service.role.RoleQueryService;
import cn.daxpay.open.platform.iam.service.role.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@PermCode(menuCode = PermCodes.Iam.Role.MENU)
@Validated
@Tag(name = "角色管理")
@RestController
@RequestMapping("/role")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    private final RoleQueryService roleQueryService;

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "添加角色")
    @PostMapping(value = "/add")
    public Result<Void> add(@RequestBody @Validated(ValidationGroup.add.class) RoleParam roleParam) {
        ValidationUtil.validateParam(roleParam);
        roleService.add(roleParam);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "修改角色")
    @PostMapping(value = "/update")
    public Result<Void> update(@RequestBody @Validated(ValidationGroup.edit.class) RoleParam roleParam) {
        ValidationUtil.validateParam(roleParam);
        roleService.update(roleParam);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "分页查询")
    @GetMapping("/page")
    public Result<PageResult<RoleResult>> page(PageParam pageParam, RoleQuery query) {
        return Res.ok(roleQueryService.page(pageParam, query));
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "删除角色")
    @PostMapping(value = "/delete")
    public Result<Void> delete(@NotNull(message = "{validation.field.id.notNull}") Long id) {
        roleService.delete(id);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "通过ID查询角色")
    @GetMapping(value = "/get")
    public Result<RoleResult> findById(@NotNull(message = "{validation.field.id.notNull}") Long id) {
        return Res.ok(roleQueryService.findById(id));
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "查询所有的角色")
    @GetMapping(value = "/all")
    public Result<List<RoleResult>> findAll() {
        return Res.ok(roleQueryService.findAll());
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "角色下拉框")
    @GetMapping(value = "/dropdown")
    public Result<List<KeyValue>> dropdown() {
        return Res.ok(roleQueryService.dropdown());
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "编码是否被使用")
    @GetMapping("/exists-by-code")
    public Result<Boolean> existsByCode(@NotBlank(message = "{validation.field.code.notBlank}") @Parameter(description = "编码") String code) {
        return Res.ok(roleQueryService.existsByCode(code));
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "编码是否被使用(不包含自己)")
    @GetMapping("/exists-by-code-not-id")
    public Result<Boolean> existsByCode(
        @NotBlank(message = "{validation.field.code.notBlank}") @Parameter(description = "编码") String code,
        @NotNull(message = "{validation.field.id.notNull}") @Parameter(description = "主键") Long id) {
        return Res.ok(roleQueryService.existsByCode(code, id));
    }
}
