package cn.bootx.platform.starter.audit.log.controller;

import cn.bootx.platform.common.core.rest.PageResult;
import cn.bootx.platform.common.core.rest.Res;
import cn.bootx.platform.common.core.rest.ResResult;
import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.starter.audit.log.dto.LoginLogDto;
import cn.bootx.platform.starter.audit.log.param.LoginLogParam;
import cn.bootx.platform.starter.audit.log.service.LoginLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xxm
 * @since 2021/9/7
 */
@Tag(name = "登录日志")
@RestController
@RequestMapping("/log/login")
@RequiredArgsConstructor
public class LoginLogController {

    private final LoginLogService loginLogService;

    @Operation(summary = "分页")
    @GetMapping("/page")
    public ResResult<PageResult<LoginLogDto>> page(@ParameterObject PageParam pageParam, @ParameterObject LoginLogParam loginLogParam) {
        return Res.ok(loginLogService.page(pageParam, loginLogParam));
    }

    @Operation(summary = "获取")
    @GetMapping("/findById")
    public ResResult<LoginLogDto> findById(Long id) {
        return Res.ok(loginLogService.findById(id));
    }

    @Operation(summary = "清除指定天数之前的日志")
    @DeleteMapping("/deleteByDay")
    public ResResult<Void> deleteByDay(int deleteDay){
        loginLogService.deleteByDay(deleteDay);
        return Res.ok();
    }
}
