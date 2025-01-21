package org.dromara.daxpay.service.controller.reconcile;

import cn.bootx.platform.core.annotation.RequestGroup;
import cn.bootx.platform.core.annotation.RequestPath;
import cn.bootx.platform.core.rest.Res;
import cn.bootx.platform.core.rest.param.PageParam;
import cn.bootx.platform.core.rest.result.PageResult;
import cn.bootx.platform.core.rest.result.Result;
import org.dromara.daxpay.service.param.reconcile.ReconcileDiscrepancyQuery;
import org.dromara.daxpay.service.result.reconcile.ReconcileDiscrepancyResult;
import org.dromara.daxpay.service.service.reconcile.ReconcileDiscrepancyService;
import com.fhs.core.trans.anno.TransMethodResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 对账差异记录
 * @author xxm
 * @since 2024/8/7
 */
@RequestGroup(moduleCode = "reconcile",moduleName = "对账服务", groupCode = "ReconcileDiscrepancy", groupName = "对账差异记录")
@Tag(name = "对账差异记录")
@RestController
@RequestMapping("/reconcile/discrepancy")
@RequiredArgsConstructor
public class ReconcileDiscrepancyController {
    private final ReconcileDiscrepancyService reconcileDiscrepancyService;

    @TransMethodResult
    @RequestPath("对账差异记录分页")
    @Operation(summary = "对账差异记录分页")
    @GetMapping("/page")
    public Result<PageResult<ReconcileDiscrepancyResult>> page(PageParam pageParam, ReconcileDiscrepancyQuery query){
        return Res.ok(reconcileDiscrepancyService.page(pageParam,query));
    }

    @TransMethodResult
    @RequestPath("查询对账差异记录")
    @Operation(summary = "查询对账差异记录")
    @GetMapping("/findById")
    public Result<ReconcileDiscrepancyResult> findById(Long id){
        return Res.ok(reconcileDiscrepancyService.findById(id));
    }

}
