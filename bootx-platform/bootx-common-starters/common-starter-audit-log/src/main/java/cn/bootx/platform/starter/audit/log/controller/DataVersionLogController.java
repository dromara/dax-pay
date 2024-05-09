package cn.bootx.platform.starter.audit.log.controller;

import cn.bootx.platform.common.core.rest.PageResult;
import cn.bootx.platform.common.core.rest.Res;
import cn.bootx.platform.common.core.rest.ResResult;
import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.starter.audit.log.dto.DataVersionLogDto;
import cn.bootx.platform.starter.audit.log.param.DataVersionLogParam;
import cn.bootx.platform.starter.audit.log.service.DataVersionLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xxm
 * @since 2022/1/10
 */
@Tag(name = "数据版本日志")
@RestController
@RequestMapping("/log/dataVersion")
@RequiredArgsConstructor
public class DataVersionLogController {

    private final DataVersionLogService service;

    @Operation(summary = "分页")
    @GetMapping("/page")
    public ResResult<PageResult<DataVersionLogDto>> page(@ParameterObject PageParam pageParam,
            @ParameterObject DataVersionLogParam param) {
        return Res.ok(service.page(pageParam, param));
    }

    @Operation(summary = "获取")
    @GetMapping("/findById")
    public ResResult<DataVersionLogDto> findById(Long id) {
        return Res.ok(service.findById(id));
    }

}
