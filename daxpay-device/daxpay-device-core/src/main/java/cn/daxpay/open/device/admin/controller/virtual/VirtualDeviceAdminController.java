package cn.daxpay.open.device.admin.controller.virtual;

import cn.daxpay.open.device.admin.param.common.DeviceBindMerchantParam;
import cn.daxpay.open.device.admin.param.common.DeviceBindStoreBatchParam;
import cn.daxpay.open.device.admin.param.virtual.VirtualDeviceGenerateParam;
import cn.daxpay.open.device.admin.param.virtual.VirtualDeviceParam;
import cn.daxpay.open.device.admin.service.virtual.VirtualDeviceAdminService;
import cn.daxpay.open.device.virtual.param.VirtualDeviceQuery;
import cn.daxpay.open.device.virtual.param.VirtualDeviceTestParam;
import cn.daxpay.open.device.virtual.result.VirtualDeviceResult;
import cn.daxpay.open.platform.core.annotation.OperateLog;
import cn.daxpay.open.platform.core.annotation.PermCode;
import cn.daxpay.open.platform.core.code.PermCodes;
import cn.daxpay.open.platform.core.enums.common.OperateLogType;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import cn.daxpay.open.platform.core.rest.result.Result;
import cn.daxpay.open.platform.core.validation.ValidationGroup;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/// # 虚拟设备管理(运营端)
///
/// 虚拟设备独立台账的管理入口: 生成/编辑/启停/删除/归属绑定/试播试打/激活二维码内容。
/// 开源版裁剪(对照 plus): 无分拨代理商/收回、无打印模板下拉、无监控大屏、无激活日志。
@PermCode(menuCode = PermCodes.Device.Virtual.MENU)
@Validated
@Tag(name = "虚拟设备管理")
@RestController
@RequestMapping("/admin/device/virtual")
@RequiredArgsConstructor
public class VirtualDeviceAdminController {

    private final VirtualDeviceAdminService virtualDeviceAdminService;

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "虚拟设备分页")
    @GetMapping("/page")
    public Result<PageResult<VirtualDeviceResult>> page(PageParam pageParam, VirtualDeviceQuery query) {
        return Res.ok(virtualDeviceAdminService.page(pageParam, query));
    }

    @PermCode(code = PermCodes.Action.EXPORT)
    @Operation(summary = "导出虚拟设备")
    @OperateLog(title = "导出虚拟设备", businessType = OperateLogType.EXPORT, saveParam = false)
    @PostMapping("/export")
    public void export(HttpServletResponse response, VirtualDeviceQuery query,
                       @RequestParam(required = false) String ids) {
        virtualDeviceAdminService.export(query, ids, response);
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "生成虚拟设备(产号+入库原子完成, 返回设备号清单)")
    @PostMapping("/generate")
    public Result<List<String>> generate(@RequestBody @Validated VirtualDeviceGenerateParam param) {
        return Res.ok(virtualDeviceAdminService.generate(param));
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "修改虚拟设备(名称/启停)")
    @PostMapping("/update")
    public Result<Void> update(@RequestBody @Validated(ValidationGroup.edit.class) VirtualDeviceParam param) {
        virtualDeviceAdminService.update(param);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "启用/停用虚拟设备")
    @PostMapping("/change-enable")
    public Result<Void> changeEnable(@NotNull(message = "{validation.field.id.notNull}") Long id,
                                     @NotNull(message = "{validation.field.enabled.notNull}") Boolean enable) {
        virtualDeviceAdminService.changeEnable(id, enable);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "删除虚拟设备(未激活未绑定才可删)")
    @PostMapping("/delete")
    public Result<Void> delete(@NotNull(message = "{validation.field.id.notNull}") Long id) {
        virtualDeviceAdminService.delete(id);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "绑定商户(可一并选应用与门店)")
    @PostMapping("/bind-merchant")
    public Result<Void> bindMerchant(@RequestBody @Validated DeviceBindMerchantParam param) {
        virtualDeviceAdminService.bindMerchant(param);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "解绑商户(回平台库存, 旧 APP 凭证即时失效)")
    @PostMapping("/unbind-merchant")
    public Result<Void> unbindMerchant(@RequestBody List<Long> ids) {
        virtualDeviceAdminService.unbindMerchant(ids);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "批量绑定门店(门店工作台「绑定收银设备到本店」)")
    @PostMapping("/bind-store-batch")
    public Result<Void> bindStoreBatch(@RequestBody @Validated DeviceBindStoreBatchParam param) {
        virtualDeviceAdminService.bindStoreBatch(param);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "试播(可指定金额与收款方式, 返回 msgId 供回执比对)")
    @PostMapping("/test")
    public Result<String> test(@RequestBody @Validated VirtualDeviceTestParam param) {
        return Res.ok(virtualDeviceAdminService.test(param));
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "试播文案预览(平台镜像终端词元规则, 返回预计播报文本)")
    @GetMapping("/test-speech")
    public Result<String> testSpeech(Long amountFen, String channel) {
        return Res.ok(virtualDeviceAdminService.testSpeech(amountFen, channel));
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "试打(内置版式渲染样例小票, 可指定版式代码, 返回 msgId 供回执比对)")
    @PostMapping("/test-print")
    public Result<String> testPrint(@RequestBody @Validated VirtualDeviceTestParam param) {
        return Res.ok(virtualDeviceAdminService.testPrint(param));
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "激活二维码内容字符串(前端渲染二维码图)")
    @GetMapping("/qr-content")
    public Result<String> qrContent(@NotBlank(message = "{validation.field.deviceNo.notBlank}") String deviceNo) {
        return Res.ok(virtualDeviceAdminService.qrContent(deviceNo));
    }
}
