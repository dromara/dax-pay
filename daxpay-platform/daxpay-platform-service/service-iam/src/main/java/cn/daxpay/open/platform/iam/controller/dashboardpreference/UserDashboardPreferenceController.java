package cn.daxpay.open.platform.iam.controller.dashboardpreference;

import cn.daxpay.open.platform.core.annotation.IgnoreAuth;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.result.Result;
import cn.daxpay.open.platform.iam.param.dashboardpreference.QuickEntrySaveParam;
import cn.daxpay.open.platform.iam.result.dashboardpreference.QuickEntryResult;
import cn.daxpay.open.platform.iam.service.dashboardpreference.UserDashboardPreferenceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/// # 工作台快捷入口偏好
///
/// 用户个性化工作台快捷入口(显隐 + 排序), PC 与移动端按请求终端自动区分.
/// 每个登录用户均可管理自己的偏好, 无需额外权限码.
@IgnoreAuth(login = true)
@Validated
@Tag(name = "工作台快捷入口")
@RestController
@RequestMapping("/iam/dashboard/quick-entry")
@RequiredArgsConstructor
public class UserDashboardPreferenceController {

    private final UserDashboardPreferenceService userDashboardPreferenceService;

    @Operation(summary = "查询当前用户的快捷入口序列")
    @GetMapping
    public Result<QuickEntryResult> find() {
        return Res.ok(userDashboardPreferenceService.findCurrent());
    }

    @Operation(summary = "保存当前用户的快捷入口序列(整体覆盖)")
    @PutMapping
    public Result<Void> save(@RequestBody @Validated QuickEntrySaveParam param) {
        userDashboardPreferenceService.saveCurrent(param.getEntries());
        return Res.ok();
    }

}
