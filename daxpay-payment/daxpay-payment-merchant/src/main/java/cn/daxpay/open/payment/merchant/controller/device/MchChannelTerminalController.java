package cn.daxpay.open.payment.merchant.controller.device;

import cn.daxpay.open.payment.device.terminal.param.ChannelTerminalQuery;
import cn.daxpay.open.payment.device.terminal.result.ChannelTerminalResult;
import cn.daxpay.open.payment.merchant.service.device.MchChannelTerminalService;
import cn.daxpay.open.platform.core.annotation.PermCode;
import cn.daxpay.open.platform.core.code.PermCodes;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import cn.daxpay.open.platform.core.rest.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/// # 通道终端台账（商户端，只读）
///
/// 通道侧终端的建档/报备/绑定维护收口运营端, 商户端只保留只读查询(对账与核对绑定关系),
/// 商户号由 Service 从 PaymentContext 强制写入。权限沿用通道商户菜单(通道终端归通道商户)。
@PermCode(menuCode = PermCodes.Channel.Merchant.MENU)
@Validated
@Tag(name = "通道终端台账(商户端)")
@RestController
@RequestMapping("/mch/device/terminal/channel")
@RequiredArgsConstructor
public class MchChannelTerminalController {

    private final MchChannelTerminalService mchChannelTerminalService;

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "通道终端分页")
    @GetMapping("/page")
    public Result<PageResult<ChannelTerminalResult>> page(PageParam pageParam, ChannelTerminalQuery query) {
        return Res.ok(mchChannelTerminalService.page(pageParam, query));
    }
}
