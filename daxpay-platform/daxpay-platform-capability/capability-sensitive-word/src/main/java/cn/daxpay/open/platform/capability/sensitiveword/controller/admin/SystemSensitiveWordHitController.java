package cn.daxpay.open.platform.capability.sensitiveword.controller.admin;

import cn.daxpay.open.platform.capability.sensitiveword.param.SystemSensitiveWordHitQuery;
import cn.daxpay.open.platform.capability.sensitiveword.result.SystemSensitiveWordHitResult;
import cn.daxpay.open.platform.capability.sensitiveword.service.SystemSensitiveWordHitService;
import cn.daxpay.open.platform.core.annotation.PermCode;
import cn.daxpay.open.platform.core.code.PermCodes;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import cn.daxpay.open.platform.core.rest.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/// # 敏感词命中记录（运营端，只读）
///
@PermCode(menuCode = PermCodes.System.SensitiveWordHit.MENU)
@Validated
@Tag(name = "敏感词命中记录")
@RestController
@RequestMapping("/admin/system/sensitive-word-hit")
@RequiredArgsConstructor
public class SystemSensitiveWordHitController {

    private final SystemSensitiveWordHitService systemSensitiveWordHitService;

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "分页查询")
    @GetMapping("/page")
    public Result<PageResult<SystemSensitiveWordHitResult>> page(
            PageParam pageParam, SystemSensitiveWordHitQuery query) {
        return Res.ok(systemSensitiveWordHitService.page(pageParam, query));
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "详情")
    @GetMapping("/get-by-id")
    public Result<SystemSensitiveWordHitResult> getById(
            @NotNull(message = "{validation.field.id.notNull}") Long id) {
        return Res.ok(systemSensitiveWordHitService.findById(id));
    }
}

