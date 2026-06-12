package org.dromara.daxpay.platform.system.controller.config;

import org.dromara.daxpay.platform.core.rest.Res;
import org.dromara.daxpay.platform.core.rest.result.Result;
import org.dromara.daxpay.platform.system.param.config.PlatformSmsConfigParam;
import org.dromara.daxpay.platform.system.result.config.platform.PlatformSmsConfigResult;
import org.dromara.daxpay.platform.system.service.config.PlatformSmsConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/// # 平台短信配置控制器
///
@Tag(name = "平台短信配置")
@Validated
@RestController
@RequestMapping("/platform/config/sms")
@RequiredArgsConstructor
public class PlatformSmsConfigController {
    private final PlatformSmsConfigService smsConfigService;

    /// 添加短信配置
    @Operation(summary = "添加短信配置")
    @PostMapping("/add")
    public Result<Void> add(@RequestBody @Validated PlatformSmsConfigParam param) {
        smsConfigService.add(param);
        return Res.ok();
    }

    /// 更新短信配置
    @Operation(summary = "更新短信配置")
    @PostMapping("/update")
    public Result<Void> update(@RequestBody @Validated PlatformSmsConfigParam param) {
        smsConfigService.update(param);
        return Res.ok();
    }

    /// 设置短信配置为启用
    @Operation(summary = "设置短信配置为启用")
    @PostMapping("/enable")
    public Result<Void> enable(@NotNull(message = "{validation.field.smsConfigId.notNull}") Long id) {
        smsConfigService.setupEnable(id);
        return Res.ok();
    }

    /// 取消短信配置启用
    @Operation(summary = "取消短信配置启用")
    @PostMapping("/disable")
    public Result<Void> disable(@NotNull(message = "{validation.field.smsConfigId.notNull}") Long id) {
        smsConfigService.clearEnable(id);
        return Res.ok();
    }

    /// 删除短信配置
    @Operation(summary = "删除短信配置")
    @PostMapping("/delete")
    public Result<Void> delete(@NotNull(message = "{validation.field.smsConfigId.notNull}") Long id) {
        smsConfigService.delete(id);
        return Res.ok();
    }

    /// 获取短信配置
    @Operation(summary = "获取短信配置")
    @GetMapping("/get")
    public Result<PlatformSmsConfigResult> findById(@NotNull(message = "{validation.field.smsConfigId.notNull}") Long id) {
        return Res.ok(smsConfigService.findById(id));
    }

    /// 获取所有短信配置
    @Operation(summary = "获取所有短信配置")
    @GetMapping("/list")
    public Result<List<PlatformSmsConfigResult>> list() {
        return Res.ok(smsConfigService.findAll());
    }
}
