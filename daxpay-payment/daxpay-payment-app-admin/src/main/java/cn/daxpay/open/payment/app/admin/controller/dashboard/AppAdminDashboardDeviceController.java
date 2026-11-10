package cn.daxpay.open.payment.app.admin.controller.dashboard;

import cn.daxpay.open.payment.admin.result.dashboard.AdminMchDeviceCountResult;
import cn.daxpay.open.payment.admin.service.dashboard.AdminDashboardDeviceService;
import cn.daxpay.open.platform.core.annotation.IgnoreAuth;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/// # 商户设备与应用计数(运营移动端)
///
/// 对应 Web 运营端 [AdminDashboardDeviceController], 镜像 counts 端点,
/// 复用同一 [AdminDashboardDeviceService], 仅 URL 前缀不同(/app-admin)。
/// 供商户工作台「收银与设备」「支付配置」磁贴数字角标: 虚拟设备台数/微信应用数/抖音应用数。
/// 显式指定商户号(mchNo 必填)。
///
/// 不挂菜单权限码: 商户工作台为商户列表直达页面, 登录即达;
/// 敏感明细仍由各业务管理页与权限码控制。
@Tag(name = "小程序管理端-商户设备与应用计数")
@IgnoreAuth(login = true)
@Validated
@RestController
@RequestMapping("/app-admin/dashboard/device")
@RequiredArgsConstructor
public class AppAdminDashboardDeviceController {

    private final AdminDashboardDeviceService adminDashboardDeviceService;

    /// 指定商户的设备与支付应用存量计数
    @Operation(summary = "商户设备与应用计数(虚拟设备/微信应用/抖音应用)")
    @GetMapping("/counts")
    public Result<AdminMchDeviceCountResult> counts(
            @RequestParam @NotBlank(message = "{validation.field.mchNo.notBlank}") String mchNo) {
        return Res.ok(adminDashboardDeviceService.counts(mchNo));
    }
}
