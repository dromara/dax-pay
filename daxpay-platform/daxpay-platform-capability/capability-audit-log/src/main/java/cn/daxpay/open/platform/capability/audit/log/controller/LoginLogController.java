package cn.daxpay.open.platform.capability.audit.log.controller;

import cn.daxpay.open.platform.core.annotation.PermCode;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import cn.daxpay.open.platform.core.rest.result.Result;
import cn.daxpay.open.platform.capability.audit.log.param.LoginLogQuery;
import cn.daxpay.open.platform.capability.audit.log.result.LoginLogResult;
import cn.daxpay.open.platform.capability.audit.log.service.log.LoginLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@PermCode(menuCode = "system:log:login")
@Validated
@Tag(name = "登录日志")
@RestController
@RequestMapping("/log/login")
@RequiredArgsConstructor
public class LoginLogController {

    private final LoginLogService loginLogService;

    @PermCode(code = "view", nameCn = "登录日志查看", nameEn = "Login Log View")
    @Operation(summary = "分页")
    @GetMapping("/page")
    public Result<PageResult<LoginLogResult>> page(PageParam pageParam, LoginLogQuery query) {
        return Res.ok(loginLogService.page(pageParam, query));
    }

    @PermCode(code = "view", nameCn = "登录日志查看", nameEn = "Login Log View")
    @Operation(summary = "获取")
    @GetMapping("/get")
    public Result<LoginLogResult> findById(@NotNull(message = "{validation.field.id.notNull}") Long id) {
        return Res.ok(loginLogService.findById(id));
    }

    @PermCode(code = "manage", nameCn = "登录日志管理", nameEn = "Login Log Manage")
    @Operation(summary = "清除指定天数之前的日志")
    @PostMapping("/delete-by-day")
    public Result<Void> deleteByDay(@NotNull(message = "{validation.field.deleteDay.notNull}") Integer deleteDay){
        loginLogService.deleteByDay(deleteDay);
        return Res.ok();
    }
}
