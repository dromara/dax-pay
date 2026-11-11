package cn.daxpay.open.payment.app.merchant.controller.dashboard;

import cn.daxpay.open.payment.merchant.result.dashboard.MchDeviceCountResult;
import cn.daxpay.open.payment.merchant.service.dashboard.MchDashboardDeviceService;
import cn.daxpay.open.platform.core.annotation.IgnoreAuth;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/// # 工作台设备与应用计数(商户移动端)
///
/// 对应 Web 商户端 [cn.daxpay.open.payment.merchant.controller.dashboard.MchDashboardDeviceController]
/// (路径 `/mch/dashboard/device`), 镜像 counts 端点, 复用同一 [MchDashboardDeviceService],
/// 仅 URL 前缀不同(`/app-mch`)。
/// 供商户工作台「通知与设备」卡片数字角标: 虚拟设备台数/微信应用数/抖音应用数,
/// 数据强制按当前登录商户隔离(商户号取会话上下文, 无入参)。
///
/// 不挂菜单权限码: 工作台为登录即达的页面, 任何已认证商户用户均可查看。
@Tag(name = "工作台设备与应用计数(商户移动端)")
@IgnoreAuth(login = true)
@Validated
@RestController
@RequestMapping("/app-mch/dashboard/device")
@RequiredArgsConstructor
public class AppMerchantDashboardDeviceController {

    private final MchDashboardDeviceService mchDashboardDeviceService;

    @Operation(summary = "设备与应用计数(虚拟设备/微信应用/抖音应用)")
    @GetMapping("/counts")
    public Result<MchDeviceCountResult> counts() {
        return Res.ok(mchDashboardDeviceService.deviceCounts());
    }
}
