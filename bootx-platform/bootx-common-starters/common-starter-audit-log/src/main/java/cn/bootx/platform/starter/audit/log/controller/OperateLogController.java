package cn.bootx.platform.starter.audit.log.controller;

import cn.bootx.platform.common.core.rest.PageResult;
import cn.bootx.platform.common.core.rest.Res;
import cn.bootx.platform.common.core.rest.ResResult;
import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.starter.audit.log.dto.OperateLogDto;
import cn.bootx.platform.starter.audit.log.param.OperateLogParam;
import cn.bootx.platform.starter.audit.log.service.OperateLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 操作日志
 *
 * @author xxm
 * @since 2021/9/8
 */
@Tag(name = "操作日志")
@RestController
@RequestMapping("/log/operate")
@RequiredArgsConstructor
public class OperateLogController {

    private final OperateLogService operateLogService;

    @Operation(summary = "分页")
    @GetMapping("/page")
    public ResResult<PageResult<OperateLogDto>> page(@ParameterObject PageParam pageParam,
            @ParameterObject OperateLogParam operateLogParam) {
        return Res.ok(operateLogService.page(pageParam, operateLogParam));
    }

    @Operation(summary = "获取")
    @GetMapping("/findById")
    public ResResult<OperateLogDto> findById(Long id) {
        return Res.ok(operateLogService.findById(id));
    }


    @Operation(summary = "清除指定天数的日志")
    @DeleteMapping("/deleteByDay")
    public ResResult<Void> deleteByDay(Integer type){
        operateLogService.deleteByDay(type);
        return Res.ok();
    }
}
