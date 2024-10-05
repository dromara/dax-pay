package org.dromara.daxpay.service.controller.config;

import cn.bootx.platform.core.annotation.RequestGroup;
import cn.bootx.platform.core.annotation.RequestPath;
import cn.bootx.platform.core.rest.Res;
import cn.bootx.platform.core.rest.result.Result;
import org.dromara.daxpay.service.param.config.ChannelCashierConfigParam;
import org.dromara.daxpay.service.result.config.ChannelCashierConfigResult;
import org.dromara.daxpay.service.service.config.ChannelCashierConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 *
 * @author xxm
 * @since 2024/9/28
 */
@Tag(name = "通道支付收银台配置")
@RestController
@RequestGroup(groupCode = "ChannelCashierConfig", groupName = "通道支付收银台配置", moduleCode = "PayConfig", moduleName = "支付配置")
@RequestMapping("/channel/cashier/config")
@RequiredArgsConstructor
public class ChannelCashierConfigController {
    private final ChannelCashierConfigService channelCashierConfigService;

    @RequestPath("配置列表")
    @Operation(summary = "配置列表")
    @GetMapping("/findByAppId")
    public Result<List<ChannelCashierConfigResult>> findByAppId(String appId) {
        return Res.ok(channelCashierConfigService.findByAppId(appId));
    }

    @RequestPath("配置详情")
    @Operation(summary = "配置详情")
    @GetMapping("/findById")
    public Result<ChannelCashierConfigResult> findById(Long id) {
        return Res.ok(channelCashierConfigService.findById(id));
    }

    @RequestPath("配置保存")
    @Operation(summary = "配置保存")
    @PostMapping("/save")
    public Result<Void> save(@RequestBody ChannelCashierConfigParam param) {
        channelCashierConfigService.save(param);
        return Res.ok();
    }

    @RequestPath("配置更新")
    @Operation(summary = "配置更新")
    @PostMapping("/update")
    public Result<Void> update(@RequestBody ChannelCashierConfigParam param) {
        channelCashierConfigService.update(param);
        return Res.ok();
    }

    @RequestPath("配置是否存在")
    @Operation(summary = "配置是否存在")
    @GetMapping("/existsByType")
    public Result<Boolean> existsByType(String type, String appId) {
        return Res.ok(channelCashierConfigService.existsByType(type, appId));
    }

    @RequestPath("配置是否存在(不包括自身)")
    @Operation(summary = "配置是否存在(不包括自身)")
    @GetMapping("/existsByTypeNotId")
    public Result<Boolean> existsByTypeNotId(String type, String appId, Long id) {
        return Res.ok(channelCashierConfigService.existsByType(type, appId, id));
    }

    @RequestPath("配置删除")
    @Operation(summary = "配置删除")
    @PostMapping("/delete")
    public Result<Void> delete(Long id) {
        channelCashierConfigService.delete(id);
        return Res.ok();
    }

    @RequestPath("获取码牌地址")
    @Operation(summary = "获取码牌地址")
    @GetMapping("/qrCodeUrl")
    public Result<String> qrCodeUrl(String appId) {
        return Res.ok(channelCashierConfigService.qrCodeUrl(appId));
    }
}
