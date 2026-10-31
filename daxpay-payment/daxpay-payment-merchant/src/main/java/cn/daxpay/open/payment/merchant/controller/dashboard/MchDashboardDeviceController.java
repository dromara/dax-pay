package cn.daxpay.open.payment.merchant.controller.dashboard;

import cn.daxpay.open.platform.core.annotation.IgnoreAuth;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.result.Result;
import cn.daxpay.open.payment.merchant.result.dashboard.MchDeviceCountResult;
import cn.daxpay.open.payment.merchant.service.dashboard.MchDashboardDeviceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/// # 工作台设备与应用计数(商户端)
///
/// 应用工作台「通知与设备」卡片数字角标取数端点, 单端点聚合三类计数;
/// 数据强制按当前登录商户隔离(商户号取会话上下文, 禁止参数指定)。
@Tag(name = "工作台设备与应用计数(商户端)")
@IgnoreAuth(login = true)
@Validated
@RestController
@RequestMapping("/mch/dashboard/device")
@RequiredArgsConstructor
public class MchDashboardDeviceController {

    private final MchDashboardDeviceService mchDashboardDeviceService;

    @Operation(summary = "设备与应用计数(虚拟设备/微信应用/抖音应用)")
    @GetMapping("/counts")
    public Result<MchDeviceCountResult> counts() {
        return Res.ok(mchDashboardDeviceService.deviceCounts());
    }
}
