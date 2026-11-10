package cn.daxpay.open.payment.app.admin.controller.device;

import cn.daxpay.open.payment.admin.param.device.TerminalDeviceParam;
import cn.daxpay.open.payment.admin.service.device.TerminalDeviceAdminService;
import cn.daxpay.open.payment.device.terminal.param.TerminalBindStoreParam;
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

/// # 系统终端管理(小程序管理端)
///
/// 小程序管理端系统终端镜像, 对应 admin 版 [TerminalDeviceAdminController]。镜像新增/编辑/删除/
/// 门店默认终端/总览等端点; 批量绑定门店与通道终端绑定/解绑(可从通道终端侧完成)留在 Web 端,
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
    @Operation(summary = "商户终端总览分页(只读, 含关联通道终端)")
    @GetMapping("/overview-page")
    public Result<PageResult<TerminalDeviceResult>> overviewPage(PageParam pageParam, TerminalDeviceQuery query) {
        return Res.ok(terminalDeviceAdminService.overviewPage(pageParam, query));
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "删除系统终端")
    @PostMapping("/delete")
    public Result<Void> delete(@NotNull(message = "{validation.field.id.notNull}") Long id) {
        terminalDeviceAdminService.delete(id);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "设为门店默认终端(同门店先清后设)")
    @PostMapping("/set-default")
    public Result<Void> setDefault(@NotNull(message = "{validation.field.id.notNull}") Long id) {
        terminalDeviceAdminService.setDefault(id);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "取消门店默认终端")
    @PostMapping("/clear-default")
    public Result<Void> clearDefault(@NotNull(message = "{validation.field.id.notNull}") Long id) {
        terminalDeviceAdminService.clearDefault(id);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "小程序管理端-批量绑定门店")
    @PostMapping("/bind-store-batch")
    public Result<Void> bindStoreBatch(@RequestBody @Validated TerminalBindStoreParam param) {
        terminalDeviceAdminService.bindStoreBatch(param);
        return Res.ok();
    }
}
