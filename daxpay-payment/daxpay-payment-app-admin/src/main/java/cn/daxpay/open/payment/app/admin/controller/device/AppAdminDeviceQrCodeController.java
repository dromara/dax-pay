package cn.daxpay.open.payment.app.admin.controller.device;

import cn.daxpay.open.payment.admin.param.device.DeviceQrCodeBatchParam;
import cn.daxpay.open.payment.admin.param.device.DeviceQrCodeBindAppParam;
import cn.daxpay.open.payment.admin.param.device.DeviceQrCodeBindMerchantParam;
import cn.daxpay.open.payment.admin.param.device.DeviceQrCodeBindStoreParam;
import cn.daxpay.open.payment.admin.param.device.DeviceQrCodeParam;
import cn.daxpay.open.payment.admin.result.device.DeviceQrCodeAllocWarningResult;
import cn.daxpay.open.payment.admin.service.device.DeviceQrCodeAdminService;
import cn.daxpay.open.payment.device.qrcode.param.DeviceQrCodeQuery;
import cn.daxpay.open.payment.device.qrcode.result.DeviceQrCodeResult;
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

/// # 支付码牌管理(小程序管理端镜像)
///
/// 对应 admin 版 [DeviceQrCodeAdminController], 镜像全部端点, 复用同一 Service 与权限码。
@PermCode(menuCode = PermCodes.Device.QrCode.MENU)
@Validated
@Tag(name = "小程序管理端-支付码牌管理")
@RestController
@RequestMapping("/app-admin/device/qrcode")
@RequiredArgsConstructor
public class AppAdminDeviceQrCodeController {

    private final DeviceQrCodeAdminService deviceQrCodeAdminService;

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "批量创建空白码牌")
    @PostMapping("/create-batch")
    public Result<Void> createBatch(@RequestBody @Validated DeviceQrCodeBatchParam param) {
        deviceQrCodeAdminService.createBatch(param);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "判断批次号是否已存在")
    @GetMapping("/exists-by-batch-no")
    public Result<Boolean> existsByBatchNo(@NotBlank(message = "{validation.field.batchNo.notBlank}") String batchNo) {
        return Res.ok(deviceQrCodeAdminService.existsByBatchNo(batchNo));
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "分账能力预警(码牌开启分账前预检, 不阻断)")
    @GetMapping("/alloc-capability-warning")
    public Result<List<DeviceQrCodeAllocWarningResult>> allocCapabilityWarning(
            @NotBlank(message = "{validation.field.mchNo.notBlank}") String mchNo, String appId) {
        return Res.ok(deviceQrCodeAdminService.allocCapabilityWarning(mchNo, appId));
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "批量绑定商户")
    @PostMapping("/bind-merchant")
    public Result<Void> bindMerchant(@RequestBody @Validated DeviceQrCodeBindMerchantParam param) {
        deviceQrCodeAdminService.bindMerchant(param);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "批量解绑商户")
    @PostMapping("/unbind-merchant")
    public Result<Void> unbindMerchant(@RequestBody @NotEmpty(message = "{validation.field.ids.notEmpty}") List<Long> ids) {
        deviceQrCodeAdminService.unbindMerchant(ids);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "批量绑定应用")
    @PostMapping("/bind-app")
    public Result<Void> bindApp(@RequestBody @Validated DeviceQrCodeBindAppParam param) {
        deviceQrCodeAdminService.bindApp(param);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "批量解绑应用")
    @PostMapping("/unbind-app")
    public Result<Void> unbindApp(@RequestBody @NotEmpty(message = "{validation.field.ids.notEmpty}") List<Long> ids) {
        deviceQrCodeAdminService.unbindApp(ids);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "批量绑定门店")
    @PostMapping("/bind-store")
    public Result<Void> bindStore(@RequestBody @Validated DeviceQrCodeBindStoreParam param) {
        deviceQrCodeAdminService.bindStore(param);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "批量解绑门店")
    @PostMapping("/unbind-store")
    public Result<Void> unbindStore(@RequestBody @NotEmpty(message = "{validation.field.ids.notEmpty}") List<Long> ids) {
        deviceQrCodeAdminService.unbindStore(ids);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "修改码牌")
    @PostMapping("/update")
    public Result<Void> update(@RequestBody @Validated(ValidationGroup.edit.class) DeviceQrCodeParam param) {
        deviceQrCodeAdminService.update(param);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "码牌分页")
    @GetMapping("/page")
    public Result<PageResult<DeviceQrCodeResult>> page(PageParam pageParam, DeviceQrCodeQuery query) {
        return Res.ok(deviceQrCodeAdminService.page(pageParam, query));
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "根据id查询码牌")
    @GetMapping("/get")
    public Result<DeviceQrCodeResult> findById(@NotNull(message = "{validation.field.id.notNull}") Long id) {
        return Res.ok(deviceQrCodeAdminService.findById(id));
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "获取码牌扫码链接")
    @GetMapping("/get-code-link")
    public Result<String> getCodeLink(@NotBlank(message = "{validation.field.code.notBlank}") String code) {
        return Res.ok(deviceQrCodeAdminService.getCodeLink(code));
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "删除码牌")
    @PostMapping("/delete")
    public Result<Void> delete(@NotNull(message = "{validation.field.id.notNull}") Long id) {
        deviceQrCodeAdminService.delete(id);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "修改码牌状态")
    @PostMapping("/change-status")
    public Result<Void> changeStatus(@NotNull(message = "{validation.field.id.notNull}") Long id,
                                     @NotBlank(message = "{validation.field.status.notBlank}") String status) {
        deviceQrCodeAdminService.changeStatus(id, status);
        return Res.ok();
    }
}