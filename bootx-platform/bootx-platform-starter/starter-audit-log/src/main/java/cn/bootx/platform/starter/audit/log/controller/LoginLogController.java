package cn.bootx.platform.starter.audit.log.controller;

import cn.bootx.platform.core.annotation.RequestGroup;
import cn.bootx.platform.core.annotation.RequestPath;
import cn.bootx.platform.core.rest.Res;
import cn.bootx.platform.core.rest.param.PageParam;
import cn.bootx.platform.core.rest.result.PageResult;
import cn.bootx.platform.core.rest.result.Result;
import cn.bootx.platform.starter.audit.log.result.LoginLogResult;
import cn.bootx.platform.starter.audit.log.param.LoginLogParam;
import cn.bootx.platform.starter.audit.log.service.log.LoginLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
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
@RequestGroup(groupCode = "loginLog", groupName = "登录日志", moduleCode = "starter", moduleName = "starter模块")
@RequiredArgsConstructor
public class LoginLogController {

    private final LoginLogService loginLogService;

    @RequestPath("登录日志分页")
    @Operation(summary = "分页")
    @GetMapping("/page")
    public Result<PageResult<LoginLogResult>> page(PageParam pageParam, LoginLogParam loginLogParam) {
        return Res.ok(loginLogService.page(pageParam, loginLogParam));
    }

    @RequestPath("获取登录日志")
    @Operation(summary = "获取")
    @GetMapping("/findById")
    public Result<LoginLogResult> findById(Long id) {
        return Res.ok(loginLogService.findById(id));
    }

    @RequestPath("清除指定天数之前的登录日志")
    @Operation(summary = "清除指定天数之前的日志")
    @PostMapping("/deleteByDay")
    public Result<Void> deleteByDay(int deleteDay){
        loginLogService.deleteByDay(deleteDay);
        return Res.ok();
    }
}
