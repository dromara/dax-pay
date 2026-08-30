package cn.daxpay.open.payment.merchant.controller.device;

import cn.daxpay.open.payment.device.qrcode.param.DeviceQrCodeQuery;
import cn.daxpay.open.payment.device.qrcode.result.DeviceQrCodeAllocWarningResult;
import cn.daxpay.open.payment.device.qrcode.result.DeviceQrCodeResult;
import cn.daxpay.open.payment.merchant.param.device.DeviceQrCodeBindAppParam;
import cn.daxpay.open.payment.merchant.param.device.DeviceQrCodeBindStoreParam;
import cn.daxpay.open.payment.merchant.param.device.DeviceQrCodeClaimParam;
import cn.daxpay.open.payment.merchant.param.device.DeviceQrCodeParam;
import cn.daxpay.open.payment.merchant.service.device.MchDeviceQrCodeService;
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

/// # 支付码牌管理（商户端）
///
/// 对照运营端 [cn.daxpay.open.payment.admin.controller.device.DeviceQrCodeAdminController]，路径 `/mch/device/qrcode`。
/// 商户号由 Service 从 PaymentContext 强制校验；不提供批量生成、商户归属变更与删除（运营端能力），
/// 额外提供空白码认领(claim)。
@PermCode(menuCode = PermCodes.Device.QrCode.MENU)
@Validated
@Tag(name = "支付码牌管理(商户端)")
@RestController
@RequestMapping("/mch/device/qrcode")
@RequiredArgsConstructor
public class MchDeviceQrCodeController {

    private final MchDeviceQrCodeService mchDeviceQrCodeService;

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "码牌分页")
    @GetMapping("/page")
    public Result<PageResult<DeviceQrCodeResult>> page(PageParam pageParam, DeviceQrCodeQuery query) {
        return Res.ok(mchDeviceQrCodeService.page(pageParam, query));
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "根据id查询码牌")
    @GetMapping("/get")
    public Result<DeviceQrCodeResult> findById(@NotNull(message = "{validation.field.id.notNull}") Long id) {
        return Res.ok(mchDeviceQrCodeService.findById(id));
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "获取码牌扫码链接")
    @GetMapping("/get-code-link")
    public Result<String> getCodeLink(@NotBlank(message = "{validation.field.code.notBlank}") String code) {
        return Res.ok(mchDeviceQrCodeService.getCodeLink(code));
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "分账能力预警(码牌开启分账前预检, 不阻断)")
    @GetMapping("/alloc-capability-warning")
    public Result<List<DeviceQrCodeAllocWarningResult>> allocCapabilityWarning(String appId) {
        return Res.ok(mchDeviceQrCodeService.allocCapabilityWarning(appId));
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "修改码牌")
    @PostMapping("/update")
    public Result<Void> update(@RequestBody @Validated(ValidationGroup.edit.class) DeviceQrCodeParam param) {
        mchDeviceQrCodeService.update(param);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "修改码牌状态")
    @PostMapping("/change-status")
    public Result<Void> changeStatus(@NotNull(message = "{validation.field.id.notNull}") Long id,
                                     @NotBlank(message = "{validation.field.status.notBlank}") String status) {
        mchDeviceQrCodeService.changeStatus(id, status);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "批量绑定应用")
    @PostMapping("/bind-app")
    public Result<Void> bindApp(@RequestBody @Validated DeviceQrCodeBindAppParam param) {
        mchDeviceQrCodeService.bindApp(param);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "批量解绑应用")
    @PostMapping("/unbind-app")
    public Result<Void> unbindApp(@RequestBody @NotEmpty(message = "{validation.field.ids.notEmpty}") List<Long> ids) {
        mchDeviceQrCodeService.unbindApp(ids);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "批量绑定门店")
    @PostMapping("/bind-store")
    public Result<Void> bindStore(@RequestBody @Validated DeviceQrCodeBindStoreParam param) {
        mchDeviceQrCodeService.bindStore(param);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "批量解绑门店")
    @PostMapping("/unbind-store")
    public Result<Void> unbindStore(@RequestBody @NotEmpty(message = "{validation.field.ids.notEmpty}") List<Long> ids) {
        mchDeviceQrCodeService.unbindStore(ids);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "认领空白码牌")
    @PostMapping("/claim")
    public Result<Void> claim(@RequestBody @Validated DeviceQrCodeClaimParam param) {
        mchDeviceQrCodeService.claim(param);
        return Res.ok();
    }
}
