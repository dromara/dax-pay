package cn.daxpay.multi.service.controller.config;

import cn.bootx.platform.core.annotation.RequestGroup;
import cn.bootx.platform.core.annotation.RequestPath;
import cn.bootx.platform.core.rest.Res;
import cn.bootx.platform.core.rest.result.Result;
import cn.daxpay.multi.service.param.config.ChannelCashierConfigParam;
import cn.daxpay.multi.service.result.config.ChannelCashierConfigResult;
import cn.daxpay.multi.service.service.config.ChannelCashierConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    @GetMapping("/list")
    public Result<List<ChannelCashierConfigResult>> list(ChannelCashierConfigParam param) {
        return Res.ok(channelCashierConfigService.list(param));
    }

    @RequestPath("配置详情")
    @Operation(summary = "配置详情")
    @GetMapping("/findById")
    public Result<ChannelCashierConfigResult> findById(Long id) {
        return Res.ok(channelCashierConfigService.findById(id));
    }

    @RequestPath("配置保存")
    @Operation(summary = "配置保存")
    @GetMapping("/save")
    public Result<Void> save(ChannelCashierConfigParam param) {
        channelCashierConfigService.save(param);
        return Res.ok();
    }

    @RequestPath("配置更新")
    @Operation(summary = "配置更新")
    @GetMapping("/update")
    public Result<Void> update(ChannelCashierConfigParam param) {
        channelCashierConfigService.update(param);
        return Res.ok();
    }

    @RequestPath("配置删除")
    @Operation(summary = "配置删除")
    @GetMapping("/delete")
    public Result<Void> delete(Long id) {
        channelCashierConfigService.delete(id);
        return Res.ok();
    }
}
