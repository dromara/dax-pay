package org.dromara.daxpay.payment.pay.controller.record;

import org.dromara.daxpay.platform.core.rest.Res;
import org.dromara.daxpay.platform.core.rest.param.PageParam;
import org.dromara.daxpay.platform.core.rest.result.PageResult;
import org.dromara.daxpay.platform.core.rest.result.Result;
import org.dromara.daxpay.payment.pay.param.record.TradeSyncRecordQuery;
import org.dromara.daxpay.payment.pay.result.record.sync.TradeSyncRecordResult;
import org.dromara.daxpay.payment.pay.service.record.sync.TradeSyncRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/// # 交易同步记录
///
@Validated
@Tag(name = "交易同步记录")
@RestController
@RequestMapping("/record/sync")
@RequiredArgsConstructor
public class TradeSyncRecordController {
    private final TradeSyncRecordService service;

    @Operation(summary = "分页查询")
    @GetMapping("/page")
    public Result<PageResult<TradeSyncRecordResult>> page(PageParam pageParam, TradeSyncRecordQuery query) {
        return Res.ok(service.page(pageParam, query));
    }

    @Operation(summary = "查询单条")
    @GetMapping("/get")
    public Result<TradeSyncRecordResult> findById(@NotNull(message = "{validation.field.id.notNull}") Long id) {
        return Res.ok(service.findById(id));
    }
}
