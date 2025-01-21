package org.dromara.daxpay.service.controller.notice;

import cn.bootx.platform.core.annotation.RequestGroup;
import cn.bootx.platform.core.annotation.RequestPath;
import cn.bootx.platform.core.rest.Res;
import cn.bootx.platform.core.rest.param.PageParam;
import cn.bootx.platform.core.rest.result.PageResult;
import cn.bootx.platform.core.rest.result.Result;
import org.dromara.daxpay.service.param.notice.notify.MerchantNotifyTaskQuery;
import org.dromara.daxpay.service.result.notice.notify.MerchantNotifyRecordResult;
import org.dromara.daxpay.service.result.notice.notify.MerchantNotifyTaskResult;
import org.dromara.daxpay.service.service.notice.notify.MerchantNotifyQueryService;
import org.dromara.daxpay.service.service.notice.notify.MerchantNotifySendService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 商户订阅通知消息控制器
 * @author xxm
 * @since 2024/8/5
 */
@RequestGroup(groupCode = "MerchantNotify", groupName = "商户订阅通知", moduleCode = "MerchantNotice")
@Tag(name = "商户订阅通知控制器")
@RestController
@RequestMapping("/merchant/notice/notify")
@RequiredArgsConstructor
public class MerchantNotifyController {
    private final MerchantNotifyQueryService queryService;

    private final MerchantNotifySendService sendService;

    @RequestPath("发送订阅消息")
    @Operation(summary = "发送订阅消息")
    @PostMapping("/send")
    public Result<Void> send(Long taskId) {
        sendService.send(taskId);
        return Res.ok();
    }

    @RequestPath("任务分页")
    @Operation(summary = "任务分页")
    @GetMapping("/task/page")
    public Result<PageResult<MerchantNotifyTaskResult>> page(PageParam param, MerchantNotifyTaskQuery query) {
        return Res.ok(queryService.page(param,query));
    }

    @RequestPath("任务详情")
    @Operation(summary = "任务详情")
    @GetMapping("/task/findById")
    public Result<MerchantNotifyTaskResult> findById(Long id) {
        return Res.ok(queryService.findById(id));
    }

    @RequestPath("发送记录分页")
    @Operation(summary = "发送记录分页")
    @GetMapping("/record/page")
    public Result<PageResult<MerchantNotifyRecordResult>> pageRecord(PageParam param, Long taskId) {
        return Res.ok(queryService.pageRecord(param,taskId));
    }

    @RequestPath("发送记录详情")
    @Operation(summary = "发送记录详情")
    @GetMapping("/record/findById")
    public Result<MerchantNotifyRecordResult> findRecordById(Long id) {
        return Res.ok(queryService.findRecordById(id));
    }

}
