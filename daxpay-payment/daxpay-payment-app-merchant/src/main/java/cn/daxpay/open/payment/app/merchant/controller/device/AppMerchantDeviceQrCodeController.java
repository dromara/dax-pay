package cn.daxpay.open.payment.app.merchant.controller.device;

import cn.daxpay.open.payment.app.merchant.service.device.AppMerchantDeviceQrCodeService;
import cn.daxpay.open.payment.device.qrcode.param.DeviceQrCodeQuery;
import cn.daxpay.open.payment.device.qrcode.result.DeviceQrCodeAllocWarningResult;
import cn.daxpay.open.payment.device.qrcode.result.DeviceQrCodeResult;
import cn.daxpay.open.payment.merchant.param.device.DeviceQrCodeBindAppParam;
import cn.daxpay.open.payment.merchant.param.device.DeviceQrCodeBindStoreParam;
import cn.daxpay.open.payment.merchant.param.device.DeviceQrCodeClaimParam;
import cn.daxpay.open.payment.merchant.param.device.DeviceQrCodeParam;
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
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/// # 支付码牌管理（商户移动端）
///
/// 面向商户 APP/小程序的码牌自助管理。业务编排委托 [AppMerchantDeviceQrCodeService]，
/// 与商户端 Web [cn.daxpay.open.payment.merchant.controller.device.MchDeviceQrCodeController] 同源逻辑、同权限码。
@PermCode(menuCode = PermCodes.Device.QrCode.MENU)
@Validated
@Tag(name = "支付码牌管理(商户移动端)")
@RestController
@RequestMapping("/app-mch/device/qrcode")
@RequiredArgsConstructor
public class AppMerchantDeviceQrCodeController {

    private final AppMerchantDeviceQrCodeService deviceQrCodeService;

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "码牌分页")
    @GetMapping("/page")
    public Result<PageResult<DeviceQrCodeResult>> page(PageParam pageParam, DeviceQrCodeQuery query) {
        return Res.ok(deviceQrCodeService.page(pageParam, query));
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "根据id查询码牌")
    @GetMapping("/get")
    public Result<DeviceQrCodeResult> findById(@NotNull(message = "{validation.field.id.notNull}") Long id) {
        return Res.ok(deviceQrCodeService.findById(id));
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "获取码牌扫码链接")
    @GetMapping("/get-code-link")
    public Result<String> getCodeLink(@NotBlank(message = "{validation.field.code.notBlank}") String code) {
        return Res.ok(deviceQrCodeService.getCodeLink(code));
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "分账能力预警(码牌开启分账前预检, 不阻断)")
    @GetMapping("/alloc-capability-warning")
    public Result<List<DeviceQrCodeAllocWarningResult>> allocCapabilityWarning(String appId) {
        return Res.ok(deviceQrCodeService.allocCapabilityWarning(appId));
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "修改码牌")
    @PostMapping("/update")
    public Result<Void> update(@RequestBody @Validated(ValidationGroup.edit.class) DeviceQrCodeParam param) {
        deviceQrCodeService.update(param);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "修改码牌状态")
    @PostMapping("/change-status")
    public Result<Void> changeStatus(@NotNull(message = "{validation.field.id.notNull}") Long id,
                                     @NotBlank(message = "{validation.field.status.notBlank}") String status) {
        deviceQrCodeService.changeStatus(id, status);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "批量绑定应用")
    @PostMapping("/bind-app")
    public Result<Void> bindApp(@RequestBody @Validated DeviceQrCodeBindAppParam param) {
        deviceQrCodeService.bindApp(param);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "批量解绑应用")
    @PostMapping("/unbind-app")
    public Result<Void> unbindApp(@RequestBody @NotEmpty(message = "{validation.field.ids.notEmpty}") List<Long> ids) {
        deviceQrCodeService.unbindApp(ids);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "批量绑定门店")
    @PostMapping("/bind-store")
    public Result<Void> bindStore(@RequestBody @Validated DeviceQrCodeBindStoreParam param) {
        deviceQrCodeService.bindStore(param);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "批量解绑门店")
    @PostMapping("/unbind-store")
    public Result<Void> unbindStore(@RequestBody @NotEmpty(message = "{validation.field.ids.notEmpty}") List<Long> ids) {
        deviceQrCodeService.unbindStore(ids);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "认领空白码牌")
    @PostMapping("/claim")
    public Result<Void> claim(@RequestBody @Validated DeviceQrCodeClaimParam param) {
        deviceQrCodeService.claim(param);
        return Res.ok();
    }
}
