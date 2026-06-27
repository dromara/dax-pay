package cn.daxpay.open.payment.admin.device.speaker.controller;

import cn.daxpay.open.payment.admin.device.speaker.service.DeviceSpeakerAdminService;
import cn.daxpay.open.payment.device.speaker.param.DeviceSpeakerParam;
import cn.daxpay.open.payment.device.speaker.param.DeviceSpeakerQuery;
import cn.daxpay.open.payment.device.speaker.result.DeviceSpeakerResult;
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

/// # 云音箱设备管理(运营端)
///
@PermCode(menuCode = "device:speaker")
@Validated
@Tag(name = "云音箱设备管理")
@RestController
@RequestMapping("/admin/device/speaker")
@RequiredArgsConstructor
public class DeviceSpeakerAdminController {

    private final DeviceSpeakerAdminService deviceSpeakerAdminService;

    @PermCode(code = "manage", nameCn = "云音箱管理", nameEn = "Speaker Manage")
    @Operation(summary = "新增云音箱设备")
    @PostMapping("/add")
    public Result<Void> add(@RequestBody @Validated(ValidationGroup.add.class) DeviceSpeakerParam param) {
        deviceSpeakerAdminService.add(param);
        return Res.ok();
    }

    @PermCode(code = "manage", nameCn = "云音箱管理", nameEn = "Speaker Manage")
    @Operation(summary = "修改云音箱设备")
    @PostMapping("/update")
    public Result<Void> update(@RequestBody @Validated(ValidationGroup.edit.class) DeviceSpeakerParam param) {
        deviceSpeakerAdminService.update(param);
        return Res.ok();
    }

    @PermCode(code = "view", nameCn = "云音箱查看", nameEn = "Speaker View")
    @Operation(summary = "云音箱设备分页")
    @GetMapping("/page")
    public Result<PageResult<DeviceSpeakerResult>> page(PageParam pageParam, DeviceSpeakerQuery query) {
        return Res.ok(deviceSpeakerAdminService.page(pageParam, query));
    }

    @PermCode(code = "view", nameCn = "云音箱查看", nameEn = "Speaker View")
    @Operation(summary = "根据id查询云音箱设备")
    @GetMapping("/get")
    public Result<DeviceSpeakerResult> findById(@NotNull(message = "{validation.field.id.notNull}") Long id) {
        return Res.ok(deviceSpeakerAdminService.findById(id));
    }

    @PermCode(code = "manage", nameCn = "云音箱管理", nameEn = "Speaker Manage")
    @Operation(summary = "删除云音箱设备")
    @PostMapping("/delete")
    public Result<Void> delete(@NotNull(message = "{validation.field.id.notNull}") Long id) {
        deviceSpeakerAdminService.delete(id);
        return Res.ok();
    }

    @PermCode(code = "manage", nameCn = "云音箱管理", nameEn = "Speaker Manage")
    @Operation(summary = "绑定云音箱设备")
    @PostMapping("/bind")
    public Result<Void> bind(@NotNull(message = "{validation.field.id.notNull}") Long id) {
        deviceSpeakerAdminService.bind(id);
        return Res.ok();
    }

    @PermCode(code = "manage", nameCn = "云音箱管理", nameEn = "Speaker Manage")
    @Operation(summary = "解绑云音箱设备")
    @PostMapping("/unbind")
    public Result<Void> unbind(@NotNull(message = "{validation.field.id.notNull}") Long id) {
        deviceSpeakerAdminService.unbind(id);
        return Res.ok();
    }
}
