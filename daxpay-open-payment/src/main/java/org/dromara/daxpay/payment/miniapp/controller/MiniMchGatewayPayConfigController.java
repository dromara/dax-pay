package org.dromara.daxpay.payment.miniapp.controller;

import cn.bootx.platform.core.annotation.ClientCode;
import cn.bootx.platform.core.annotation.RequestGroup;
import cn.bootx.platform.core.annotation.RequestPath;
import cn.bootx.platform.core.rest.Res;
import cn.bootx.platform.core.rest.result.Result;
import cn.bootx.platform.core.validation.ValidationGroup;
import org.dromara.daxpay.payment.common.code.DaxPayCode;
import org.dromara.daxpay.payment.merchant.entity.gateway.*;
import org.dromara.daxpay.payment.merchant.param.gateway.*;
import org.dromara.daxpay.payment.merchant.result.gateway.GatewayPayConfigResult;
import org.dromara.daxpay.payment.merchant.result.gateway.GatewayPayReadConfigResult;
import org.dromara.daxpay.payment.merchant.service.gateway.AggregateConfigService;
import org.dromara.daxpay.payment.merchant.service.gateway.CashierCodeConfigService;
import org.dromara.daxpay.payment.merchant.service.gateway.GatewayPayConfigService;
import org.dromara.daxpay.payment.merchant.service.gateway.GatewayPayReadConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 小程序网关支付接口
 * @author xxm
 * @since 2025/10/14
 */
@Validated
@ClientCode(DaxPayCode.Client.MERCHANT)
@Tag(name = "小程序网关支付接口")
@RestController
@RequestMapping("/mini/mch")
@RequestGroup(groupCode = "MiniMchGateway", groupName = "小程序网关支付配置接口", moduleCode = "MiniMch")
@RequiredArgsConstructor
public class MiniMchGatewayPayConfigController {
    private final AggregateConfigService aggregateConfigService;
    private final GatewayPayConfigService gatewayPayConfigService2;
    private final GatewayPayReadConfigService gatewayPayReadConfigService;
    private final CashierCodeConfigService codeConfigService;

    // ==================== 聚合支付配置 ====================

    @RequestPath("聚合扫码支付配置查询")
    @Operation(summary = "聚合扫码支付配置查询")
    @GetMapping("/aggregate/pay/config/findQrConfigByAppId")
    public Result<AggregateQrPayConfig> findQrConfigByAppId(@NotBlank(message = "应用ID不可为空") String appId){
        return Res.ok(aggregateConfigService.findQrConfigByAppId(appId));
    }

    @RequestPath("聚合扫码支付配置更新")
    @Operation(summary = "聚合扫码支付配置更新")
    @PostMapping("/aggregate/pay/config/updateQrConfig")
    public Result<Void> updateQrConfig(@Validated @RequestBody AggregateQrPayConfigParam param){
        aggregateConfigService.updateQrConfig(param);
        return Res.ok();
    }

    @RequestPath("聚合付款码支付配置查询")
    @Operation(summary = "聚合付款码支付配置查询")
    @GetMapping("/aggregate/pay/config/findBarConfigByAppId")
    public Result<AggregateBarPayConfig> findBarConfigByAppId(@NotBlank(message = "应用ID不可为空") String appId){
        return Res.ok(aggregateConfigService.findBarConfigByAppId(appId));
    }

    @RequestPath("聚合付款码支付配置更新")
    @Operation(summary = "聚合付款码支付配置更新")
    @PostMapping("/aggregate/pay/config/updateBarConfig")
    public Result<Void> updateBarConfig(@Validated @RequestBody AggregateBarPayConfigParam param){
        aggregateConfigService.updateBarConfig(param);
        return Res.ok();
    }

    // ==================== 网关支付配置 ====================

    @RequestPath("获取网关支付配置")
    @Operation(summary = "获取网关支付配置")
    @GetMapping("/gateway/pay/config/getConfig")
    public Result<GatewayPayConfigResult> getConfig(@NotBlank(message = "商户应用ID不可为空") String appId) {
        return Res.ok(gatewayPayConfigService2.findByAppId(appId).toResult());
    }

    @RequestPath("更新网关支付配置")
    @Operation(summary = "更新网关支付配置")
    @PostMapping("/gateway/pay/config/update")
    public Result<Void> update(@Validated(ValidationGroup.edit.class) @RequestBody GatewayPayConfigParam param) {
        gatewayPayConfigService2.update(param);
        return Res.ok();
    }

    @RequestPath("获取网关支付读取配置")
    @Operation(summary = "获取网关支付读取配置")
    @GetMapping("/gateway/pay/config/getReadConfig")
    public Result<GatewayPayReadConfigResult> getReadConfig(@NotBlank(message = "商户应用ID不可为空") String appId) {
        return Res.ok(gatewayPayReadConfigService.findByAppId(appId).toResult());
    }

    @RequestPath("更新网关支付读取配置")
    @Operation(summary = "更新网关支付读取配置")
    @PostMapping("/gateway/pay/config/updateReadConfig")
    public Result<Void> updateReadConfig(@Validated(ValidationGroup.edit.class) @RequestBody GatewayPayReadConfigParam param) {
        gatewayPayReadConfigService.update(param);
        return Res.ok();
    }

    // ==================== 收银码牌配置 ====================

    @RequestPath("根据应用ID查询")
    @Operation(summary = "根据应用ID查询")
    @GetMapping("/cashier/code/config/findByAppId")
    public Result<CashierCodeConfig> findByAppId(@NotBlank(message = "应用ID不可为空") String appId){
        return Res.ok(codeConfigService.findByAppId(appId));
    }

    @RequestPath("更新")
    @Operation(summary = "更新")
    @PostMapping("/cashier/code/config/update")
    public Result<Void> update(@Validated @RequestBody CashierCodeConfigParam param){
        codeConfigService.update(param);
        return Res.ok();
    }

}
