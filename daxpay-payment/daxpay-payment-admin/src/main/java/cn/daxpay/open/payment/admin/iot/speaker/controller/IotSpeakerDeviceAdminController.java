package cn.daxpay.open.payment.admin.iot.speaker.controller;

import cn.daxpay.open.payment.admin.iot.speaker.service.IotSpeakerDeviceAdminService;
import cn.daxpay.open.payment.iot.speaker.param.IotSpeakerDeviceParam;
import cn.daxpay.open.payment.iot.speaker.param.IotSpeakerDeviceQuery;
import cn.daxpay.open.payment.iot.speaker.result.IotSpeakerDeviceResult;
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

/// # 云音响设备管理(运营端)
///
@PermCode(menuCode = "payment:iot:speaker")
@Validated
@Tag(name = "云音响设备管理")
@RestController
@RequestMapping("/admin/iot/speaker-device")
@RequiredArgsConstructor
public class IotSpeakerDeviceAdminController {

    private final IotSpeakerDeviceAdminService iotSpeakerDeviceAdminService;

    @PermCode(code = "add", nameCn = "云音响新增", nameEn = "Speaker Add")
    @Operation(summary = "新增云音响设备")
    @PostMapping("/add")
    public Result<Void> add(@RequestBody @Validated(ValidationGroup.add.class) IotSpeakerDeviceParam param) {
        iotSpeakerDeviceAdminService.add(param);
        return Res.ok();
    }

    @PermCode(code = "edit", nameCn = "云音响编辑", nameEn = "Speaker Edit")
    @Operation(summary = "修改云音响设备")
    @PostMapping("/update")
    public Result<Void> update(@RequestBody @Validated(ValidationGroup.edit.class) IotSpeakerDeviceParam param) {
        iotSpeakerDeviceAdminService.update(param);
        return Res.ok();
    }

    @PermCode(code = "view", nameCn = "云音响查看", nameEn = "Speaker View")
    @Operation(summary = "云音响设备分页")
    @GetMapping("/page")
    public Result<PageResult<IotSpeakerDeviceResult>> page(PageParam pageParam, IotSpeakerDeviceQuery query) {
        return Res.ok(iotSpeakerDeviceAdminService.page(pageParam, query));
    }

    @PermCode(code = "view", nameCn = "云音响查看", nameEn = "Speaker View")
    @Operation(summary = "根据id查询云音响设备")
    @GetMapping("/get")
    public Result<IotSpeakerDeviceResult> findById(@NotNull(message = "{validation.field.id.notNull}") Long id) {
        return Res.ok(iotSpeakerDeviceAdminService.findById(id));
    }

    @PermCode(code = "delete", nameCn = "云音响删除", nameEn = "Speaker Delete")
    @Operation(summary = "删除云音响设备")
    @PostMapping("/delete")
    public Result<Void> delete(@NotNull(message = "{validation.field.id.notNull}") Long id) {
        iotSpeakerDeviceAdminService.delete(id);
        return Res.ok();
    }

    @PermCode(code = "edit", nameCn = "云音响编辑", nameEn = "Speaker Edit")
    @Operation(summary = "绑定云音响设备")
    @PostMapping("/bind")
    public Result<Void> bind(@NotNull(message = "{validation.field.id.notNull}") Long id) {
        iotSpeakerDeviceAdminService.bind(id);
        return Res.ok();
    }

    @PermCode(code = "edit", nameCn = "云音响编辑", nameEn = "Speaker Edit")
    @Operation(summary = "解绑云音响设备")
    @PostMapping("/unbind")
    public Result<Void> unbind(@NotNull(message = "{validation.field.id.notNull}") Long id) {
        iotSpeakerDeviceAdminService.unbind(id);
        return Res.ok();
    }
}
