package cn.daxpay.open.payment.merchant.controller.trade;

import cn.daxpay.open.payment.merchant.service.trade.MchNoticeService;
import cn.daxpay.open.payment.trade.notice.param.MchNoticeRecordQuery;
import cn.daxpay.open.payment.trade.notice.param.MchNoticeTaskQuery;
import cn.daxpay.open.payment.trade.notice.result.MchNoticeRecordResult;
import cn.daxpay.open.payment.trade.notice.result.MchNoticeTaskResult;
import cn.daxpay.open.platform.core.annotation.PermCode;
import cn.daxpay.open.platform.core.code.PermCodes;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import cn.daxpay.open.platform.core.rest.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/// # 商户出站通知(商户端)
///
/// 业务编排委托 [MchNoticeService]；强制当前商户过滤。
@PermCode(menuCode = PermCodes.Trade.Notice.MENU)
@Validated
@Tag(name = "商户出站通知(商户端)")
@RestController
@RequestMapping("/mch/mch-notice")
@RequiredArgsConstructor
public class MchNoticeController {

    private final MchNoticeService mchNoticeService;

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "通知任务分页")
    @GetMapping("/task/page")
    public Result<PageResult<MchNoticeTaskResult>> pageTask(PageParam pageParam, MchNoticeTaskQuery query) {
        return Res.ok(mchNoticeService.pageTask(pageParam, query));
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "通知任务详情")
    @GetMapping("/task/get-by-id")
    public Result<MchNoticeTaskResult> getTaskById(
            @NotNull(message = "{validation.field.id.notNull}") Long id) {
        return Res.ok(mchNoticeService.findTaskById(id));
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "手动重发通知")
    @PostMapping("/task/resend")
    public Result<Void> resend(@NotNull(message = "{validation.field.id.notNull}") Long id) {
        mchNoticeService.resend(id);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "发送记录分页")
    @GetMapping("/record/page")
    public Result<PageResult<MchNoticeRecordResult>> pageRecord(PageParam pageParam, MchNoticeRecordQuery query) {
        return Res.ok(mchNoticeService.pageRecord(pageParam, query));
    }
}
