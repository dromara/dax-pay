package cn.bootx.platform.starter.monitor.controller;

import cn.bootx.platform.common.core.rest.Res;
import cn.bootx.platform.common.core.rest.ResResult;
import cn.bootx.platform.starter.monitor.entity.RedisMonitorResult;
import cn.bootx.platform.starter.monitor.entity.SystemMonitorResult;
import cn.bootx.platform.starter.monitor.service.RedisMonitorService;
import cn.bootx.platform.starter.monitor.service.SystemMonitorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xxm
 * @since 2022/6/10
 */
@Tag(name = "系统信息监控")
@RestController
@RequestMapping("/monitor/system")
@RequiredArgsConstructor
public class SystemMonitorController {

    private final SystemMonitorService systemMonitorService;

    private final RedisMonitorService redisMonitorService;

    @Operation(summary = "获取系统消息")
    @GetMapping("/getSystemInfo")
    public ResResult<SystemMonitorResult> getSystemInfo() {
        return Res.ok(systemMonitorService.getSystemInfo());
    }

    @Operation(summary = "获取Redis信息")
    @GetMapping("/getRedisInfo")
    public ResResult<RedisMonitorResult> getRedisInfo() {
        return Res.ok(redisMonitorService.getRedisInfo());
    }

}
