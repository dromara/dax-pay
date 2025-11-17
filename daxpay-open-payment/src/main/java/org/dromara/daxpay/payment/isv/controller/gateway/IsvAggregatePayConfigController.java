package org.dromara.daxpay.payment.isv.controller.gateway;

import cn.bootx.platform.core.annotation.ClientCode;
import cn.bootx.platform.core.annotation.RequestGroup;
import cn.bootx.platform.core.annotation.RequestPath;
import cn.bootx.platform.core.rest.Res;
import cn.bootx.platform.core.rest.result.Result;
import org.dromara.daxpay.payment.common.code.DaxPayCode;
import org.dromara.daxpay.payment.isv.param.gateway.IsvAggregateBarPayConfigParam;
import org.dromara.daxpay.payment.isv.param.gateway.IsvAggregatePayConfigParam;
import org.dromara.daxpay.payment.isv.result.gateway.IsvAggregateBarPayConfigResult;
import org.dromara.daxpay.payment.isv.result.gateway.IsvAggregatePayConfigResult;
import org.dromara.daxpay.payment.isv.service.gateway.IsvAggregatePayConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 聚合支付配置
 * @author xxm
 * @since 2025/3/24
 */
@Validated
@ClientCode({DaxPayCode.Client.ADMIN})
@Tag(name = "聚合支付配置(服务商)")
@RestController
@RequestMapping("/isv/aggregate/pay/config")
@RequestGroup(groupCode = "AggregateConfig", groupName = "聚合支付配置(服务商)", moduleCode = "GatewayPay")
@RequiredArgsConstructor
public class IsvAggregatePayConfigController {
    private final IsvAggregatePayConfigService aggregateConfigService;

    @RequestPath("获取聚合二维码配置详情")
    @Operation(summary = "获取聚合二维码配置详情")
    @GetMapping("/findQrByIsvNo")
    public Result<IsvAggregatePayConfigResult> findQrByNo(@NotBlank(message = "服务商号不可为空") String isvNo) {
        return Res.ok(aggregateConfigService.findPayByIsvNo(isvNo).toResult());
    }

    @RequestPath("更新聚合二维码支付配置")
    @Operation(summary = "更新聚合二维码支付配置")
    @PostMapping("/updateQrConfig")
    public Result<Void> updateQrConfig(@RequestBody @Validated IsvAggregatePayConfigParam param) {
        aggregateConfigService.updateQrConfig(param);
        return Res.ok();
    }

    @RequestPath("获取聚合付款码配置详情")
    @Operation(summary = "获取聚合付款码配置详情")
    @GetMapping("/findBarByIsvNo")
    public Result<IsvAggregateBarPayConfigResult> findBarByNo(@NotBlank(message = "服务商号不可为空") String isvNo) {
        return Res.ok(aggregateConfigService.findBarByNo(isvNo).toResult());
    }

    @RequestPath("更新聚合付款码支付配置")
    @Operation(summary = "更新聚合付款码支付配置")
    @PostMapping("/updateBarConfig")
    public Result<Void> updateBarConfig(@RequestBody @Validated IsvAggregateBarPayConfigParam param) {
        aggregateConfigService.updateBarConfig(param);
        return Res.ok();
    }

}
