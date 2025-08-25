package org.dromara.daxpay.controller.device.terminal;

import cn.bootx.platform.core.annotation.ClientCode;
import cn.bootx.platform.core.annotation.RequestGroup;
import cn.bootx.platform.core.annotation.RequestPath;
import cn.bootx.platform.core.rest.Res;
import cn.bootx.platform.core.rest.result.Result;
import org.dromara.daxpay.service.common.code.DaxPayCode;
import org.dromara.daxpay.service.device.param.terminal.ChannelGetAndCreateParam;
import org.dromara.daxpay.service.device.param.terminal.ChannelTerminalParam;
import org.dromara.daxpay.service.device.result.terminal.ChannelTerminalResult;
import org.dromara.daxpay.service.device.service.terminal.ChannelTerminalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 通道辅助终端报备
 * @author xxm
 * @since 2025/7/7
 */
@Validated
@Tag(name = "通道辅助终端报备")
@RestController
@RequestMapping("/device/terminal/channel")
@ClientCode({DaxPayCode.Client.ADMIN, DaxPayCode.Client.MERCHANT})
@RequestGroup(groupCode = "TerminalDevice", groupName = "辅助终端报备", moduleCode = "device")
@RequiredArgsConstructor
public class ChannelTerminalController {
    private final ChannelTerminalService terminalDeviceService;


    @RequestPath("查询通道报备列表")
    @Operation(summary = "查询通道报备列表")
    @GetMapping("/list")
    public Result<List<ChannelTerminalResult>> channelList(@NotNull(message = "支付终端设备id不能为空") Long terminalId){
        return Res.ok(terminalDeviceService.channelList(terminalId));
    }

    @RequestPath("通道报备")
    @Operation(summary = "通道报备")
    @PostMapping("/submit")
    public Result<Void> channelSubmit(@NotNull(message = "通道终端设备id不能为空") Long channelTerminalId){
        terminalDeviceService.channelSubmit(channelTerminalId);
        return Res.ok();
    }

    @RequestPath("通道报备取消")
    @Operation(summary = "通道报备取消")
    @PostMapping("/cancel")
    public Result<Void> channelCancel(@NotNull(message = "通道终端设备id不能为空") Long channelTerminalId){
        terminalDeviceService.channelCancel(channelTerminalId);
        return Res.ok();
    }

    @RequestPath("通道报备状态同步")
    @Operation(summary = "通道报备状态同步")
    @PostMapping("/sync")
    public Result<Void> channel(@NotNull(message = "通道终端设备id不能为空") Long channelTerminalId){
        terminalDeviceService.channelSync(channelTerminalId);
        return Res.ok();
    }

    @RequestPath("通道报备修改")
    @Operation(summary = "通道报备修改")
    @PostMapping("/edit")
    public Result<Void> channelEdit(@RequestBody @Validated ChannelTerminalParam param){
        terminalDeviceService.channelEdit(param);
        return Res.ok();
    }

    @RequestPath("查询通道报备详情")
    @Operation(summary = "查询通道报备详情")
    @GetMapping("/detail")
    public Result<ChannelTerminalResult> channelDetail(@NotNull(message = "通道终端设备id不能为空") Long channelTerminalId){
        return Res.ok(terminalDeviceService.channelDetail(channelTerminalId));
    }

    @RequestPath("通道报备新增")
    @Operation(summary = "通道报备新增")
    @PostMapping("/getAndCreate")
    public Result<ChannelTerminalResult> getAndCreate(@Validated ChannelGetAndCreateParam param){
        return Res.ok(terminalDeviceService.getAndCreate(param));
    }

}
