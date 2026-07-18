package cn.daxpay.open.payment.admin.controller.device;

import cn.daxpay.open.payment.admin.param.device.ChannelTerminalParam;
import cn.daxpay.open.payment.admin.param.device.TerminalChannelBindParam;
import cn.daxpay.open.payment.admin.service.device.ChannelTerminalAdminService;
import cn.daxpay.open.payment.device.terminal.param.ChannelTerminalQuery;
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

/// # 通道终端台账管理(运营端)
///
/// 挂在通道商户下维护, 权限复用 [PermCodes.Channel.Merchant]。
@PermCode(menuCode = PermCodes.Channel.Merchant.MENU)
@Validated
@Tag(name = "通道终端台账管理")
@RestController
@RequestMapping("/admin/device/terminal/channel")
@RequiredArgsConstructor
public class ChannelTerminalAdminController {

    private final ChannelTerminalAdminService channelTerminalAdminService;

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "新增通道终端")
    @PostMapping("/add")
    public Result<Void> add(@RequestBody @Validated(ValidationGroup.add.class) ChannelTerminalParam param) {
        channelTerminalAdminService.add(param);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "修改通道终端")
    @PostMapping("/update")
    public Result<Void> update(@RequestBody @Validated(ValidationGroup.edit.class) ChannelTerminalParam param) {
        channelTerminalAdminService.update(param);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "通道终端分页")
    @GetMapping("/page")
    public Result<PageResult<ChannelTerminalResult>> page(PageParam pageParam, ChannelTerminalQuery query) {
        return Res.ok(channelTerminalAdminService.page(pageParam, query));
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "通道终端详情")
    @GetMapping("/get")
    public Result<ChannelTerminalResult> findById(@NotNull(message = "{validation.field.id.notNull}") Long id) {
        return Res.ok(channelTerminalAdminService.findById(id));
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "按商户查询通道终端列表")
    @GetMapping("/list-by-mch-no")
    public Result<List<ChannelTerminalResult>> listByMchNo(
            @NotBlank(message = "{validation.field.mchNo.notBlank}") String mchNo) {
        return Res.ok(channelTerminalAdminService.listByMchNo(mchNo));
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "删除通道终端")
    @PostMapping("/delete")
    public Result<Void> delete(@NotNull(message = "{validation.field.id.notNull}") Long id) {
        channelTerminalAdminService.delete(id);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "绑定系统终端")
    @PostMapping("/bind")
    public Result<Void> bind(@RequestBody @Validated TerminalChannelBindParam param) {
        channelTerminalAdminService.bind(param);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "解绑系统终端")
    @PostMapping("/unbind")
    public Result<Void> unbind(@RequestBody @Validated TerminalChannelBindParam param) {
        channelTerminalAdminService.unbind(param);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "已绑定的系统终端列表")
    @GetMapping("/bound-system-list")
    public Result<List<TerminalDeviceResult>> listBoundSystem(
            @NotNull(message = "{validation.field.id.notNull}") Long channelTerminalId) {
        return Res.ok(channelTerminalAdminService.listBoundSystem(channelTerminalId));
    }
}
