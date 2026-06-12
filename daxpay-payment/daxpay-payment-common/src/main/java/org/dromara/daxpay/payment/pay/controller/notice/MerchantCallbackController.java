package org.dromara.daxpay.payment.pay.controller.notice;

import org.dromara.daxpay.platform.core.rest.Res;
import org.dromara.daxpay.platform.core.rest.param.PageParam;
import org.dromara.daxpay.platform.core.rest.result.PageResult;
import org.dromara.daxpay.platform.core.rest.result.Result;
import org.dromara.daxpay.payment.pay.param.notice.callback.MerchantCallbackTaskQuery;
import org.dromara.daxpay.payment.pay.result.notice.callback.MerchantCallbackRecordResult;
import org.dromara.daxpay.payment.pay.result.notice.callback.MerchantCallbackTaskResult;
import org.dromara.daxpay.payment.pay.service.notice.callback.MerchantCallbackQueryService;
import org.dromara.daxpay.payment.pay.service.notice.callback.MerchantCallbackSendService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/// # 商户回调消息控制器
///
@Validated
@Tag(name = "商户回调通知控制器")
@RestController
@RequestMapping("/merchant/notice/callback")
@RequiredArgsConstructor
public class MerchantCallbackController {

    private final MerchantCallbackQueryService queryService;

    private final MerchantCallbackSendService sendService;

    @Operation(summary = "发送回调消息")
    @PostMapping("/send")
    public Result<Void> send(@NotNull(message = "{validation.field.taskId.notNull}") @Parameter(description = "消息ID") Long taskId) {
        sendService.send(taskId);
        return Res.ok();
    }

    @Operation(summary = "任务分页")
    @GetMapping("/task/page")
    public Result<PageResult<MerchantCallbackTaskResult>> page(PageParam param, MerchantCallbackTaskQuery query) {
        return Res.ok(queryService.page(param,query));
    }

    @Operation(summary = "任务详情")
    @GetMapping("/task/get")
    public Result<MerchantCallbackTaskResult> findById(@NotNull(message = "{validation.field.id.notNull}") Long id) {
        return Res.ok(queryService.findById(id));
    }

    @Operation(summary = "发送记录分页")
    @GetMapping("/record/page")
    public Result<PageResult<MerchantCallbackRecordResult>> pageRecord(PageParam param, @NotNull(message = "{validation.field.id.notNull}") Long taskId) {
        return Res.ok(queryService.pageRecord(param,taskId));
    }

    @Operation(summary = "发送记录详情")
    @GetMapping("/record/get")
    public Result<MerchantCallbackRecordResult> findRecordById(@NotNull(message = "{validation.field.id.notNull}") Long id) {
        return Res.ok(queryService.findRecordById(id));
    }
}
