package cn.daxpay.open.payment.merchant.controller.device;

import cn.daxpay.open.payment.device.terminal.param.TerminalDeviceQuery;
import cn.daxpay.open.payment.device.terminal.result.ChannelTerminalResult;
import cn.daxpay.open.payment.device.terminal.result.TerminalDeviceResult;
import cn.daxpay.open.payment.merchant.param.device.TerminalChannelBindParam;
import cn.daxpay.open.payment.merchant.param.device.TerminalDeviceParam;
import cn.daxpay.open.payment.merchant.service.device.MchTerminalDeviceService;
import cn.daxpay.open.platform.core.annotation.PermCode;
import cn.daxpay.open.platform.core.code.PermCodes;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import cn.daxpay.open.platform.core.rest.result.Result;
import cn.daxpay.open.platform.core.validation.ValidationGroup;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/// # 系统终端管理（商户端）
///
/// 对照运营端 [TerminalDeviceAdminController]，路径 `/mch/device/terminal/system`。
/// 商户号由 Service 从 PaymentContext 强制写入；额外提供通道终端候选列表供绑定抽屉使用。
@PermCode(menuCode = PermCodes.Merchant.Terminal.MENU)
@Validated
@Tag(name = "系统终端管理(商户端)")
@RestController
@RequestMapping("/mch/device/terminal/system")
@RequiredArgsConstructor
public class MchTerminalDeviceController {

    private final MchTerminalDeviceService mchTerminalDeviceService;

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "新增系统终端")
    @PostMapping("/add")
    public Result<Void> add(@RequestBody @Validated(ValidationGroup.add.class) TerminalDeviceParam param) {
        mchTerminalDeviceService.add(param);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "修改系统终端")
    @PostMapping("/update")
    public Result<Void> update(@RequestBody @Validated(ValidationGroup.edit.class) TerminalDeviceParam param) {
        mchTerminalDeviceService.update(param);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "系统终端分页")
    @GetMapping("/page")
    public Result<PageResult<TerminalDeviceResult>> page(PageParam pageParam, TerminalDeviceQuery query) {
        return Res.ok(mchTerminalDeviceService.page(pageParam, query));
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "系统终端详情")
    @GetMapping("/get")
    public Result<TerminalDeviceResult> findById(@NotNull(message = "{validation.field.id.notNull}") Long id) {
        return Res.ok(mchTerminalDeviceService.findById(id));
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "当前商户系统终端列表")
    @GetMapping("/list")
    public Result<List<TerminalDeviceResult>> list() {
        return Res.ok(mchTerminalDeviceService.list());
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "删除系统终端")
    @PostMapping("/delete")
    public Result<Void> delete(@NotNull(message = "{validation.field.id.notNull}") Long id) {
        mchTerminalDeviceService.delete(id);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "绑定通道终端")
    @PostMapping("/bind")
    public Result<Void> bind(@RequestBody @Validated TerminalChannelBindParam param) {
        mchTerminalDeviceService.bind(param);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "解绑通道终端")
    @PostMapping("/unbind")
    public Result<Void> unbind(@RequestBody @Validated TerminalChannelBindParam param) {
        mchTerminalDeviceService.unbind(param);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "已绑定的通道终端列表")
    @GetMapping("/bound-channel-list")
    public Result<List<ChannelTerminalResult>> listBoundChannel(
            @NotBlank(message = "{validation.field.type.notBlank}") String terminalNo) {
        return Res.ok(mchTerminalDeviceService.listBoundChannel(terminalNo));
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "当前商户通道终端列表（绑定候选）")
    @GetMapping("/channel-terminal-list")
    public Result<List<ChannelTerminalResult>> listChannelTerminal() {
        return Res.ok(mchTerminalDeviceService.listChannelTerminal());
    }
}
