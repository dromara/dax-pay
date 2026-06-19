package cn.daxpay.open.payment.old.pay.controller.record;

import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import cn.daxpay.open.platform.core.rest.result.Result;
import cn.daxpay.open.payment.old.pay.param.record.TradeSyncRecordQuery;
import cn.daxpay.open.payment.old.pay.result.record.sync.TradeSyncRecordResult;
import cn.daxpay.open.payment.old.pay.service.record.sync.TradeSyncRecordService;
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
