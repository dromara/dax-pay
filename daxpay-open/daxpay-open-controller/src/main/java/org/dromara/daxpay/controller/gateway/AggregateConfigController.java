package org.dromara.daxpay.controller.gateway;

import cn.bootx.platform.core.annotation.RequestGroup;
import cn.bootx.platform.core.annotation.RequestPath;
import cn.bootx.platform.core.rest.Res;
import cn.bootx.platform.core.rest.result.Result;
import org.dromara.daxpay.service.param.gateway.AggregateBarPayConfigParam;
import org.dromara.daxpay.service.param.gateway.AggregatePayConfigParam;
import org.dromara.daxpay.service.result.gateway.config.AggregateBarPayConfigResult;
import org.dromara.daxpay.service.result.gateway.config.AggregatePayConfigResult;
import org.dromara.daxpay.service.service.gateway.config.AggregateConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 *聚合支付配置
 * @author xxm
 * @since 2025/3/24
 */
@Validated
@Tag(name = "聚合支付配置")
@RestController
@RequestMapping("/aggregate/config")
@RequestGroup(groupCode = "AggregateConfig", groupName = "聚合支付配置", moduleCode = "GatewayPay")
@RequiredArgsConstructor
public class AggregateConfigController {
    private final AggregateConfigService aggregateConfigService;

    @RequestPath("支付配置列表")
    @Operation(summary = "支付配置列表")
    @GetMapping("/listByPay")
    public Result<List<AggregatePayConfigResult>> listByPay(@NotBlank(message = "商户应用ID不可为空") String appId){
        return Res.ok(aggregateConfigService.listByPay(appId));
    }

    @RequestPath("付款码支付配置列表")
    @Operation(summary = "付款码支付配置列表")
    @GetMapping("/listByBar")
    public Result<List<AggregateBarPayConfigResult>> listByBar(@NotBlank(message = "商户应用ID不可为空") String appId){
        return Res.ok(aggregateConfigService.listByBar(appId));
    }

    @RequestPath("支付配置详情")
    @Operation(summary = "支付配置详情")
    @GetMapping("/findPayById")
    public Result<AggregatePayConfigResult> findPayById(Long id){
        return Res.ok(aggregateConfigService.findPayById(id));
    }

    @RequestPath("付款码支付配置详情")
    @Operation(summary = "付款码支付配置详情")
    @GetMapping("/findBarById")
    public Result<AggregateBarPayConfigResult> findBarById(Long id){
        return Res.ok(aggregateConfigService.findBarById(id));
    }

    @RequestPath("保存支付配置")
    @Operation(summary = "保存支付配置")
    @PostMapping("/savePay")
    public Result<Void> savePay(@Validated @RequestBody AggregatePayConfigParam param){
        aggregateConfigService.savePay(param);
        return Res.ok();
    }

    @RequestPath("保存付款码支付配置")
    @Operation(summary = "保存付款码支付配置")
    @PostMapping("/saveBar")
    public Result<Void> saveBar(@Validated @RequestBody AggregateBarPayConfigParam param){
        aggregateConfigService.saveBar(param);
        return Res.ok();
    }

    @RequestPath("更新支付配置")
    @Operation(summary = "更新支付配置")
    @PostMapping("/updatePay")
    public Result<Void> updatePay(@Validated @RequestBody AggregatePayConfigParam param){
        aggregateConfigService.updatePay(param);
        return Res.ok();
    }

    @RequestPath("更新付款码支付配置")
    @Operation(summary = "更新付款码支付配置")
    @PostMapping("/updateBar")
    public Result<Void> updateBar(@Validated @RequestBody AggregateBarPayConfigParam param){
        aggregateConfigService.updateBar(param);
        return Res.ok();
    }

    @RequestPath("删除支付配置")
    @Operation(summary = "删除支付配置")
    @PostMapping("/deletePay")
    public Result<Void> deletePay(Long id){
        aggregateConfigService.deletePay(id);
        return Res.ok();
    }

    @RequestPath("删除付款码支付配置")
    @Operation(summary = "删除付款码支付配置")
    @PostMapping("/deleteBar")
    public Result<Void> deleteBar(Long id){
        aggregateConfigService.deleteBar(id);
        return Res.ok();
    }

    @RequestPath("支付配置是否存在")
    @Operation(summary = "支付配置是否存在")
    @GetMapping("/existsPay")
    public Result<Boolean> existsPay(String appId, String type){
        return Res.ok(aggregateConfigService.existsPay(appId, type));
    }

    @RequestPath("支付配置是否存在(不包含自身)")
    @Operation(summary = "支付配置是否存在(不包含自身)")
    @GetMapping("/existsPayNotId")
    public Result<Boolean> existsPayNotId(String appId, String type, Long id){
        return Res.ok(aggregateConfigService.existsPay(appId, type, id));
    }

    @RequestPath("付款码支付配置是否存在")
    @Operation(summary = "付款码支付配置是否存在")
    @GetMapping("/existsBar")
    public Result<Boolean> existsBar(String appId, String type){
        return Res.ok(aggregateConfigService.existsBar(appId, type));
    }

    @RequestPath("付款码支付配置是否存在(不包含自身)")
    @Operation(summary = "付款码支付配置是否存在(不包含自身)")
    @GetMapping("/existsBarNotId")
    public Result<Boolean> existsBarNotId(String appId, String type, Long id){
        return Res.ok(aggregateConfigService.existsBar(appId, type, id));
    }
}
