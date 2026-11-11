package cn.daxpay.open.payment.app.merchant.controller.device;

import cn.daxpay.open.payment.device.terminal.bo.TerminalExtraField;
import cn.daxpay.open.payment.device.terminal.param.ChannelTerminalQuery;
import cn.daxpay.open.payment.device.terminal.result.ChannelTerminalResult;
import cn.daxpay.open.payment.device.terminal.result.TerminalCapabilityResult;
import cn.daxpay.open.payment.merchant.param.device.MchChannelTerminalAddParam;
import cn.daxpay.open.payment.merchant.param.device.MchChannelTerminalRegisterParam;
import cn.daxpay.open.payment.merchant.param.device.MchChannelTerminalReportParam;
import cn.daxpay.open.payment.merchant.param.device.MchChannelTerminalUpdateParam;
import cn.daxpay.open.payment.merchant.service.device.MchChannelTerminalService;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/// # 通道终端台账管理(商户移动端)
///
/// 对应 Web 商户端 [cn.daxpay.open.payment.merchant.controller.device.MchChannelTerminalController]
/// (路径 `/mch/device/terminal/channel`), 镜像其全部端点(该控制器无导出端点), 复用同一
/// [MchChannelTerminalService] 与菜单/权限码(通道终端归通道商户), 仅 URL 前缀不同(`/app-mch`)。
/// 商户号由 Service 从 [cn.daxpay.open.payment.common.context.PaymentContext] 强制写入并逐方法校验归属。
@PermCode(menuCode = PermCodes.Channel.Merchant.MENU)
@Validated
@Tag(name = "小程序商户端-通道终端台账管理")
@RestController
@RequestMapping("/app-mch/device/terminal/channel")
@RequiredArgsConstructor
public class AppMerchantChannelTerminalController {

    private final MchChannelTerminalService mchChannelTerminalService;

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "通道终端分页")
    @GetMapping("/page")
    public Result<PageResult<ChannelTerminalResult>> page(PageParam pageParam, ChannelTerminalQuery query) {
        return Res.ok(mchChannelTerminalService.page(pageParam, query));
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "通道终端详情")
    @GetMapping("/get")
    public Result<ChannelTerminalResult> get(@NotNull(message = "{validation.field.id.notNull}") Long id) {
        return Res.ok(mchChannelTerminalService.get(id));
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "新增通道终端(建档即绑定商户终端)")
    @PostMapping("/add")
    public Result<Void> add(@RequestBody @Validated MchChannelTerminalAddParam param) {
        mchChannelTerminalService.add(param);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "修改通道终端(名称/备注/报备参数)")
    @PostMapping("/update")
    public Result<Void> update(@RequestBody @Validated MchChannelTerminalUpdateParam param) {
        mchChannelTerminalService.update(param);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "手动调整通道终端(通道终端号/状态; 不调通道)")
    @PostMapping("/register")
    public Result<Void> register(@RequestBody @Validated MchChannelTerminalRegisterParam param) {
        mchChannelTerminalService.register(param);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "删除通道终端(级联解绑)")
    @PostMapping("/delete")
    public Result<Void> delete(@NotNull(message = "{validation.field.id.notNull}") Long id) {
        mchChannelTerminalService.delete(id);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "终端报备(调通道API; 已绑定时可不传商户终端)")
    @PostMapping("/report")
    public Result<Void> report(@RequestBody @Validated MchChannelTerminalReportParam param) {
        mchChannelTerminalService.report(param);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "终端状态同步(调通道API回读实际状态)")
    @PostMapping("/sync")
    public Result<Void> sync(@NotNull(message = "{validation.field.id.notNull}") Long id) {
        mchChannelTerminalService.sync(id);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "终端注销(调通道API; 人工通道直接置已注销)")
    @PostMapping("/cancel")
    public Result<Void> cancel(@NotNull(message = "{validation.field.id.notNull}") Long id) {
        mchChannelTerminalService.cancel(id);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "解绑当前绑定的商户终端")
    @PostMapping("/unbind")
    public Result<Void> unbind(@NotNull(message = "{validation.field.id.notNull}") Long id) {
        mchChannelTerminalService.unbind(id);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "通道支持的终端报送类型")
    @GetMapping("/terminal-types")
    public Result<List<String>> terminalTypes(@NotBlank(message = "{validation.field.product.notBlank}") String product) {
        return Res.ok(mchChannelTerminalService.terminalTypes(product));
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "通道差异化报备参数字段定义")
    @GetMapping("/extra-fields")
    public Result<List<TerminalExtraField>> extraFields(
            @NotBlank(message = "{validation.field.product.notBlank}") String product) {
        return Res.ok(mchChannelTerminalService.extraFields(product));
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "通道终端能力(报备/注销/同步)")
    @GetMapping("/capabilities")
    public Result<TerminalCapabilityResult> capabilities(
            @NotBlank(message = "{validation.field.product.notBlank}") String product) {
        return Res.ok(mchChannelTerminalService.capabilities(product));
    }
}
