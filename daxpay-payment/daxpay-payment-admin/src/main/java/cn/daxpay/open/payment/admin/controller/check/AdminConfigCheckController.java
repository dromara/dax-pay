package cn.daxpay.open.payment.admin.controller.check;

import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.result.Result;
import cn.daxpay.open.payment.admin.check.service.AdminConfigCheckService;
import cn.daxpay.open.payment.check.model.ConfigCheckResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/// # 配置检查(运营端)
///
/// 工作台「配置待完成」Widget 数据源。
/// 不挂菜单权限码: 工作台为登录即达的页面, 任何已认证运营用户均可查看平台级配置完成度。
@Tag(name = "配置检查(运营端)")
@Validated
@RestController
@RequestMapping("/admin/config-check")
@RequiredArgsConstructor
public class AdminConfigCheckController {

    private final AdminConfigCheckService adminConfigCheckService;

    /// 获取平台级未完成配置项列表(含分类汇总)
    @Operation(summary = "获取平台级未完成配置项")
    @GetMapping("/items")
    public Result<ConfigCheckResult> items() {
        return Res.ok(adminConfigCheckService.check());
    }
}
