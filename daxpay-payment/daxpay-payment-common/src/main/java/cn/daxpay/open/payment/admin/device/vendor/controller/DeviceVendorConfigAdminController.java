package cn.daxpay.open.payment.admin.device.vendor.controller;

import cn.daxpay.open.payment.admin.device.vendor.service.DeviceVendorConfigAdminService;
import cn.daxpay.open.payment.device.vendor.param.DeviceVendorConfigParam;
import cn.daxpay.open.payment.device.vendor.param.DeviceVendorConfigQuery;
import cn.daxpay.open.payment.device.vendor.result.DeviceVendorConfigResult;
import cn.daxpay.open.platform.core.annotation.PermCode;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import cn.daxpay.open.platform.core.rest.result.Result;
import cn.daxpay.open.platform.core.validation.ValidationGroup;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/// # 设备厂商配置管理(运营端)
@PermCode(menuCode = "device:vendor_config")
@Validated
@Tag(name = "设备厂商配置管理")
@RestController
@RequestMapping("/admin/device/vendor-config")
@RequiredArgsConstructor
public class DeviceVendorConfigAdminController {

    private final DeviceVendorConfigAdminService deviceVendorConfigAdminService;

    @PermCode(code = "manage", nameCn = "厂商配置管理", nameEn = "Vendor Config Manage")
    @Operation(summary = "新增厂商配置")
    @PostMapping("/add")
    public Result<Void> add(@RequestBody @Validated(ValidationGroup.add.class) DeviceVendorConfigParam param) {
        deviceVendorConfigAdminService.add(param);
        return Res.ok();
    }

    @PermCode(code = "manage", nameCn = "厂商配置管理", nameEn = "Vendor Config Manage")
    @Operation(summary = "修改厂商配置")
    @PostMapping("/update")
    public Result<Void> update(@RequestBody @Validated(ValidationGroup.edit.class) DeviceVendorConfigParam param) {
        deviceVendorConfigAdminService.update(param);
        return Res.ok();
    }

    @PermCode(code = "view", nameCn = "厂商配置查看", nameEn = "Vendor Config View")
    @Operation(summary = "厂商配置分页")
    @GetMapping("/page")
    public Result<PageResult<DeviceVendorConfigResult>> page(PageParam pageParam, DeviceVendorConfigQuery query) {
        return Res.ok(deviceVendorConfigAdminService.page(pageParam, query));
    }

    @PermCode(code = "view", nameCn = "厂商配置查看", nameEn = "Vendor Config View")
    @Operation(summary = "根据id查询厂商配置")
    @GetMapping("/get")
    public Result<DeviceVendorConfigResult> findById(@NotNull(message = "{validation.field.id.notNull}") Long id) {
        return Res.ok(deviceVendorConfigAdminService.findById(id));
    }

    @PermCode(code = "manage", nameCn = "厂商配置管理", nameEn = "Vendor Config Manage")
    @Operation(summary = "删除厂商配置")
    @PostMapping("/delete")
    public Result<Void> delete(@NotNull(message = "{validation.field.id.notNull}") Long id) {
        deviceVendorConfigAdminService.delete(id);
        return Res.ok();
    }

    @PermCode(code = "view", nameCn = "厂商配置查看", nameEn = "Vendor Config View")
    @Operation(summary = "查询指定设备类型和厂商的启用配置列表")
    @GetMapping("/list-enabled-by-vendor")
    public Result<List<DeviceVendorConfigResult>> listEnabledByVendor(String deviceType, String vendorCode) {
        return Res.ok(deviceVendorConfigAdminService.listEnabledByVendor(deviceType, vendorCode));
    }
}
