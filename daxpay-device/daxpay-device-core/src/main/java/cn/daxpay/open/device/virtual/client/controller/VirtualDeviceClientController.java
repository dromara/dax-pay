package cn.daxpay.open.device.virtual.client.controller;

import cn.daxpay.open.device.virtual.client.param.VirtualDeviceAckParam;
import cn.daxpay.open.device.virtual.client.param.VirtualDeviceActivateParam;
import cn.daxpay.open.device.virtual.client.param.VirtualDevicePrintPreviewParam;
import cn.daxpay.open.device.virtual.client.param.VirtualDeviceReprintParam;
import cn.daxpay.open.device.virtual.client.result.VirtualDeviceAckResult;
import cn.daxpay.open.device.virtual.client.result.VirtualDeviceActivateResult;
import cn.daxpay.open.device.virtual.client.result.VirtualDevicePrintPreviewResult;
import cn.daxpay.open.device.virtual.client.result.VirtualDeviceReprintResult;
import cn.daxpay.open.device.virtual.client.service.VirtualDeviceClientService;
import cn.daxpay.open.platform.core.annotation.IgnoreAuth;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.result.Result;
import cn.hutool.extra.servlet.JakartaServletUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/// # 虚拟音箱设备自助(公开/APP 侧)
///
/// 供 DaxPay 收款播报 APP(纯设备终端, 零业务感知)调用, 无需登录态:
/// 激活凭设备号单因子下发凭证六件套, 回执/心跳验签、nonce 防重放并刷新在线状态(Redis 在线窗口)。
/// 设备归属绑定在运营端台账完成。
///
/// B 波(2026-10-24)已补齐: `/reprint-data`(补打取数)与 `/print-preview`(试打取数),
/// HMAC 验签 + 设备维度限流 + 内置版式渲染, 语义见 [VirtualDeviceClientService] 对应方法注释。
@IgnoreAuth
@Validated
@Tag(name = "虚拟音箱设备")
@RestController
@RequestMapping("/client/device/audio")
@RequiredArgsConstructor
public class VirtualDeviceClientController {

    private final VirtualDeviceClientService audioDeviceClientService;

    @Operation(summary = "激活虚拟音箱(设备号单因子, 返回凭证六件套)")
    @PostMapping("/activate")
    public Result<VirtualDeviceActivateResult> activate(@RequestBody @Validated VirtualDeviceActivateParam param,
            HttpServletRequest request) {
        return Res.ok(audioDeviceClientService.activate(param, JakartaServletUtil.getClientIP(request)));
    }

    @Operation(summary = "回执/心跳(HMAC 验签+nonce 防重放, 刷新在线并返回设备状态)")
    @PostMapping("/ack")
    public Result<VirtualDeviceAckResult> ack(@RequestBody @Validated VirtualDeviceAckParam param) {
        return Res.ok(audioDeviceClientService.ack(param));
    }

    @Operation(summary = "补打取数(HMAC+bizNo, 按支付单号取平台渲染小票字节流)")
    @PostMapping("/reprint-data")
    public Result<VirtualDeviceReprintResult> reprintData(@RequestBody @Validated VirtualDeviceReprintParam param) {
        return Res.ok(audioDeviceClientService.reprint(param));
    }

    @Operation(summary = "试打取数(HMAC+paperWidth, 取当前生效版式渲染的样例小票)")
    @PostMapping("/print-preview")
    public Result<VirtualDevicePrintPreviewResult> printPreview(@RequestBody @Validated VirtualDevicePrintPreviewParam param) {
        return Res.ok(audioDeviceClientService.previewPrint(param));
    }
}
