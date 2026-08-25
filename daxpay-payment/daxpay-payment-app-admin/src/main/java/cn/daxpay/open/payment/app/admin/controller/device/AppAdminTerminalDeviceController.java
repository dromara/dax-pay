package cn.daxpay.open.payment.app.admin.controller.device;

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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/// # 系统终端管理(小程序管理端)
///
/// 小程序管理端系统终端镜像, 对应 admin 版 [TerminalDeviceAdminController]。仅镜像只读查询,
/// 业务编排委托 [TerminalDeviceAdminService]。
@PermCode(menuCode = PermCodes.Merchant.Terminal.MENU)
@Validated
@Tag(name = "系统终端管理(小程序管理端)")
@RestController
@RequestMapping("/app-admin/device/terminal/system")
@RequiredArgsConstructor
public class AppAdminTerminalDeviceController {

    private final TerminalDeviceAdminService terminalDeviceAdminService;

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "小程序管理端-系统终端分页")
    @GetMapping("/page")
    public Result<PageResult<TerminalDeviceResult>> page(PageParam pageParam, TerminalDeviceQuery query) {
        return Res.ok(terminalDeviceAdminService.page(pageParam, query));
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "小程序管理端-系统终端详情")
    @GetMapping("/get")
    public Result<TerminalDeviceResult> findById(@NotNull(message = "{validation.field.id.notNull}") Long id) {
        return Res.ok(terminalDeviceAdminService.findById(id));
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "小程序管理端-已绑定的通道终端列表")
    @GetMapping("/bound-channel-list")
    public Result<List<ChannelTerminalResult>> listBoundChannel(
            @NotBlank(message = "{validation.field.type.notBlank}") String terminalNo) {
        return Res.ok(terminalDeviceAdminService.listBoundChannel(terminalNo));
    }
}