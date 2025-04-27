package org.dromara.daxpay.controller.gateway;

import cn.bootx.platform.core.annotation.RequestGroup;
import cn.bootx.platform.core.annotation.RequestPath;
import cn.bootx.platform.core.rest.Res;
import cn.bootx.platform.core.rest.result.Result;
import cn.bootx.platform.core.validation.ValidationGroup;
import org.dromara.daxpay.service.param.gateway.CashierGroupConfigParam;
import org.dromara.daxpay.service.param.gateway.CashierItemConfigParam;
import org.dromara.daxpay.service.result.gateway.config.CashierGroupConfigResult;
import org.dromara.daxpay.service.result.gateway.config.CashierItemConfigResult;
import org.dromara.daxpay.service.service.gateway.config.CashierConfigService;
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
@RequestMapping("/cashier/config")
@RequestGroup(groupCode = "CashierConfig", groupName = "收银台支付配置", moduleCode = "GatewayPay")
@RequiredArgsConstructor
public class CashierConfigController {
    private final CashierConfigService cashierConfigService;

    @RequestPath("获取指定类型收银台分组列表")
    @Operation(summary = "获取指定类型收银台分组列表")
    @GetMapping("/listByType")
    public Result<List<CashierGroupConfigResult>> listByType(@NotBlank(message = "商户应用ID不可为空") String appId, @NotBlank(message = "商户应用ID不可为空") String cashierType) {
        return Res.ok(cashierConfigService.listByGroup(appId, cashierType));
    }

    @RequestPath("获取收银台分组配置")
    @Operation(summary = "获取收银台分组配置")
    @GetMapping("/findGroupById")
    public Result<CashierGroupConfigResult> findGroupById(@NotNull(message = "分组ID不可为空") Long groupId) {
        return Res.ok(cashierConfigService.findGroupById(groupId));
    }

    @RequestPath("获取收银台条目配置列表")
    @Operation(summary = "获取收银台条目配置列表")
    @GetMapping("/listByItem")
    public Result<List<CashierItemConfigResult>> listByItem(Long groupId) {
        return Res.ok(cashierConfigService.listByItem(groupId));
    }

    @RequestPath("获取收银台条目配置")
    @Operation(summary = "获取收银台条目配置")
    @GetMapping("/findItemById")
    public Result<CashierItemConfigResult> findItemById(Long groupId) {
        return Res.ok(cashierConfigService.findItemById(groupId));
    }

    @RequestPath("保存收银台分组")
    @Operation(summary = "保存收银台分组")
    @PostMapping("/saveGroup")
    public Result<Void> saveGroup(@RequestBody @Validated(ValidationGroup.add.class) CashierGroupConfigParam param) {
        cashierConfigService.saveGroup(param);
        return Res.ok();
    }

    @RequestPath("修改收银台分组")
    @Operation(summary = "修改收银台分组")
    @PostMapping("/updateGroup")
    public Result<Void> updateGroup(@RequestBody @Validated(ValidationGroup.edit.class) CashierGroupConfigParam param) {
        cashierConfigService.updateGroup(param);
        return Res.ok();
    }

    @RequestPath("删除收银台分组")
    @Operation(summary = "删除收银台分组")
    @PostMapping("/deleteGroup")
    public Result<Void> deleteGroup(Long id) {
        cashierConfigService.deleteGroup(id);
        return Res.ok();
    }

    @RequestPath("保存收银台条目配置")
    @Operation(summary = "保存收银台条目配置")
    @PostMapping("/saveItem")
    public Result<Void> saveItem(@RequestBody @Validated(ValidationGroup.add.class)  CashierItemConfigParam param) {
        cashierConfigService.saveItem(param);
        return Res.ok();
    }

    @RequestPath("修改收银台条目配置")
    @Operation(summary = "修改收银台条目配置")
    @PostMapping("/updateItem")
    public Result<Void> updateItem(@RequestBody @Validated(ValidationGroup.edit.class)  CashierItemConfigParam param) {
        cashierConfigService.updateItem(param);
        return Res.ok();
    }

    @RequestPath("删除收银台条目配置")
    @Operation(summary = "删除收银台条目配置")
    @PostMapping("/deleteItem")
    public Result<Void> deleteItem(Long id) {
        cashierConfigService.deleteItem(id);
        return Res.ok();
    }

}
