package cn.daxpay.open.device.app.merchant.controller;

import cn.daxpay.open.device.merchant.param.audio.MchAudioDeviceBindAppBatchParam;
import cn.daxpay.open.device.merchant.param.audio.MchAudioDeviceBindAppParam;
import cn.daxpay.open.device.merchant.param.audio.MchAudioDeviceBindStoreBatchParam;
import cn.daxpay.open.device.merchant.param.audio.MchAudioDeviceBindStoreParam;
import cn.daxpay.open.device.merchant.param.audio.MchAudioDeviceUpdateParam;
import cn.daxpay.open.device.virtual.param.VirtualDeviceQuery;
import cn.daxpay.open.device.virtual.param.VirtualDeviceTestParam;
import cn.daxpay.open.device.virtual.result.VirtualDeviceResult;
import cn.daxpay.open.device.virtual.service.MchVirtualDeviceService;
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

/// # 收款设备管理(商户小程序端镜像)
///
/// 对应 Web 商户端 [MchVirtualDeviceController], 镜像除导出(export)外的全部端点,
/// 复用同一 [MchVirtualDeviceService] 与菜单/权限码(device:virtual), 仅 URL 前缀不同(/app-mch)。
///
/// 存在意义: 商户小程序此前直接调用 Web 前缀 `/mch/device/virtual`, 是两端唯一未收编的跨命名空间调用;
/// 收编后 App 端点与 Web 契约解耦, 移动端可独立裁剪字段与参数, 且自动受 `/app-mch/**` 身份域守卫保护。
///
/// 不做导出: 小程序无文件落盘诉求(AGENTS 口径「小程序不做导出」), 导出保留在 Web 商户端。
/// 不做设备生成/归属变更/删除: 设备资产由运营端批量发放绑定, 商户只消费(与码牌「运营制码 + 商户认领」同链路)。
@PermCode(menuCode = PermCodes.Device.Virtual.MENU)
@Validated
@Tag(name = "小程序商户端-收款设备管理")
@RestController
@RequestMapping("/app-mch/device/virtual")
@RequiredArgsConstructor
public class AppMerchantVirtualDeviceController {

    private final MchVirtualDeviceService mchVirtualDeviceService;

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "收款设备分页(强制当前商户)")
    @GetMapping("/page")
    public Result<PageResult<VirtualDeviceResult>> page(PageParam pageParam, VirtualDeviceQuery query) {
        return Res.ok(mchVirtualDeviceService.page(pageParam, query));
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "收款设备详情")
    @GetMapping("/get")
    public Result<VirtualDeviceResult> findById(@NotNull(message = "{validation.field.id.notNull}") Long id) {
        return Res.ok(mchVirtualDeviceService.findById(id));
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "修改设备名称")
    @PostMapping("/update")
    public Result<Void> update(@RequestBody @Validated MchAudioDeviceUpdateParam param) {
        mchVirtualDeviceService.update(param);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "调整使用应用(空=全部应用)")
    @PostMapping("/bind-app")
    public Result<Void> bindApp(@RequestBody @Validated MchAudioDeviceBindAppParam param) {
        mchVirtualDeviceService.bindApp(param);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "批量调整使用应用(空=全部应用)")
    @PostMapping("/bind-app-batch")
    public Result<Void> bindAppBatch(@RequestBody @Validated MchAudioDeviceBindAppBatchParam param) {
        mchVirtualDeviceService.bindAppBatch(param);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "调整使用门店")
    @PostMapping("/bind-store")
    public Result<Void> bindStore(@RequestBody @Validated MchAudioDeviceBindStoreParam param) {
        mchVirtualDeviceService.bindStore(param);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "批量调整使用门店(空=解绑)")
    @PostMapping("/bind-store-batch")
    public Result<Void> bindStoreBatch(@RequestBody @Validated MchAudioDeviceBindStoreBatchParam param) {
        mchVirtualDeviceService.bindStoreBatch(param);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "解绑门店(置为未分配)")
    @PostMapping("/unbind-store")
    public Result<Void> unbindStore(@NotNull(message = "{validation.field.id.notNull}") Long id) {
        mchVirtualDeviceService.unbindStore(id);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "启用/停用设备")
    @PostMapping("/change-enable")
    public Result<Void> changeEnable(@NotNull(message = "{validation.field.id.notNull}") Long id,
                                     @NotNull(message = "{validation.field.enabled.notNull}") Boolean enable) {
        mchVirtualDeviceService.changeEnable(id, enable);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "试播(可指定金额与收款方式, 返回 msgId 供回执比对)")
    @PostMapping("/test-broadcast")
    public Result<String> testBroadcast(@RequestBody @Validated VirtualDeviceTestParam param) {
        return Res.ok(mchVirtualDeviceService.testBroadcast(param));
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "试播文案预览(返回预计播报文本)")
    @GetMapping("/test-speech")
    public Result<String> testSpeech(Long amountFen, String channel) {
        return Res.ok(mchVirtualDeviceService.testSpeech(amountFen, channel));
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "试打(内置版式渲染样例小票, 返回 msgId 供回执比对)")
    @PostMapping("/test-print")
    public Result<String> testPrint(@RequestBody @Validated VirtualDeviceTestParam param) {
        return Res.ok(mchVirtualDeviceService.testPrint(param));
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "激活二维码内容字符串(归属校验, 前端渲染二维码图)")
    @GetMapping("/qr-content")
    public Result<String> qrContent(@NotBlank(message = "{validation.field.deviceNo.notBlank}") String deviceNo) {
        return Res.ok(mchVirtualDeviceService.qrContent(deviceNo));
    }
}
