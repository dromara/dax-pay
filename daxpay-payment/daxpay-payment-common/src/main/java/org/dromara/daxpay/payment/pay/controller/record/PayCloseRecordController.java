package org.dromara.daxpay.payment.pay.controller.record;

import org.dromara.daxpay.platform.core.rest.Res;
import org.dromara.daxpay.platform.core.rest.param.PageParam;
import org.dromara.daxpay.platform.core.rest.result.PageResult;
import org.dromara.daxpay.platform.core.rest.result.Result;
import org.dromara.daxpay.payment.pay.param.record.PayCloseRecordQuery;
import org.dromara.daxpay.payment.pay.result.record.close.PayCloseRecordResult;
import org.dromara.daxpay.payment.pay.service.record.close.PayCloseRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/// # 支付订单关闭记录
///
@Validated
@Tag(name = "支付订单关闭记录")
@RestController
@RequestMapping("/record/close")
@RequiredArgsConstructor
public class PayCloseRecordController {
    private final PayCloseRecordService service;

    @Operation(summary = "分页查询")
    @GetMapping("/page")
    public Result<PageResult<PayCloseRecordResult>> page(PageParam pageParam, PayCloseRecordQuery query){
        return Res.ok(service.page(pageParam, query));
    }

    @Operation(summary = "查询单条")
    @GetMapping("/get")
    public Result<PayCloseRecordResult> findById(@NotNull(message = "{validation.field.id.notNull}") Long id){
        return Res.ok(service.findById(id));
    }

}
