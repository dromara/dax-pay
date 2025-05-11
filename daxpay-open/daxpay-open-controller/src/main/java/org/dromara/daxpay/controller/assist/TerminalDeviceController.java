package org.dromara.daxpay.controller.assist;

import cn.bootx.platform.core.annotation.RequestGroup;
import cn.bootx.platform.core.annotation.RequestPath;
import cn.bootx.platform.core.rest.Res;
import cn.bootx.platform.core.rest.dto.LabelValue;
import cn.bootx.platform.core.rest.param.PageParam;
import cn.bootx.platform.core.rest.result.PageResult;
import cn.bootx.platform.core.rest.result.Result;
import cn.bootx.platform.core.validation.ValidationGroup;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.dromara.core.trans.anno.TransMethodResult;
import org.dromara.daxpay.service.param.termina.TerminalDeviceParam;
import org.dromara.daxpay.service.param.termina.TerminalDeviceQuery;
import org.dromara.daxpay.service.result.termina.TerminalDeviceResult;
import org.dromara.daxpay.service.service.assist.TerminalDeviceService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 支付终端设备
 * @author xxm
 * @since 2025/3/9
 */
@Validated
@Tag(name = "支付终端设备管理")
@RestController
@RequestMapping("/terminal/device")
@RequestGroup(groupCode = "TerminalDevice", groupName = "支付终端设备管理", moduleCode = "PayConfig")
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

    @RequestPath("根据应用号查询下拉列表")
    @Operation(summary = "根据应用号查询下拉列表")
    @GetMapping("/dropdown")
    public Result<List<LabelValue>> dropdown(String appId){
        return Res.ok(terminalDeviceService.dropdown(appId));
    }
}
