package cn.daxpay.open.platform.iam.controller.permission.assign;

import cn.daxpay.open.platform.core.annotation.InternalPath;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.result.Result;
import cn.daxpay.open.platform.core.util.ValidationUtil;
import cn.daxpay.open.platform.iam.param.permission.assign.RoleUnifiedAssignParam;
import cn.daxpay.open.platform.iam.result.permission.assign.RoleUnifiedAssignResult;
import cn.daxpay.open.platform.iam.service.permission.assign.RoleUnifiedAssignService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/// # 角色统一授权
///
@Validated
@Tag(name = "角色统一授权")
@RestController
@RequestMapping("/role/perm")
@RequiredArgsConstructor
public class RoleUnifiedAssignController {

    private final RoleUnifiedAssignService roleUnifiedAssignService;

    @InternalPath
    @Operation(summary = "查询角色统一授权数据")
    @GetMapping("/get-by-role")
    public Result<RoleUnifiedAssignResult> getByRole(
            @NotNull(message = "{validation.field.roleId.notNull}") @Parameter(description = "角色ID") Long roleId,
            @NotBlank(message = "{validation.field.clientCode.notBlank}") @Parameter(description = "终端编码") String clientCode) {
        return Res.ok(roleUnifiedAssignService.getByRole(roleId, clientCode));
    }

    @InternalPath
    @Operation(summary = "保存角色统一授权")
    @PostMapping("/save")
    public Result<Void> save(@RequestBody @Validated RoleUnifiedAssignParam param) {
        ValidationUtil.validateParam(param);
        roleUnifiedAssignService.save(param);
        return Res.ok();
    }
}
