package org.dromara.daxpay.controller.assist;

import cn.bootx.platform.core.annotation.RequestGroup;
import cn.bootx.platform.core.annotation.RequestPath;
import cn.bootx.platform.core.rest.Res;
import cn.bootx.platform.core.rest.dto.LabelValue;
import cn.bootx.platform.core.rest.param.PageParam;
import cn.bootx.platform.core.rest.result.PageResult;
import cn.bootx.platform.core.rest.result.Result;
import cn.bootx.platform.core.validation.ValidationGroup;
import org.dromara.daxpay.service.param.termina.ChannelTerminalParam;
import org.dromara.daxpay.service.param.termina.TerminalDeviceQuery;
import org.dromara.daxpay.service.param.termina.TerminalDeviceParam;
import org.dromara.daxpay.service.result.termina.ChannelTerminalResult;
import org.dromara.daxpay.service.result.termina.TerminalDeviceResult;
import org.dromara.daxpay.service.service.assist.TerminalDeviceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.dromara.core.trans.anno.TransMethodResult;
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

    @RequestPath("查询通道报备列表")
    @Operation(summary = "查询通道报备列表")
    @GetMapping("/channel/list")
    public Result<List<ChannelTerminalResult>> channelList(@NotNull(message = "支付终端设备id不能为空") Long terminalId){
        return Res.ok(terminalDeviceService.channelList(terminalId));
    }

    @RequestPath("通道报备")
    @Operation(summary = "通道报备")
    @PostMapping("/channel/submit")
    public Result<Void> channelSubmit(@NotNull(message = "通道终端设备id不能为空") Long channelTerminalId){
        terminalDeviceService.channelSubmit(channelTerminalId);
        return Res.ok();
    }

    @RequestPath("通道报备取消")
    @Operation(summary = "通道报备取消")
    @PostMapping("/channel/cancel")
    public Result<Void> channelCancel(@NotNull(message = "通道终端设备id不能为空") Long channelTerminalId){
        terminalDeviceService.channelCancel(channelTerminalId);
        return Res.ok();
    }

    @RequestPath("通道报备修改")
    @Operation(summary = "通道报备修改")
    @PostMapping("/channel/edit")
    public Result<Void> channelEdit(@RequestBody @Validated ChannelTerminalParam param){
        terminalDeviceService.channelEdit(param);
        return Res.ok();
    }

    @RequestPath("查询通道报备详情")
    @Operation(summary = "查询通道报备详情")
    @GetMapping("/channel/detail")
    public Result<ChannelTerminalResult> channelDetail(@NotNull(message = "通道终端设备id不能为空") Long channelTerminalId){
        return Res.ok(terminalDeviceService.channelDetail(channelTerminalId));
    }

    @RequestPath("通道报备新增")
    @Operation(summary = "通道报备新增")
    @PostMapping("/channel/add")
    public Result<Void> channelAdd(@NotNull(message = "支付终端设备id不能为空") Long terminalId,
                                  @NotBlank(message = "通道不能为空") String channel,
                                  @NotBlank(message = "终端类型不能为空") String terminalType){
        terminalDeviceService.channelAdd(terminalId, channel,terminalType);
        return Res.ok();
    }

    @RequestPath("检查并修复终端设备")
    @Operation(summary = "检查并修复终端设备")
    @PostMapping("/channel/checkAndRepair")
    public Result<Void> checkAndRepair(@NotNull(message = "支付终端设备id不能为空") Long terminalId){
        terminalDeviceService.checkAndRepair(terminalId);
        return Res.ok();
    }

    @RequestPath("根据应用号查询下拉列表")
    @Operation(summary = "根据应用号查询下拉列表")
    @GetMapping("/dropdown")
    public Result<List<LabelValue>> dropdown(String appId){
        return Res.ok(terminalDeviceService.dropdown(appId));
    }
}
