package cn.daxpay.open.payment.app.merchant.controller.device;

import cn.daxpay.open.payment.device.terminal.param.TerminalBindStoreParam;
import cn.daxpay.open.payment.device.terminal.param.TerminalDeviceQuery;
import cn.daxpay.open.payment.device.terminal.result.ChannelTerminalResult;
import cn.daxpay.open.payment.device.terminal.result.TerminalDeviceResult;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/// # 商户终端管理(商户移动端)
///
/// 对应 Web 商户端 [cn.daxpay.open.payment.merchant.controller.device.MchTerminalDeviceController]
/// (路径 `/mch/device/terminal/system`), 镜像其全部端点(该控制器无导出端点), 复用同一
/// [MchTerminalDeviceService] 与菜单/权限码, 仅 URL 前缀不同(`/app-mch`)。
/// 商户号由 Service 从 [cn.daxpay.open.payment.common.context.PaymentContext] 强制写入;
/// 通道终端绑定/解绑已收口运营端, 这里仅保留商户终端管理与已绑定通道终端的只读查询。
@PermCode(menuCode = PermCodes.Merchant.Terminal.MENU)
@Validated
@Tag(name = "小程序商户端-商户终端管理")
@RestController
@RequestMapping("/app-mch/device/terminal/system")
@RequiredArgsConstructor
public class AppMerchantTerminalDeviceController {

    private final MchTerminalDeviceService mchTerminalDeviceService;

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "新增商户终端")
    @PostMapping("/add")
    public Result<Void> add(@RequestBody @Validated(ValidationGroup.add.class) TerminalDeviceParam param) {
        mchTerminalDeviceService.add(param);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "修改商户终端")
    @PostMapping("/update")
    public Result<Void> update(@RequestBody @Validated(ValidationGroup.edit.class) TerminalDeviceParam param) {
        mchTerminalDeviceService.update(param);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "商户终端分页")
    @GetMapping("/page")
    public Result<PageResult<TerminalDeviceResult>> page(PageParam pageParam, TerminalDeviceQuery query) {
        return Res.ok(mchTerminalDeviceService.page(pageParam, query));
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "商户终端详情")
    @GetMapping("/get")
    public Result<TerminalDeviceResult> findById(@NotNull(message = "{validation.field.id.notNull}") Long id) {
        return Res.ok(mchTerminalDeviceService.findById(id));
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "当前商户商户终端列表")
    @GetMapping("/list")
    public Result<List<TerminalDeviceResult>> list() {
        return Res.ok(mchTerminalDeviceService.list());
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "删除商户终端")
    @PostMapping("/delete")
    public Result<Void> delete(@NotNull(message = "{validation.field.id.notNull}") Long id) {
        mchTerminalDeviceService.delete(id);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "设为门店默认终端(同门店先清后设)")
    @PostMapping("/set-default")
    public Result<Void> setDefault(@NotNull(message = "{validation.field.id.notNull}") Long id) {
        mchTerminalDeviceService.setDefault(id);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "取消门店默认终端")
    @PostMapping("/clear-default")
    public Result<Void> clearDefault(@NotNull(message = "{validation.field.id.notNull}") Long id) {
        mchTerminalDeviceService.clearDefault(id);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "批量绑定门店")
    @PostMapping("/bind-store-batch")
    public Result<Void> bindStoreBatch(@RequestBody @Validated TerminalBindStoreParam param) {
        mchTerminalDeviceService.bindStoreBatch(param);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "已绑定的通道终端列表")
    @GetMapping("/bound-channel-list")
    public Result<List<ChannelTerminalResult>> listBoundChannel(
            @NotBlank(message = "{validation.field.type.notBlank}") String terminalNo) {
        return Res.ok(mchTerminalDeviceService.listBoundChannel(terminalNo));
    }
}
