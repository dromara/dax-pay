package org.dromara.daxpay.payment.merchant.controller.gateway;

import cn.bootx.platform.core.annotation.ClientCode;
import cn.bootx.platform.core.annotation.RequestGroup;
import cn.bootx.platform.core.annotation.RequestPath;
import cn.bootx.platform.core.rest.Res;
import cn.bootx.platform.core.rest.result.Result;
import org.dromara.daxpay.payment.common.code.DaxPayCode;
import org.dromara.daxpay.payment.merchant.entity.gateway.AggregateBarPayConfig;
import org.dromara.daxpay.payment.merchant.entity.gateway.AggregateQrPayConfig;
import org.dromara.daxpay.payment.merchant.param.gateway.AggregateBarPayConfigParam;
import org.dromara.daxpay.payment.merchant.param.gateway.AggregateQrPayConfigParam;
import org.dromara.daxpay.payment.merchant.service.gateway.AggregateConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 *聚合支付配置
 * @author xxm
 * @since 2025/3/24
 */
@Validated
@Tag(name = "聚合支付配置")
@RestController
@RequestMapping("/aggregate/pay/config")
@ClientCode({DaxPayCode.Client.ADMIN, DaxPayCode.Client.MERCHANT})
@RequestGroup(groupCode = "AggregateConfig", groupName = "聚合支付配置", moduleCode = "GatewayPay")
@RequiredArgsConstructor
public class AggregateConfigController {
    private final AggregateConfigService aggregateConfigService;

    @RequestPath("聚合扫码支付配置查询")
    @Operation(summary = "聚合扫码支付配置查询")
    @GetMapping("/findQrConfigByAppId")
    public Result<AggregateQrPayConfig> findQrConfigByAppId(@NotBlank(message = "应用ID不可为空") String appId){
        return Res.ok(aggregateConfigService.findQrConfigByAppId(appId));
    }

    @RequestPath("聚合扫码支付配置更新")
    @Operation(summary = "聚合扫码支付配置更新")
    @PostMapping("/updateQrConfig")
    public Result<Void> updateQrConfig(@Validated @RequestBody AggregateQrPayConfigParam param){
        aggregateConfigService.updateQrConfig(param);
        return Res.ok();
    }

    @RequestPath("聚合付款码支付配置查询")
    @Operation(summary = "聚合付款码支付配置查询")
    @GetMapping("/findBarConfigByAppId")
    public Result<AggregateBarPayConfig> findBarConfigByAppId(@NotBlank(message = "应用ID不可为空") String appId){
        return Res.ok(aggregateConfigService.findBarConfigByAppId(appId));
    }

    @RequestPath("聚合付款码支付配置更新")
    @Operation(summary = "聚合付款码支付配置更新")
    @PostMapping("/updateBarConfig")
    public Result<Void> updateBarConfig(@Validated @RequestBody AggregateBarPayConfigParam param){
        aggregateConfigService.updateBarConfig(param);
        return Res.ok();
    }
}
