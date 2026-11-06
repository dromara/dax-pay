package cn.daxpay.open.payment.admin.controller.dashboard;

import cn.daxpay.open.platform.core.annotation.IgnoreAuth;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.result.Result;
import cn.daxpay.open.payment.admin.result.dashboard.AdminMchBaseCountResult;
import cn.daxpay.open.payment.admin.service.dashboard.AdminDashboardMerchantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/// # 商户工作台基础计数(运营端)
///
/// 供商户工作台「商户用户/门店/应用/通道商户/域名校验」磁贴数字角标: 商户用户数/
/// 门店数/应用数/通道商户数/域名校验文件数。运营端显式指定商户号(mchNo 必填),
/// 与 /admin/dashboard/device/counts 同构。
///
/// 不挂菜单权限码: 商户工作台为商户列表直达页面, 登录即达;
/// 敏感明细仍由各业务管理页与权限码控制。
@Tag(name = "商户工作台基础计数")
@IgnoreAuth(login = true)
@Validated
@RestController
@RequestMapping("/admin/dashboard/merchant")
@RequiredArgsConstructor
public class AdminDashboardMerchantController {

    private final AdminDashboardMerchantService adminDashboardMerchantService;

    /// 指定商户的五类基础存量计数
    @Operation(summary = "商户基础计数(用户/门店/应用/通道商户/域名校验)")
    @GetMapping("/counts")
    public Result<AdminMchBaseCountResult> counts(
            @RequestParam @NotBlank(message = "{validation.field.mchNo.notBlank}") String mchNo) {
        return Res.ok(adminDashboardMerchantService.counts(mchNo));
    }
}
