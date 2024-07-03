package cn.bootx.platform.starter.quartz.controller;

import cn.bootx.platform.core.rest.Res;
import cn.bootx.platform.core.rest.param.PageParam;
import cn.bootx.platform.core.rest.result.PageResult;
import cn.bootx.platform.core.rest.result.Result;
import cn.bootx.platform.starter.quartz.service.QuartzJobLogService;
import cn.bootx.platform.starter.quartz.result.QuartzJobLogResult;
import cn.bootx.platform.starter.quartz.param.QuartzJobLogQuery;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xxm
 * @since 2022/5/2
 */
@Tag(name = "定时任务执行日志")
@RestController
@RequestMapping("/quartz/log")
@RequiredArgsConstructor
public class QuartzJobLogController {

    private final QuartzJobLogService quartzJobLogService;

    @Operation(summary = "分页")
    @GetMapping("/page")
    public Result<PageResult<QuartzJobLogResult>> page(PageParam pageParam, QuartzJobLogQuery param) {
        return Res.ok(quartzJobLogService.page(pageParam, param));
    }

    @Operation(summary = "单条")
    @GetMapping("/findById")
    public Result<QuartzJobLogResult> findById(Long id) {
        return Res.ok(quartzJobLogService.findById(id));
    }

}
