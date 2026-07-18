package cn.daxpay.open.payment.admin.controller.device;

import cn.daxpay.open.payment.admin.param.device.TerminalChannelBindParam;
import cn.daxpay.open.payment.admin.param.device.TerminalDeviceParam;
import cn.daxpay.open.payment.admin.service.device.TerminalDeviceAdminService;
import cn.daxpay.open.payment.device.terminal.param.TerminalDeviceQuery;
import cn.daxpay.open.payment.device.terminal.result.ChannelTerminalResult;
import cn.daxpay.open.payment.device.terminal.result.TerminalDeviceResult;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/// # 系统终端管理(运营端)
@PermCode(menuCode = PermCodes.Merchant.Terminal.MENU)
@Validated
@Tag(name = "系统终端管理")
@RestController
@RequestMapping("/admin/device/terminal/system")
@RequiredArgsConstructor
public class TerminalDeviceAdminController {

    private final TerminalDeviceAdminService terminalDeviceAdminService;

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "新增系统终端")
    @PostMapping("/add")
    public Result<Void> add(@RequestBody @Validated(ValidationGroup.add.class) TerminalDeviceParam param) {
        terminalDeviceAdminService.add(param);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "修改系统终端")
    @PostMapping("/update")
    public Result<Void> update(@RequestBody @Validated(ValidationGroup.edit.class) TerminalDeviceParam param) {
        terminalDeviceAdminService.update(param);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "系统终端分页")
    @GetMapping("/page")
    public Result<PageResult<TerminalDeviceResult>> page(PageParam pageParam, TerminalDeviceQuery query) {
        return Res.ok(terminalDeviceAdminService.page(pageParam, query));
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "系统终端详情")
    @GetMapping("/get")
    public Result<TerminalDeviceResult> findById(@NotNull(message = "{validation.field.id.notNull}") Long id) {
        return Res.ok(terminalDeviceAdminService.findById(id));
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "按商户查询系统终端列表")
    @GetMapping("/list-by-mch-no")
    public Result<List<TerminalDeviceResult>> listByMchNo(
            @NotBlank(message = "{validation.field.mchNo.notBlank}") String mchNo) {
        return Res.ok(terminalDeviceAdminService.listByMchNo(mchNo));
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "删除系统终端")
    @PostMapping("/delete")
    public Result<Void> delete(@NotNull(message = "{validation.field.id.notNull}") Long id) {
        terminalDeviceAdminService.delete(id);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "绑定通道终端")
    @PostMapping("/bind")
    public Result<Void> bind(@RequestBody @Validated TerminalChannelBindParam param) {
        terminalDeviceAdminService.bind(param);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "解绑通道终端")
    @PostMapping("/unbind")
    public Result<Void> unbind(@RequestBody @Validated TerminalChannelBindParam param) {
        terminalDeviceAdminService.unbind(param);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "已绑定的通道终端列表")
    @GetMapping("/bound-channel-list")
    public Result<List<ChannelTerminalResult>> listBoundChannel(
            @NotBlank(message = "{validation.field.type.notBlank}") String terminalNo) {
        return Res.ok(terminalDeviceAdminService.listBoundChannel(terminalNo));
    }
}
