package org.dromara.daxpay.platform.capability.audit.log.controller;

import org.dromara.daxpay.platform.core.annotation.PermCode;
import org.dromara.daxpay.platform.core.rest.Res;
import org.dromara.daxpay.platform.core.rest.param.PageParam;
import org.dromara.daxpay.platform.core.rest.result.PageResult;
import org.dromara.daxpay.platform.core.rest.result.Result;
import org.dromara.daxpay.platform.capability.audit.log.param.OperateLogQuery;
import org.dromara.daxpay.platform.capability.audit.log.result.OperateLogResult;
import org.dromara.daxpay.platform.capability.audit.log.service.log.OperateLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/// # 操作日志
///
@PermCode(menuCode = "starter:log:operate")
@Validated
@Tag(name = "操作日志")
@RestController
@RequestMapping("/log/operate")
@RequiredArgsConstructor
public class OperateLogController {

    private final OperateLogService operateLogService;

    @PermCode(code = "view", nameCn = "操作日志查看", nameEn = "Operate Log View")
    @Operation(summary = "分页")
    @GetMapping("/page")
    public Result<PageResult<OperateLogResult>> page(PageParam pageParam, OperateLogQuery operateLogParam) {
        return Res.ok(operateLogService.page(pageParam, operateLogParam));
    }

    @PermCode(code = "view", nameCn = "操作日志查看", nameEn = "Operate Log View")
    @Operation(summary = "获取")
    @GetMapping("/get")
    public Result<OperateLogResult> findById(@NotNull(message = "{validation.field.id.notNull}") Long id) {
        return Res.ok(operateLogService.findById(id));
    }

    @PermCode(code = "manage", nameCn = "操作日志管理", nameEn = "Operate Log Manage")
    @Operation(summary = "清除指定天数的日志")
    @PostMapping("/delete-by-day")
    public Result<Void> deleteByDay(@NotNull(message = "{validation.field.deleteDay.notNull}") Integer deleteDay){
        operateLogService.deleteByDay(deleteDay);
        return Res.ok();
    }
}
