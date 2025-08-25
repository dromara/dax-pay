package org.dromara.daxpay.controller.notice;

import cn.bootx.platform.core.annotation.ClientCode;
import cn.bootx.platform.core.annotation.RequestGroup;
import cn.bootx.platform.core.annotation.RequestPath;
import cn.bootx.platform.core.rest.Res;
import cn.bootx.platform.core.rest.param.PageParam;
import cn.bootx.platform.core.rest.result.PageResult;
import cn.bootx.platform.core.rest.result.Result;
import org.dromara.daxpay.service.common.code.DaxPayCode;
import org.dromara.daxpay.service.pay.param.notice.callback.MerchantCallbackTaskQuery;
import org.dromara.daxpay.service.pay.result.notice.callback.MerchantCallbackRecordResult;
import org.dromara.daxpay.service.pay.result.notice.callback.MerchantCallbackTaskResult;
import org.dromara.daxpay.service.pay.service.notice.callback.MerchantCallbackQueryService;
import org.dromara.daxpay.service.pay.service.notice.callback.MerchantCallbackSendService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.dromara.core.trans.anno.TransMethodResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 商户回调消息控制器
 * @author xxm
 * @since 2024/8/5
 */
@Validated
@ClientCode({DaxPayCode.Client.ADMIN, DaxPayCode.Client.MERCHANT})
@RequestGroup(groupCode = "MerchantCallback", groupName = "商户回调通知", moduleCode = "MerchantNotice", moduleName = "(DaxPay)商户消息通知")
@Tag(name = "商户回调通知控制器")
@RestController
@RequestMapping("/merchant/notice/callback")
@RequiredArgsConstructor
public class MerchantCallbackController {

    private final MerchantCallbackQueryService queryService;

    private final MerchantCallbackSendService sendService;

    @RequestPath("发送回调消息")
    @Operation(summary = "发送回调消息")
    @PostMapping("/send")
    public Result<Void> send(@NotNull(message = "消息ID不可为空") @Parameter(description = "消息ID") Long taskId) {
        sendService.send(taskId);
        return Res.ok();
    }

    @TransMethodResult
    @RequestPath("任务分页")
    @Operation(summary = "任务分页")
    @GetMapping("/task/page")
    public Result<PageResult<MerchantCallbackTaskResult>> page(PageParam param, MerchantCallbackTaskQuery query) {
        return Res.ok(queryService.page(param,query));
    }

    @TransMethodResult
    @RequestPath("任务详情")
    @Operation(summary = "任务详情")
    @GetMapping("/task/findById")
    public Result<MerchantCallbackTaskResult> findById(@NotNull(message = "主键不可为空") Long id) {
        return Res.ok(queryService.findById(id));
    }

    @RequestPath("发送记录分页")
    @Operation(summary = "发送记录分页")
    @GetMapping("/record/page")
    public Result<PageResult<MerchantCallbackRecordResult>> pageRecord(PageParam param, @NotNull(message = "主键不可为空") Long taskId) {
        return Res.ok(queryService.pageRecord(param,taskId));
    }

    @RequestPath("发送记录详情")
    @Operation(summary = "发送记录详情")
    @GetMapping("/record/findById")
    public Result<MerchantCallbackRecordResult> findRecordById(@NotNull(message = "主键不可为空") Long id) {
        return Res.ok(queryService.findRecordById(id));
    }
}
