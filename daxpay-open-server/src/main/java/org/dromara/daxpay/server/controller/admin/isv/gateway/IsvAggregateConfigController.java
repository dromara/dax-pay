package org.dromara.daxpay.server.controller.admin.isv.gateway;

import cn.bootx.platform.core.annotation.ClientCode;
import cn.bootx.platform.core.annotation.RequestGroup;
import cn.bootx.platform.core.annotation.RequestPath;
import cn.bootx.platform.core.rest.Res;
import cn.bootx.platform.core.rest.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.dromara.daxpay.service.common.code.DaxPayCode;
import org.dromara.daxpay.service.isv.param.gateway.IsvAggregateBarPayConfigParam;
import org.dromara.daxpay.service.isv.param.gateway.IsvAggregatePayConfigParam;
import org.dromara.daxpay.service.isv.result.gateway.IsvAggregateBarPayConfigResult;
import org.dromara.daxpay.service.isv.result.gateway.IsvAggregatePayConfigResult;
import org.dromara.daxpay.service.isv.service.gateway.IsvAggregateConfigService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 聚合支付配置
 * @author xxm
 * @since 2025/3/24
 */
@Validated
@ClientCode({DaxPayCode.Client.ADMIN})
@Tag(name = "聚合支付配置(服务商)")
@RestController
@RequestMapping("/isv/aggregate/config")
@RequestGroup(groupCode = "AggregateConfig", groupName = "聚合支付配置(服务商)", moduleCode = "GatewayPay")
@RequiredArgsConstructor
public class IsvAggregateConfigController {
    private final IsvAggregateConfigService aggregateConfigService;

    @RequestPath("支付配置列表")
    @Operation(summary = "支付配置列表")
    @GetMapping("/listByPay")
    public Result<List<IsvAggregatePayConfigResult>> listByPay(){
        return Res.ok(aggregateConfigService.listByPay());
    }

    @RequestPath("付款码支付配置列表")
    @Operation(summary = "付款码支付配置列表")
    @GetMapping("/listByBar")
    public Result<List<IsvAggregateBarPayConfigResult>> listByBar(){
        return Res.ok(aggregateConfigService.listByBar());
    }

    @RequestPath("支付配置详情")
    @Operation(summary = "支付配置详情")
    @GetMapping("/findPayById")
    public Result<IsvAggregatePayConfigResult> findPayById(Long id){
        return Res.ok(aggregateConfigService.findPayById(id));
    }

    @RequestPath("付款码支付配置详情")
    @Operation(summary = "付款码支付配置详情")
    @GetMapping("/findBarById")
    public Result<IsvAggregateBarPayConfigResult> findBarById(Long id){
        return Res.ok(aggregateConfigService.findBarById(id));
    }

    @RequestPath("保存支付配置")
    @Operation(summary = "保存支付配置")
    @PostMapping("/savePay")
    public Result<Void> savePay(@Validated @RequestBody IsvAggregatePayConfigParam param){
        aggregateConfigService.savePay(param);
        return Res.ok();
    }

    @RequestPath("保存付款码支付配置")
    @Operation(summary = "保存付款码支付配置")
    @PostMapping("/saveBar")
    public Result<Void> saveBar(@Validated @RequestBody IsvAggregateBarPayConfigParam param){
        aggregateConfigService.saveBar(param);
        return Res.ok();
    }

    @RequestPath("更新支付配置")
    @Operation(summary = "更新支付配置")
    @PostMapping("/updatePay")
    public Result<Void> updatePay(@Validated @RequestBody IsvAggregatePayConfigParam param){
        aggregateConfigService.updatePay(param);
        return Res.ok();
    }

    @RequestPath("更新付款码支付配置")
    @Operation(summary = "更新付款码支付配置")
    @PostMapping("/updateBar")
    public Result<Void> updateBar(@Validated @RequestBody IsvAggregateBarPayConfigParam param){
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
    public Result<Boolean> existsPay(String type){
        return Res.ok(aggregateConfigService.existsPay(type));
    }

    @RequestPath("支付配置是否存在(不包含自身)")
    @Operation(summary = "支付配置是否存在(不包含自身)")
    @GetMapping("/existsPayNotId")
    public Result<Boolean> existsPayNotId(String type, Long id){
        return Res.ok(aggregateConfigService.existsPay(type, id));
    }

    @RequestPath("付款码支付配置是否存在")
    @Operation(summary = "付款码支付配置是否存在")
    @GetMapping("/existsBar")
    public Result<Boolean> existsBar(String type){
        return Res.ok(aggregateConfigService.existsBar(type));
    }

    @RequestPath("付款码支付配置是否存在(不包含自身)")
    @Operation(summary = "付款码支付配置是否存在(不包含自身)")
    @GetMapping("/existsBarNotId")
    public Result<Boolean> existsBarNotId(String type, Long id){
        return Res.ok(aggregateConfigService.existsBar(type, id));
    }
}
