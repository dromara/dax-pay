package org.dromara.daxpay.payment.isv.controller.gateway;

import cn.bootx.platform.core.annotation.ClientCode;
import cn.bootx.platform.core.annotation.RequestGroup;
import cn.bootx.platform.core.annotation.RequestPath;
import cn.bootx.platform.core.rest.Res;
import cn.bootx.platform.core.rest.result.Result;
import cn.bootx.platform.core.validation.ValidationGroup;
import org.dromara.daxpay.payment.common.code.DaxPayCode;
import org.dromara.daxpay.payment.isv.param.gateway.IsvCheckoutCounterConfigParam;
import org.dromara.daxpay.payment.isv.result.gateway.IsvCheckoutCounterConfigResult;
import org.dromara.daxpay.payment.isv.service.gateway.IsvCheckoutCounterConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 收银台支付配置
 * @author xxm
 * @since 2025/3/24
 */
@Validated
@ClientCode({DaxPayCode.Client.ADMIN})
@Tag(name = "收银台支付配置(服务商)")
@RestController
@RequestMapping("/isv/checkout/counter/config")
@RequestGroup(groupCode = "CashierConfig", groupName = "收银台支付配置(服务商)", moduleCode = "GatewayPay")
@RequiredArgsConstructor
public class IsvCheckoutCounterConfigController {
    private final IsvCheckoutCounterConfigService counterConfigService;

    @RequestPath("获取指定类型收银台分组列表")
    @Operation(summary = "获取指定类型收银台分组列表")
    @GetMapping("/listByType")
    public Result<List<IsvCheckoutCounterConfigResult>> listByType(@NotBlank(message = "商户应用ID不可为空") String isvNo,
                                                                   @NotBlank(message = "收银台类型不可为空") String type) {
        return Res.ok(counterConfigService.findAll(isvNo, type));
    }

    @RequestPath("获取收银台配置")
    @Operation(summary = "获取收银台配置")
    @GetMapping("/findById")
    public Result<IsvCheckoutCounterConfigResult> findGroupById(@NotNull(message = "主键不可为空") Long id) {
        return Res.ok(counterConfigService.findById(id));
    }

    @RequestPath("保存")
    @Operation(summary = "保存")
    @PostMapping("/save")
    public Result<Void> save(@RequestBody @Validated(ValidationGroup.add.class) IsvCheckoutCounterConfigParam param) {
        counterConfigService.save(param);
        return Res.ok();
    }

    @RequestPath("修改")
    @Operation(summary = "修改")
    @PostMapping("/update")
    public Result<Void> update(@RequestBody @Validated(ValidationGroup.edit.class) IsvCheckoutCounterConfigParam param) {
        counterConfigService.update(param);
        return Res.ok();
    }

    @RequestPath("删除")
    @Operation(summary = "删除")
    @PostMapping("/delete")
    public Result<Void> delete(@RequestBody Long id) {
        counterConfigService.delete(id);
        return Res.ok();
    }

}
