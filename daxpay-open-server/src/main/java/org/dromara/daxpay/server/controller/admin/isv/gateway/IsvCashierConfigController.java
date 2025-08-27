package org.dromara.daxpay.server.controller.admin.isv.gateway;

import cn.bootx.platform.core.annotation.ClientCode;
import cn.bootx.platform.core.annotation.RequestGroup;
import cn.bootx.platform.core.annotation.RequestPath;
import cn.bootx.platform.core.rest.Res;
import cn.bootx.platform.core.rest.result.Result;
import cn.bootx.platform.core.validation.ValidationGroup;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.dromara.daxpay.service.common.code.DaxPayCode;
import org.dromara.daxpay.service.isv.param.gateway.IsvCashierGroupConfigParam;
import org.dromara.daxpay.service.isv.param.gateway.IsvCashierItemConfigParam;
import org.dromara.daxpay.service.isv.result.gateway.IsvCashierGroupConfigResult;
import org.dromara.daxpay.service.isv.result.gateway.IsvCashierItemConfigResult;
import org.dromara.daxpay.service.isv.service.gateway.IsvCashierConfigService;
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
@RequestMapping("/isv/cashier/config")
@RequestGroup(groupCode = "CashierConfig", groupName = "收银台支付配置(服务商)", moduleCode = "GatewayPay")
@RequiredArgsConstructor
public class IsvCashierConfigController {
    private final IsvCashierConfigService cashierConfigService;

    @RequestPath("获取指定类型收银台分组列表")
    @Operation(summary = "获取指定类型收银台分组列表")
    @GetMapping("/listByType")
    public Result<List<IsvCashierGroupConfigResult>> listByType(@NotBlank(message = "收银台类型不可为空") String cashierType) {
        return Res.ok(cashierConfigService.listByGroup(cashierType));
    }

    @RequestPath("获取收银台分组配置")
    @Operation(summary = "获取收银台分组配置")
    @GetMapping("/findGroupById")
    public Result<IsvCashierGroupConfigResult> findGroupById(@NotNull(message = "分组ID不可为空") Long groupId) {
        return Res.ok(cashierConfigService.findGroupById(groupId));
    }

    @RequestPath("获取收银台条目配置列表")
    @Operation(summary = "获取收银台条目配置列表")
    @GetMapping("/listByItem")
    public Result<List<IsvCashierItemConfigResult>> listByItem(Long groupId) {
        return Res.ok(cashierConfigService.listByItem(groupId));
    }

    @RequestPath("获取收银台条目配置")
    @Operation(summary = "获取收银台条目配置")
    @GetMapping("/findItemById")
    public Result<IsvCashierItemConfigResult> findItemById(Long groupId) {
        return Res.ok(cashierConfigService.findItemById(groupId));
    }

    @RequestPath("保存收银台分组")
    @Operation(summary = "保存收银台分组")
    @PostMapping("/saveGroup")
    public Result<Void> saveGroup(@RequestBody @Validated(ValidationGroup.add.class) IsvCashierGroupConfigParam param) {
        cashierConfigService.saveGroup(param);
        return Res.ok();
    }

    @RequestPath("保存默认收银台分组(H5)")
    @Operation(summary = "保存默认收银台分组(H5)")
    @PostMapping("/saveDefaultGroup")
    public Result<Void> saveDefaultGroup() {
        cashierConfigService.saveDefaultGroup();
        return Res.ok();
    }

    @RequestPath("修改收银台分组")
    @Operation(summary = "修改收银台分组")
    @PostMapping("/updateGroup")
    public Result<Void> updateGroup(@RequestBody @Validated(ValidationGroup.edit.class) IsvCashierGroupConfigParam param) {
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
    public Result<Void> saveItem(@RequestBody @Validated(ValidationGroup.add.class) IsvCashierItemConfigParam param) {
        cashierConfigService.saveItem(param);
        return Res.ok();
    }

    @RequestPath("修改收银台条目配置")
    @Operation(summary = "修改收银台条目配置")
    @PostMapping("/updateItem")
    public Result<Void> updateItem(@RequestBody @Validated(ValidationGroup.edit.class) IsvCashierItemConfigParam param) {
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
