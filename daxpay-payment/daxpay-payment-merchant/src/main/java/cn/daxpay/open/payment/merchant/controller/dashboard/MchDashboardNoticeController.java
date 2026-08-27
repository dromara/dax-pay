package cn.daxpay.open.payment.merchant.controller.dashboard;

import cn.daxpay.open.platform.core.annotation.IgnoreAuth;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import cn.daxpay.open.platform.core.rest.result.Result;
import cn.daxpay.open.platform.notify.result.notice.NotifyNoticeResult;
import cn.daxpay.open.platform.notify.service.notice.NotifyNoticeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/// # 工作台平台公告(商户端)
///
/// 面向商户端工作台的公告展示, 服务层强制只返回发布中且在生效时间窗内的公告,
/// 商户无法看到草稿/已下线/已过期内容。
/// 不挂菜单权限码: 工作台为登录即达的页面, 任何已认证用户均可查看(与 [MchDashboardTradeController] 同策略)。
@IgnoreAuth(login = true)
@Validated
@Tag(name = "工作台平台公告(商户端)")
@RestController
@RequestMapping("/mch/dashboard/notice")
@RequiredArgsConstructor
public class MchDashboardNoticeController {

    private final NotifyNoticeService noticeService;

    @Operation(summary = "已发布公告分页")
    @GetMapping("/page")
    public Result<PageResult<NotifyNoticeResult>> page(PageParam pageParam) {
        return Res.ok(noticeService.pagePublished(pageParam));
    }

    @Operation(summary = "已发布公告详情")
    @GetMapping("/get-by-id")
    public Result<NotifyNoticeResult> findById(
        @NotNull(message = "{validation.field.id.notNull}") Long id) {
        return Res.ok(noticeService.findVisibleById(id));
    }
}
