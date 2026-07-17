package cn.daxpay.open.platform.capability.audit.log.controller;

import cn.daxpay.open.platform.capability.audit.log.param.UnipayApiLogQuery;
import cn.daxpay.open.platform.capability.audit.log.result.UnipayApiLogResult;
import cn.daxpay.open.platform.capability.audit.log.service.log.UnipayApiLogService;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/// # 统一支付接口审计日志
///
@PermCode(menuCode = PermCodes.System.Log.Unipay.MENU)
@Validated
@Tag(name = "支付接口日志")
@RestController
@RequestMapping("/log/unipay")
@RequiredArgsConstructor
public class UnipayApiLogController {

    private final UnipayApiLogService unipayApiLogService;

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "分页")
    @GetMapping("/page")
    public Result<PageResult<UnipayApiLogResult>> page(PageParam pageParam, UnipayApiLogQuery query) {
        return Res.ok(unipayApiLogService.page(pageParam, query));
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "获取")
    @GetMapping("/get")
    public Result<UnipayApiLogResult> findById(@NotNull(message = "{validation.field.id.notNull}") Long id) {
        return Res.ok(unipayApiLogService.findById(id));
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "清除指定天数之前的日志")
    @PostMapping("/delete-by-day")
    public Result<Void> deleteByDay(@NotNull(message = "{validation.field.deleteDay.notNull}") Integer deleteDay) {
        unipayApiLogService.deleteByDay(deleteDay);
        return Res.ok();
    }
}
