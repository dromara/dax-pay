package cn.bootx.platform.starter.quartz.controller;

import cn.bootx.platform.common.core.rest.PageResult;
import cn.bootx.platform.common.core.rest.Res;
import cn.bootx.platform.common.core.rest.ResResult;
import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.starter.quartz.core.service.QuartzJobService;
import cn.bootx.platform.starter.quartz.dto.QuartzJobDto;
import cn.bootx.platform.starter.quartz.param.QuartzJobParam;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 定时任务
 *
 * @author xxm
 * @since 2021/11/2
 */
@Tag(name = "定时任务")
@RestController
@RequestMapping("/quartz")
@RequiredArgsConstructor
public class QuartzJobController {

    private final QuartzJobService quartzJobService;

    @Operation(summary = "添加")
    @PostMapping("/add")
    public ResResult<Void> add(@RequestBody QuartzJobParam param) {
        quartzJobService.add(param);
        return Res.ok();
    }

    @Operation(summary = "更新")
    @PostMapping("/update")
    public ResResult<Void> update(@RequestBody QuartzJobParam param) {
        quartzJobService.update(param);
        return Res.ok();
    }

    @Operation(summary = "分页")
    @GetMapping("/page")
    public ResResult<PageResult<QuartzJobDto>> page(PageParam pageParam, QuartzJobParam param) {
        return Res.ok(quartzJobService.page(pageParam, param));
    }

    @Operation(summary = "单条")
    @GetMapping("/findById")
    public ResResult<QuartzJobDto> findById(Long id) {
        return Res.ok(quartzJobService.findById(id));
    }

    @Operation(summary = "启动")
    @PostMapping("/start")
    public ResResult<Void> start(Long id) {
        quartzJobService.start(id);
        return Res.ok();
    }

    @Operation(summary = "停止")
    @PostMapping("/stop")
    public ResResult<Void> stop(Long id) {
        quartzJobService.stop(id);
        return Res.ok();
    }

    @Operation(summary = "立即执行")
    @PostMapping("/execute")
    public ResResult<Void> execute(Long id) {
        quartzJobService.execute(id);
        return Res.ok();
    }

    @Operation(summary = "删除")
    @DeleteMapping("/delete")
    public ResResult<Void> delete(Long id) {
        quartzJobService.delete(id);
        return Res.ok();
    }

    @Operation(summary = "判断是否是定时任务类")
    @GetMapping("/judgeJobClass")
    public ResResult<String> judgeJobClass(String jobClassName) {
        return Res.ok(quartzJobService.judgeJobClass(jobClassName));
    }

    @Operation(summary = "同步定时任务状态")
    @PostMapping("/syncJobStatus")
    public ResResult<Void> syncJobStatus() {
        quartzJobService.syncJobStatus();
        return Res.ok();
    }

}
