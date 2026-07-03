package cn.daxpay.open.payment.web.admin.device.printer.controller;

import cn.daxpay.open.payment.web.admin.device.printer.service.DevicePrinterAdminService;
import cn.daxpay.open.payment.device.printer.param.DevicePrinterParam;
import cn.daxpay.open.payment.device.printer.param.DevicePrinterQuery;
import cn.daxpay.open.payment.device.printer.result.DevicePrinterResult;
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

/// # 云打印设备管理(运营端)
///
@PermCode(menuCode = "device:printer")
@Validated
@Tag(name = "云打印设备管理")
@RestController
@RequestMapping("/admin/device/printer")
@RequiredArgsConstructor
public class DevicePrinterAdminController {

    private final DevicePrinterAdminService devicePrinterAdminService;

    @PermCode(code = "manage", nameCn = "云打印管理", nameEn = "Printer Manage")
    @Operation(summary = "新增云打印设备")
    @PostMapping("/add")
    public Result<Void> add(@RequestBody @Validated(ValidationGroup.add.class) DevicePrinterParam param) {
        devicePrinterAdminService.add(param);
        return Res.ok();
    }

    @PermCode(code = "manage", nameCn = "云打印管理", nameEn = "Printer Manage")
    @Operation(summary = "修改云打印设备")
    @PostMapping("/update")
    public Result<Void> update(@RequestBody @Validated(ValidationGroup.edit.class) DevicePrinterParam param) {
        devicePrinterAdminService.update(param);
        return Res.ok();
    }

    @PermCode(code = "view", nameCn = "云打印查看", nameEn = "Printer View")
    @Operation(summary = "云打印设备分页")
    @GetMapping("/page")
    public Result<PageResult<DevicePrinterResult>> page(PageParam pageParam, DevicePrinterQuery query) {
        return Res.ok(devicePrinterAdminService.page(pageParam, query));
    }

    @PermCode(code = "view", nameCn = "云打印查看", nameEn = "Printer View")
    @Operation(summary = "根据id查询云打印设备")
    @GetMapping("/get")
    public Result<DevicePrinterResult> findById(@NotNull(message = "{validation.field.id.notNull}") Long id) {
        return Res.ok(devicePrinterAdminService.findById(id));
    }

    @PermCode(code = "manage", nameCn = "云打印管理", nameEn = "Printer Manage")
    @Operation(summary = "删除云打印设备")
    @PostMapping("/delete")
    public Result<Void> delete(@NotNull(message = "{validation.field.id.notNull}") Long id) {
        devicePrinterAdminService.delete(id);
        return Res.ok();
    }

    @PermCode(code = "manage", nameCn = "云打印管理", nameEn = "Printer Manage")
    @Operation(summary = "绑定云打印设备")
    @PostMapping("/bind")
    public Result<Void> bind(@NotNull(message = "{validation.field.id.notNull}") Long id) {
        devicePrinterAdminService.bind(id);
        return Res.ok();
    }

    @PermCode(code = "manage", nameCn = "云打印管理", nameEn = "Printer Manage")
    @Operation(summary = "解绑云打印设备")
    @PostMapping("/unbind")
    public Result<Void> unbind(@NotNull(message = "{validation.field.id.notNull}") Long id) {
        devicePrinterAdminService.unbind(id);
        return Res.ok();
    }
}
