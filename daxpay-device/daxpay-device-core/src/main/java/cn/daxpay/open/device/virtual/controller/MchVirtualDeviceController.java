package cn.daxpay.open.device.virtual.controller;

import cn.daxpay.open.device.merchant.param.audio.MchAudioDeviceBindAppBatchParam;
import cn.daxpay.open.device.merchant.param.audio.MchAudioDeviceBindAppParam;
import cn.daxpay.open.device.merchant.param.audio.MchAudioDeviceBindStoreBatchParam;
import cn.daxpay.open.device.merchant.param.audio.MchAudioDeviceBindStoreParam;
import cn.daxpay.open.device.merchant.param.audio.MchAudioDeviceUpdateParam;
import cn.daxpay.open.device.virtual.param.VirtualDeviceQuery;
import cn.daxpay.open.device.virtual.param.VirtualDeviceTestParam;
import cn.daxpay.open.device.virtual.result.VirtualDeviceResult;
import cn.daxpay.open.device.virtual.service.MchVirtualDeviceService;
import cn.daxpay.open.platform.core.annotation.OperateLog;
import cn.daxpay.open.platform.core.annotation.PermCode;
import cn.daxpay.open.platform.core.code.PermCodes;
import cn.daxpay.open.platform.core.enums.common.OperateLogType;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import cn.daxpay.open.platform.core.rest.result.Result;
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
import org.springframework.web.bind.annotation.RestController;

/// # 虚拟设备管理(商户端)
///
/// 只开放商户自助能力: 查看/导出/改名/应用门店调整(支持批量)/启停/试播试打/激活二维码内容。
/// 设备录入、商户归属变更、密钥重置属运营端能力; 商户号由 Service 从 PaymentContext 强制校验。
/// 菜单码与权限码与运营端统一为 device:virtual, 两端仅端点前缀区分;
/// 设备 APP 侧免登录协议仍为 `/client/device/audio`, 消息主题仍为 `daxpay/device/audio/{deviceNo}`, 不随菜单改名。
/// 端点保留旧前缀 `/mch/device/audio` 作过渡兼容(存量小程序/浏览器书签), 待存量客户端全部升级后再移除。
@PermCode(menuCode = PermCodes.Device.Virtual.MENU)
@Validated
@Tag(name = "虚拟设备管理(商户端)")
@RestController
@RequestMapping({"/mch/device/virtual", "/mch/device/audio"})
@RequiredArgsConstructor
public class MchVirtualDeviceController {

    private final MchVirtualDeviceService mchAudioDeviceService;

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "虚拟设备分页(强制当前商户)")
    @GetMapping("/page")
    public Result<PageResult<VirtualDeviceResult>> page(PageParam pageParam, VirtualDeviceQuery query) {
        return Res.ok(mchAudioDeviceService.page(pageParam, query));
    }

    @PermCode(code = PermCodes.Action.EXPORT)
    @Operation(summary = "导出虚拟设备")
    @OperateLog(title = "导出虚拟设备", businessType = OperateLogType.EXPORT, saveParam = false)
    @PostMapping("/export")
    public void export(HttpServletResponse response, VirtualDeviceQuery query) {
        mchAudioDeviceService.export(query, response);
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "虚拟设备详情")
    @GetMapping("/get")
    public Result<VirtualDeviceResult> findById(@NotNull(message = "{validation.field.id.notNull}") Long id) {
        return Res.ok(mchAudioDeviceService.findById(id));
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "修改设备名称")
    @PostMapping("/update")
    public Result<Void> update(@RequestBody @Validated MchAudioDeviceUpdateParam param) {
        mchAudioDeviceService.update(param);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "调整使用应用(空=全部应用)")
    @PostMapping("/bind-app")
    public Result<Void> bindApp(@RequestBody @Validated MchAudioDeviceBindAppParam param) {
        mchAudioDeviceService.bindApp(param);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "批量调整使用应用(空=全部应用)")
    @PostMapping("/bind-app-batch")
    public Result<Void> bindAppBatch(@RequestBody @Validated MchAudioDeviceBindAppBatchParam param) {
        mchAudioDeviceService.bindAppBatch(param);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "调整使用门店")
    @PostMapping("/bind-store")
    public Result<Void> bindStore(@RequestBody @Validated MchAudioDeviceBindStoreParam param) {
        mchAudioDeviceService.bindStore(param);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "批量调整使用门店(空=解绑)")
    @PostMapping("/bind-store-batch")
    public Result<Void> bindStoreBatch(@RequestBody @Validated MchAudioDeviceBindStoreBatchParam param) {
        mchAudioDeviceService.bindStoreBatch(param);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "解绑门店(置为未分配)")
    @PostMapping("/unbind-store")
    public Result<Void> unbindStore(@NotNull(message = "{validation.field.id.notNull}") Long id) {
        mchAudioDeviceService.unbindStore(id);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "启用/停用设备")
    @PostMapping("/change-enable")
    public Result<Void> changeEnable(@NotNull(message = "{validation.field.id.notNull}") Long id,
                                     @NotNull(message = "{validation.field.enabled.notNull}") Boolean enable) {
        mchAudioDeviceService.changeEnable(id, enable);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "试播(可指定金额与收款方式, 返回 msgId 供回执比对)")
    @PostMapping("/test-broadcast")
    public Result<String> testBroadcast(@RequestBody @Validated VirtualDeviceTestParam param) {
        return Res.ok(mchAudioDeviceService.testBroadcast(param));
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "试播文案预览(返回预计播报文本)")
    @GetMapping("/test-speech")
    public Result<String> testSpeech(Long amountFen, String channel) {
        return Res.ok(mchAudioDeviceService.testSpeech(amountFen, channel));
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "试打(内置版式渲染样例小票, 返回 msgId 供回执比对)")
    @PostMapping("/test-print")
    public Result<String> testPrint(@RequestBody @Validated VirtualDeviceTestParam param) {
        return Res.ok(mchAudioDeviceService.testPrint(param));
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "激活二维码内容字符串(归属校验, 前端渲染二维码图)")
    @GetMapping("/qr-content")
    public Result<String> qrContent(@NotBlank(message = "{validation.field.deviceNo.notBlank}") String deviceNo) {
        return Res.ok(mchAudioDeviceService.qrContent(deviceNo));
    }
}
