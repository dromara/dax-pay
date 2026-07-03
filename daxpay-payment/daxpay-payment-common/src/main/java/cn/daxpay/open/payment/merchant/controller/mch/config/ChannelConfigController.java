package cn.daxpay.open.payment.merchant.controller.mch.config;

import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.dto.LabelValue;
import cn.daxpay.open.platform.core.rest.result.Result;
import cn.daxpay.open.payment.merchant.result.config.ChannelConfigResult;
import cn.daxpay.open.payment.merchant.service.config.ChannelConfigService;
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

/// # 商户通道配置
///
@Validated
@Tag(name = "商户通道配置")
@RestController
@RequestMapping("/channel/config")
@RequiredArgsConstructor
public class ChannelConfigController {
    private final ChannelConfigService channelConfigService;

    @Operation(summary = "根据应用AppId查询配置列表")
    @GetMapping("/all-by-app-id")
    public Result<List<ChannelConfigResult>> findAllByAppId(@NotNull(message = "{validation.field.appId.notBlank}") @Parameter(description = "应用AppId") String appId){
        return Res.ok(channelConfigService.findAllByAppId(appId));
    }

    @Operation(summary = "根据应用AppId获取启用的通道")
    @GetMapping("/dropdown-by-enable")
    public Result<List<LabelValue>> dropdownByEnable(@NotNull(message = "{validation.field.appId.notBlank}") @Parameter(description = "应用AppId") String appId){
        return Res.ok(channelConfigService.dropdownByEnable(appId));
    }

}

