package org.dromara.daxpay.channel.alipay.controller;

import cn.bootx.platform.core.annotation.RequestGroup;
import cn.bootx.platform.core.annotation.RequestPath;
import cn.bootx.platform.core.rest.Res;
import cn.bootx.platform.core.rest.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.dromara.daxpay.channel.alipay.param.config.AlipayConfigParam;
import org.dromara.daxpay.channel.alipay.result.config.AlipayConfigResult;
import org.dromara.daxpay.channel.alipay.service.config.AlipayConfigService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 支付宝配置控制器
 * @author xxm
 * @since 2024/6/26
 */
@Validated
@Tag(name = "支付宝配置控制器")
@RestController
@RequestMapping("/alipay/config")
@RequiredArgsConstructor
@RequestGroup(groupCode = "AlipayConfig", groupName = "支付宝配置", moduleCode = "alipay", moduleName = "支付宝通道")
public class AlipayConfigController {
    private final AlipayConfigService alipayConfigService;

    @RequestPath("获取配置")
    @Operation(summary = "获取配置")
    @GetMapping("/findById")
    public Result<AlipayConfigResult> findById(@NotNull(message = "ID不可为空") Long id) {
        return Res.ok(alipayConfigService.findById(id));
    }

    @RequestPath("新增或更新")
    @Operation(summary = "新增或更新")
    @PostMapping("/saveOrUpdate")
    public Result<Void> saveOrUpdate(@RequestBody AlipayConfigParam param) {
        alipayConfigService.saveOrUpdate(param);
        return Res.ok();
    }

}
