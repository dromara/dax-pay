package org.dromara.daxpay.service.controller.config;

import cn.bootx.platform.core.annotation.RequestGroup;
import cn.bootx.platform.core.annotation.RequestPath;
import cn.bootx.platform.core.rest.Res;
import cn.bootx.platform.core.rest.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.dromara.daxpay.service.param.config.checkout.CheckoutAggregateConfigParam;
import org.dromara.daxpay.service.param.config.checkout.CheckoutConfigParam;
import org.dromara.daxpay.service.param.config.checkout.CheckoutGroupConfigParam;
import org.dromara.daxpay.service.param.config.checkout.CheckoutItemConfigParam;
import org.dromara.daxpay.service.result.config.checkout.CheckoutAggregateConfigVo;
import org.dromara.daxpay.service.result.config.checkout.CheckoutConfigVo;
import org.dromara.daxpay.service.result.config.checkout.CheckoutGroupConfigVo;
import org.dromara.daxpay.service.result.config.checkout.CheckoutItemConfigVo;
import org.dromara.daxpay.service.service.config.checkout.CheckoutConfigQueryService;
import org.dromara.daxpay.service.service.config.checkout.CheckoutConfigService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 支付配置控制器
 * @author xxm
 * @since 2024/11/27
 */
@Tag(name = "收银台配置配置")
@RestController
@RequestGroup(groupCode = "channelConfig", groupName = "通道配置", moduleCode = "PayConfig", moduleName = "支付配置")
@RequestMapping("/checkout/config")
@RequiredArgsConstructor
public class CheckoutConfigController {
    private final CheckoutConfigQueryService checkoutConfigQueryService;
    private final CheckoutConfigService checkoutConfigService;

    @RequestPath("获取收银台配置")
    @Operation(summary = "获取收银台配置")
    @GetMapping("/get")
    public Result<CheckoutConfigVo> getConfig(String appId) {
        return Res.ok(checkoutConfigQueryService.getConfig(appId));
    }

    @RequestPath("保存收银台配置")
    @Operation(summary = "保存收银台配置")
    @PostMapping("/save")
    public Result<Void> saveConfig(@RequestBody CheckoutConfigParam param) {
        checkoutConfigService.saveCheckoutConfig(param);
        return Res.ok();
    }

    @RequestPath("更新收银台配置")
    @Operation(summary = "更新收银台配置")
    @PostMapping("/update")
    public Result<Void> updateConfig(@RequestBody CheckoutConfigParam param) {
        checkoutConfigService.updateCheckoutConfig(param);
        return Res.ok();
    }

    @RequestPath("获取分组配置列表")
    @Operation(summary = "获取分组配置列表")
    @GetMapping("/group/list")
    public Result<List<CheckoutGroupConfigVo>> getGroupConfigs(String appId, String checkoutType) {
        return Res.ok(checkoutConfigQueryService.getGroupConfigs(appId,checkoutType));
    }

    @RequestPath("获取分组配置")
    @Operation(summary = "获取分组配置")
    @GetMapping("/group/get")
    public Result<CheckoutGroupConfigVo> getGroupConfig(Long id) {
        return Res.ok(checkoutConfigQueryService.getGroupConfig(id));
    }

    @RequestPath("保存分组配置")
    @Operation(summary = "保存分组配置")
    @PostMapping("/group/save")
    public Result<Void> saveCheckoutGroupConfig(@RequestBody CheckoutGroupConfigParam param) {
        checkoutConfigService.saveCheckoutGroupConfig(param);
        return Res.ok();
    }

    @RequestPath("更新分组配置")
    @Operation(summary = "更新分组配置")
    @PostMapping("/group/update")
    public Result<Void> updateCheckoutGroupConfig(@RequestBody CheckoutGroupConfigParam param) {
        checkoutConfigService.updateCheckoutGroupConfig(param);
        return Res.ok();
    }

    @RequestPath("删除分组配置")
    @Operation(summary = "删除分组配置")
    @PostMapping("/group/delete")
    public Result<Void> deleteCheckoutGroupConfig(Long id) {
        checkoutConfigService.deleteCheckoutGroupConfig(id);
        return Res.ok();
    }

    @RequestPath("获取配置项列表")
    @Operation(summary = "获取配置项列表")
    @GetMapping("/item/list")
    public Result<List<CheckoutItemConfigVo>> getItemConfigs(Long groupId) {
        return Res.ok(checkoutConfigQueryService.getItemConfigs(groupId));
    }

    @RequestPath("获取配置项")
    @Operation(summary = "获取配置项")
    @GetMapping("/item/get")
    public Result<CheckoutItemConfigVo> getItemConfig(Long id) {
        return Res.ok(checkoutConfigQueryService.getItemConfig(id));
    }


    @RequestPath("保存配置项配置")
    @Operation(summary = "保存配置项配置")
    @PostMapping("/item/save")
    public Result<Void> saveCheckoutItemConfig(@RequestBody CheckoutItemConfigParam param) {
        checkoutConfigService.saveCheckoutItemConfig(param);
        return Res.ok();
    }

    @RequestPath("更新配置项配置")
    @Operation(summary = "更新配置项配置")
    @PostMapping("/item/update")
    public Result<Void> updateCheckoutItemConfig(@RequestBody CheckoutItemConfigParam param) {
        checkoutConfigService.updateCheckoutItemConfig(param);
        return Res.ok();
    }

    @RequestPath("删除配置项配置")
    @Operation(summary = "删除配置项配置")
    @PostMapping("/item/delete")
    public Result<Void> deleteCheckoutItemConfig(Long id) {
        checkoutConfigService.deleteCheckoutItemConfig(id);
        return Res.ok();
    }

    @RequestPath("获取聚合支付配置列表")
    @Operation(summary = "获取聚合支付配置列表")
    @GetMapping("/aggregate/list")
    public Result<List<CheckoutAggregateConfigVo>> getAggregateConfigs(String appId) {
        return Res.ok(checkoutConfigQueryService.getAggregateConfigs(appId));
    }

    @RequestPath("获取聚合支付配置")
    @Operation(summary = "获取聚合支付配置")
    @GetMapping("/aggregate/get")
    public Result<CheckoutAggregateConfigVo> getAggregateConfig(Long id) {
        return Res.ok(checkoutConfigQueryService.getAggregateConfig(id));
    }

    @RequestPath("聚合支付配置是否存在")
    @Operation(summary = "聚合支付配置是否存在")
    @GetMapping("/aggregate/exists")
    public Result<Boolean> existsAggregateConfig(String appId, String type) {
        return Res.ok(checkoutConfigQueryService.existsByAppIdAndType(appId,type));
    }

    @RequestPath("聚合支付配置是否存在(不包含自身)")
    @Operation(summary = "聚合支付配置是否存在(不包含自身)")
    @GetMapping("/aggregate/existsNotId")
    public Result<Boolean> existsAggregateConfig(String appId, String type, Long id) {
        return Res.ok(checkoutConfigQueryService.existsByAppIdAndType(appId,type,id));
    }

    @RequestPath("保存聚合支付配置")
    @Operation(summary = "保存聚合支付配置")
    @PostMapping("/aggregate/save")
    public Result<Void> saveCheckoutAggregateConfig(@RequestBody CheckoutAggregateConfigParam param) {
        checkoutConfigService.saveCheckoutAggregateConfig(param);
        return Res.ok();
    }

    @RequestPath("更新聚合支付配置")
    @Operation(summary = "更新聚合支付配置")
    @PostMapping("/aggregate/update")
    public Result<Void> updateCheckoutAggregateConfig(@RequestBody CheckoutAggregateConfigParam param) {
        checkoutConfigService.updateCheckoutAggregateConfig(param);
        return Res.ok();
    }

    @RequestPath("删除聚合支付配置")
    @Operation(summary = "删除聚合支付配置")
    @PostMapping("/aggregate/delete")
    public Result<Void> deleteCheckoutAggregateConfig(Long id) {
        checkoutConfigService.deleteCheckoutAggregateConfig(id);
        return Res.ok();
    }


}
