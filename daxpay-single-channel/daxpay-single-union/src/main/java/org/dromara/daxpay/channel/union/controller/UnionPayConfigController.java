package org.dromara.daxpay.channel.union.controller;

import cn.bootx.platform.core.annotation.RequestGroup;
import cn.bootx.platform.core.annotation.RequestPath;
import cn.bootx.platform.core.rest.Res;
import cn.bootx.platform.core.rest.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.dromara.daxpay.channel.union.param.config.UnionPayConfigParam;
import org.dromara.daxpay.channel.union.result.UnionPayConfigResult;
import org.dromara.daxpay.channel.union.service.config.UnionPayConfigService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 云闪付支付配置
 * @author xxm
 * @since 2024/9/12
 */
@Validated
@Tag(name = "云闪付支付配置")
@RestController
@RequestGroup(groupCode = "UnionPayConfig", groupName = "云闪付配置", moduleCode = "UnionPay", moduleName = "云闪付")
@RequestMapping("/union/pay/config")
@RequiredArgsConstructor
public class UnionPayConfigController {
    private final UnionPayConfigService configService;

    @RequestPath("获取配置")
    @Operation(summary = "获取配置")
    @GetMapping("/findById")
    public Result<UnionPayConfigResult> findById(@NotNull(message = "ID不可为空") Long id) {
        return Res.ok(configService.findById(id));
    }

    @RequestPath("新增或更新")
    @Operation(summary = "新增或更新")
    @PostMapping("/saveOrUpdate")
    public Result<Void> saveOrUpdate(@RequestBody UnionPayConfigParam param) {
        configService.saveOrUpdate(param);
        return Res.ok();
    }
}
