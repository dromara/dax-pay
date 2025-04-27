package org.dromara.daxpay.controller.config;

import cn.bootx.platform.core.annotation.RequestGroup;
import cn.bootx.platform.core.annotation.RequestPath;
import cn.bootx.platform.core.rest.Res;
import cn.bootx.platform.core.rest.dto.LabelValue;
import cn.bootx.platform.core.rest.result.Result;
import org.dromara.daxpay.service.result.config.ChannelConfigResult;
import org.dromara.daxpay.service.service.config.ChannelConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 通道配置
 * @author xxm
 * @since 2024/6/25
 */
@Validated
@Tag(name = "通道配置")
@RestController
@RequestGroup(groupCode = "ChannelConfig", groupName = "通道配置", moduleCode = "PayConfig")
@RequestMapping("/channel/config")
@RequiredArgsConstructor
public class ChannelConfigController {
    private final ChannelConfigService channelConfigService;

    @RequestPath("根据应用AppId查询配置列表")
    @Operation(summary = "根据应用AppId查询配置列表")
    @GetMapping("/findAllByAppId")
    public Result<List<ChannelConfigResult>> findAllByAppId(@NotNull(message = "应用AppId不可为空") @Parameter(description = "应用AppId") String appId){
        return Res.ok(channelConfigService.findAllByAppId(appId));
    }

    @RequestPath("根据应用AppId获取启用的通道")
    @Operation(summary = "根据应用AppId获取启用的通道")
    @GetMapping("/dropdownByEnable")
    public Result<List<LabelValue>> dropdownByEnable(@NotNull(message = "应用AppId不可为空") @Parameter(description = "应用AppId") String appId){
        return Res.ok(channelConfigService.dropdownByEnable(appId));
    }
}

