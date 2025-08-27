package org.dromara.daxpay.controller.device.terminal;

import cn.bootx.platform.core.annotation.ClientCode;
import cn.bootx.platform.core.annotation.RequestGroup;
import cn.bootx.platform.core.annotation.RequestPath;
import cn.bootx.platform.core.rest.Res;
import cn.bootx.platform.core.rest.dto.LabelValue;
import cn.bootx.platform.core.rest.param.PageParam;
import cn.bootx.platform.core.rest.result.PageResult;
import cn.bootx.platform.core.rest.result.Result;
import cn.bootx.platform.core.validation.ValidationGroup;
import org.dromara.daxpay.service.common.code.DaxPayCode;
import org.dromara.daxpay.service.device.param.commom.AssignMerchantParam;
import org.dromara.daxpay.service.device.param.terminal.TerminalDeviceParam;
import org.dromara.daxpay.service.device.param.terminal.TerminalDeviceQuery;
import org.dromara.daxpay.service.device.result.terminal.TerminalDeviceResult;
import org.dromara.daxpay.service.device.service.terminal.TerminalDeviceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.dromara.core.trans.anno.TransMethodResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 辅助终端报备
 * @author xxm
 * @since 2025/3/9
 */
@Validated
@Tag(name = "辅助终端报备")
@RestController
@RequestMapping("/device/terminal")
@ClientCode({DaxPayCode.Client.ADMIN, DaxPayCode.Client.MERCHANT})
@RequestGroup(groupCode = "TerminalDevice", groupName = "辅助终端报备", moduleCode = "device")
@RequiredArgsConstructor
public class TerminalDeviceController {
    private final TerminalDeviceService terminalDeviceService;

    @TransMethodResult
    @RequestPath("分页查询")
    @Operation(summary = "分页查询")
    @GetMapping("/page")
    public Result<PageResult<TerminalDeviceResult>> page(PageParam pageParam, TerminalDeviceQuery query){
        return Res.ok(terminalDeviceService.page(pageParam, query));
    }

    @TransMethodResult
    @RequestPath("根据ID查询")
    @Operation(summary = "根据ID查询")
    @GetMapping("/findById")
    public Result<TerminalDeviceResult> findById(@NotNull(message = "支付终端设备id不能为空") Long id){
        return Res.ok(terminalDeviceService.findById(id));
    }

    @RequestPath("新增")
    @Operation(summary = "新增")
    @PostMapping("/add")
    public Result<Void> add(@RequestBody @Validated(ValidationGroup.add.class) TerminalDeviceParam param){
        terminalDeviceService.add(param);
        return Res.ok();
    }

    @ClientCode({DaxPayCode.Client.ADMIN})
    @RequestPath("绑定商户")
    @Operation(summary = "绑定商户")
    @PostMapping("/bindMerchant")
    public Result<Void> bindMerchant(@Validated @RequestBody AssignMerchantParam param) {
        terminalDeviceService.bindMerchant(param);
        return Res.ok();
    }

    @ClientCode({DaxPayCode.Client.ADMIN})
    @RequestPath("解绑商户")
    @Operation(summary = "解绑商户")
    @PostMapping("/unbindMerchant")
    public Result<Void> unbindMerchant(@Validated @RequestBody AssignMerchantParam param) {
        terminalDeviceService.unbindMerchant(param);
        return Res.ok();
    }

    @ClientCode({DaxPayCode.Client.MERCHANT})
    @RequestPath("绑定应用")
    @Operation(summary = "绑定应用")
    @PostMapping("/bindApp")
    public Result<Void> bindApp(@RequestBody @Validated AssignMerchantParam param){
        terminalDeviceService.bindApp(param);
        return Res.ok();
    }

    @ClientCode({DaxPayCode.Client.MERCHANT})
    @RequestPath("解绑应用")
    @Operation(summary = "解绑应用")
    @PostMapping("/unbindApp")
    public Result<Void> unbindApp(@RequestBody @Validated AssignMerchantParam param){
        terminalDeviceService.unbindApp(param);
        return Res.ok();
    }

    @RequestPath("修改")
    @Operation(summary = "修改")
    @PostMapping("/edit")
    public Result<Void> edit(@RequestBody @Validated(ValidationGroup.edit.class) TerminalDeviceParam param){
        terminalDeviceService.edit(param);
        return Res.ok();
    }

    @RequestPath("删除")
    @Operation(summary = "删除")
    @PostMapping("/delete")
    public Result<Void> delete(@NotNull(message = "支付终端设备id不能为空") Long id){
        terminalDeviceService.delete(id);
        return Res.ok();
    }

    @RequestPath("根据AppId获取下拉列表")
    @Operation(summary = "根据AppId获取下拉列表")
    @GetMapping("/dropdownByAppId")
    public Result<List<LabelValue>> dropdownByAppId(@NotNull(message = "AppId不能为空") String appId){
        return Res.ok(terminalDeviceService.dropdownByAppId(appId));
    }
}
