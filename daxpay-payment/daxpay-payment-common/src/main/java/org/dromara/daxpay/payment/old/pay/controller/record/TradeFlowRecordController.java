package org.dromara.daxpay.payment.old.pay.controller.record;

import org.dromara.daxpay.platform.core.rest.Res;
import org.dromara.daxpay.platform.core.rest.param.PageParam;
import org.dromara.daxpay.platform.core.rest.result.PageResult;
import org.dromara.daxpay.platform.core.rest.result.Result;
import org.dromara.daxpay.payment.old.pay.param.record.TradeFlowRecordQuery;
import org.dromara.daxpay.payment.old.pay.result.record.flow.TradeFlowAmountResult;
import org.dromara.daxpay.payment.old.pay.result.record.flow.TradeFlowRecordResult;
import org.dromara.daxpay.payment.old.pay.service.record.flow.TradeFlowRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/// # 交易流水记录控制器
///
@Validated
@Tag(name = "交易流水记录控制器")
@RestController
@RequestMapping("/record/flow")
@RequiredArgsConstructor
public class TradeFlowRecordController {
    private final TradeFlowRecordService service;

    @Operation(summary = "分页查询")
    @GetMapping("/page")
    public Result<PageResult<TradeFlowRecordResult>> page(PageParam pageParam, TradeFlowRecordQuery query) {
        return Res.ok(service.page(pageParam, query));
    }

    @Operation(summary = "查询单条")
    @GetMapping("/get")
    public Result<TradeFlowRecordResult> findById(@NotNull(message = "{validation.field.id.notNull}") Long id) {
        return Res.ok(service.findById(id));
    }

    @Operation(summary = "查询各类金额汇总")
    @GetMapping("/summary")
    public Result<TradeFlowAmountResult> summary(TradeFlowRecordQuery query) {
        return Res.ok(service.summary(query));
    }
}
