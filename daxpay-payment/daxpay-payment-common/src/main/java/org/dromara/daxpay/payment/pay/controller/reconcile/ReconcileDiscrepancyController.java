package org.dromara.daxpay.payment.pay.controller.reconcile;

import org.dromara.daxpay.platform.core.rest.Res;
import org.dromara.daxpay.platform.core.rest.param.PageParam;
import org.dromara.daxpay.platform.core.rest.result.PageResult;
import org.dromara.daxpay.platform.core.rest.result.Result;
import org.dromara.daxpay.payment.pay.param.reconcile.ReconcileDiscrepancyQuery;
import org.dromara.daxpay.payment.pay.result.reconcile.ReconcileDiscrepancyResult;
import org.dromara.daxpay.payment.pay.service.reconcile.ReconcileDiscrepancyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/// # 对账差异记录
///
@Validated
@Tag(name = "对账差异记录")
@RestController
@RequestMapping("/reconcile/discrepancy")
@RequiredArgsConstructor
public class ReconcileDiscrepancyController {
    private final ReconcileDiscrepancyService reconcileDiscrepancyService;

    @Operation(summary = "对账差异记录分页")
    @GetMapping("/page")
    public Result<PageResult<ReconcileDiscrepancyResult>> page(PageParam pageParam, ReconcileDiscrepancyQuery query){
        return Res.ok(reconcileDiscrepancyService.page(pageParam,query));
    }

    @Operation(summary = "查询对账差异记录")
    @GetMapping("/get")
    public Result<ReconcileDiscrepancyResult> findById(@NotNull(message = "{validation.field.id.notNull}") Long id){
        return Res.ok(reconcileDiscrepancyService.findById(id));
    }

}
