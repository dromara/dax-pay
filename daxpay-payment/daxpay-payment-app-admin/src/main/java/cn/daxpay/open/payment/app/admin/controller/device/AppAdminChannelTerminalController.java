package cn.daxpay.open.payment.app.admin.controller.device;

import cn.daxpay.open.payment.admin.service.device.ChannelTerminalAdminService;
import cn.daxpay.open.payment.device.terminal.param.ChannelTerminalQuery;
import cn.daxpay.open.payment.device.terminal.result.ChannelTerminalResult;
import cn.daxpay.open.platform.core.annotation.PermCode;
import cn.daxpay.open.platform.core.code.PermCodes;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import cn.daxpay.open.platform.core.rest.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/// # 通道终端台账管理(小程序管理端)
///
/// 小程序管理端通道终端镜像, 对应 admin 版 [ChannelTerminalAdminController]。挂在通道商户下维护,
/// 权限复用 [PermCodes.Channel.Merchant]。仅镜像只读查询, 业务编排委托 [ChannelTerminalAdminService]。
@PermCode(menuCode = PermCodes.Channel.Merchant.MENU)
@Validated
@Tag(name = "通道终端台账管理(小程序管理端)")
@RestController
@RequestMapping("/app-admin/device/terminal/channel")
@RequiredArgsConstructor
public class AppAdminChannelTerminalController {

    private final ChannelTerminalAdminService channelTerminalAdminService;

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "小程序管理端-通道终端分页")
    @GetMapping("/page")
    public Result<PageResult<ChannelTerminalResult>> page(PageParam pageParam, ChannelTerminalQuery query) {
        return Res.ok(channelTerminalAdminService.page(pageParam, query));
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "小程序管理端-通道终端详情")
    @GetMapping("/get")
    public Result<ChannelTerminalResult> findById(@NotNull(message = "{validation.field.id.notNull}") Long id) {
        return Res.ok(channelTerminalAdminService.findById(id));
    }
}