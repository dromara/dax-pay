package org.dromara.daxpay.payment.merchant.controller.gateway;

import cn.bootx.platform.core.annotation.ClientCode;
import cn.bootx.platform.core.annotation.RequestGroup;
import cn.bootx.platform.core.annotation.RequestPath;
import cn.bootx.platform.core.rest.Res;
import cn.bootx.platform.core.rest.result.Result;
import cn.bootx.platform.core.validation.ValidationGroup;
import org.dromara.daxpay.payment.common.code.DaxPayCode;
import org.dromara.daxpay.payment.merchant.param.gateway.CheckoutCounterConfigParam;
import org.dromara.daxpay.payment.merchant.result.gateway.CheckoutCounterConfigResult;
import org.dromara.daxpay.payment.merchant.service.gateway.CheckoutCounterConfigService;
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
@Tag(name = "收银台支付配置")
@RestController
@RequestMapping("/checkout/counter/config")
@ClientCode({DaxPayCode.Client.ADMIN, DaxPayCode.Client.MERCHANT})
@RequestGroup(groupCode = "CheckoutCounter", groupName = "收银台支付配置", moduleCode = "GatewayPay")
@RequiredArgsConstructor
public class CheckoutCounterConfigController {
    private final CheckoutCounterConfigService checkoutCounterConfigService;

    @RequestPath("获取指定类型收银台分组列表")
    @Operation(summary = "获取指定类型收银台分组列表")
    @GetMapping("/listByType")
    public Result<List<CheckoutCounterConfigResult>> listByType(@NotBlank(message = "商户应用ID不可为空") String appId,
                                                                @NotBlank(message = "收银台类型不可为空") String type) {
        return Res.ok(checkoutCounterConfigService.findAll(appId, type));
    }

    @RequestPath("获取收银台配置")
    @Operation(summary = "获取收银台配置")
    @GetMapping("/findById")
    public Result<CheckoutCounterConfigResult> findGroupById(@NotNull(message = "主键不可为空") Long id) {
        return Res.ok(checkoutCounterConfigService.findById(id));
    }

    @RequestPath("保存")
    @Operation(summary = "保存")
    @PostMapping("/save")
    public Result<Void> save(@RequestBody @Validated(ValidationGroup.add.class) CheckoutCounterConfigParam param) {
        checkoutCounterConfigService.save(param);
        return Res.ok();
    }


    @RequestPath("修改")
    @Operation(summary = "修改")
    @PostMapping("/update")
    public Result<Void> update(@RequestBody @Validated(ValidationGroup.edit.class) CheckoutCounterConfigParam param) {
        checkoutCounterConfigService.update(param);
        return Res.ok();
    }

    @RequestPath("删除")
    @Operation(summary = "删除")
    @PostMapping("/delete")
    public Result<Void> delete(@RequestBody Long id) {
        checkoutCounterConfigService.delete(id);
        return Res.ok();
    }

}
