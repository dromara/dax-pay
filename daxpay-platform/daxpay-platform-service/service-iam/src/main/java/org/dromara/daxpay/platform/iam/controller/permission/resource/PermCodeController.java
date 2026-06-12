package org.dromara.daxpay.platform.iam.controller.permission.resource;

import org.dromara.daxpay.platform.core.annotation.IgnoreAuth;
import org.dromara.daxpay.platform.core.annotation.PermCode;
import org.dromara.daxpay.platform.core.entity.UserDetail;
import org.dromara.daxpay.platform.core.rest.Res;
import org.dromara.daxpay.platform.core.rest.result.Result;
import org.dromara.daxpay.platform.iam.param.permission.resource.PermCodeScanParam;
import org.dromara.daxpay.platform.iam.result.permission.resource.MenuPermCodeItemResult;
import org.dromara.daxpay.platform.iam.result.permission.resource.PermCodeScanResult;
import org.dromara.daxpay.platform.iam.service.permission.resource.PermCodeScanService;
import org.dromara.daxpay.platform.iam.service.permission.resource.PermCodeService;
import org.dromara.daxpay.platform.iam.service.upms.UserRolePremService;
import org.dromara.daxpay.platform.capability.auth.util.SecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/// # 权限码管理
///
@PermCode(menuCode = "iam:perm:menu")
@Validated
@Tag(name = "权限码管理")
@RestController
@RequestMapping("/perm/code")
@RequiredArgsConstructor
public class PermCodeController {

    private final PermCodeService permCodeService;
    private final UserRolePremService userRoleService;
    private final PermCodeScanService permCodeScanService;

    @PermCode(code = "manage", nameCn = "菜单管理", nameEn = "Menu Manage")
    @Operation(summary = "手动扫描同步权限码")
    @PostMapping("/scan")
    public Result<PermCodeScanResult> scan() {
        return Res.ok(permCodeScanService.scan(new PermCodeScanParam()));
    }

    @Operation(summary = "根据菜单查询权限码列表")
    @GetMapping("/get-by-menu")
    public Result<List<MenuPermCodeItemResult>> findByMenu(
            @NotNull(message = "{validation.field.menuId.notNull}") @Parameter(description = "菜单ID") Long menuId) {
        return Res.ok(permCodeService.findByMenu(menuId));
    }

    @IgnoreAuth(login = true)
    @Operation(summary = "根据用户获取权限码")
    @GetMapping("/find-codes-by-user")
    public Result<List<String>> findCodesByUser() {
        UserDetail user = SecurityUtil.getUser();
        if (user.isAdmin()) {
            return Res.ok(permCodeService.findAllCode());
        }
        return Res.ok(userRoleService.findAllCodesByUser(user.getId()));
    }
}
