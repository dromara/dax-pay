package cn.bootx.platform.starter.audit.log.controller;

import cn.bootx.platform.core.annotation.RequestGroup;
import cn.bootx.platform.core.annotation.RequestPath;
import cn.bootx.platform.core.rest.Res;
import cn.bootx.platform.core.rest.param.PageParam;
import cn.bootx.platform.core.rest.result.PageResult;
import cn.bootx.platform.core.rest.result.Result;
import cn.bootx.platform.starter.audit.log.param.OperateLogQuery;
import cn.bootx.platform.starter.audit.log.result.OperateLogResult;
import cn.bootx.platform.starter.audit.log.service.log.OperateLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 操作日志
 *
 * @author xxm
 * @since 2021/9/8
 */
@Validated
@Tag(name = "操作日志")
@RestController
@RequestMapping("/log/operate")
@RequestGroup(groupCode = "operateLog", groupName = "操作日志", moduleCode = "starter")
@RequiredArgsConstructor
public class OperateLogController {

    private final OperateLogService operateLogService;

    @RequestPath("操作日志分页")
    @Operation(summary = "分页")
    @GetMapping("/page")
    public Result<PageResult<OperateLogResult>> page(PageParam pageParam, OperateLogQuery operateLogParam) {
        return Res.ok(operateLogService.page(pageParam, operateLogParam));
    }

    @RequestPath("获取日志分页")
    @Operation(summary = "获取")
    @GetMapping("/findById")
    public Result<OperateLogResult> findById(@NotNull(message = "主键不可为空") Long id) {
        return Res.ok(operateLogService.findById(id));
    }


    @RequestPath("清除指定天数的操作日志")
    @Operation(summary = "清除指定天数的日志")
    @PostMapping("/deleteByDay")
    public Result<Void> deleteByDay(@NotNull(message = "天数不可为空") Integer deleteDay){
        operateLogService.deleteByDay(deleteDay);
        return Res.ok();
    }
}
