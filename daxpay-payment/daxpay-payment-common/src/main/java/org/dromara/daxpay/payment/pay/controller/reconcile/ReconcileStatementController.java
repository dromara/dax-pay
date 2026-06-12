package org.dromara.daxpay.payment.pay.controller.reconcile;

import org.dromara.daxpay.platform.core.rest.Res;
import org.dromara.daxpay.platform.core.rest.param.PageParam;
import org.dromara.daxpay.platform.core.rest.result.PageResult;
import org.dromara.daxpay.platform.core.rest.result.Result;
import org.dromara.daxpay.platform.core.util.ValidationUtil;
import org.dromara.daxpay.payment.pay.param.reconcile.ReconcileCreatParam;
import org.dromara.daxpay.payment.pay.param.reconcile.ReconcileStatementQuery;
import org.dromara.daxpay.payment.pay.param.reconcile.ReconcileUploadParam;
import org.dromara.daxpay.payment.pay.result.reconcile.ReconcileStatementResult;
import org.dromara.daxpay.payment.pay.service.reconcile.ReconcileStatementQueryService;
import org.dromara.daxpay.payment.pay.service.reconcile.ReconcileStatementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/// # 对账服务控制器
///
@Validated
@Tag(name = "对账服务控制器")
@RestController
@RequestMapping("/reconcile/statement")
@RequiredArgsConstructor
public class ReconcileStatementController {
    private final ReconcileStatementService statementService;
    private final ReconcileStatementQueryService queryService;

    @Operation(summary = "手动创建对账单")
    @PostMapping("/create")
    public Result<Void> create(@RequestBody @Validated ReconcileCreatParam param){
        statementService.create(param);
        return Res.ok();
    }
    @Operation(summary = "手动触发对账文件下载")
    @PostMapping("/down-and-save")
    public Result<Void> downAndSave(@NotNull(message = "{validation.field.reconcileStatementId.notNull}") Long id){
        statementService.downAndSave(id);
        return Res.ok();
    }

    @Operation(summary = "手动上传交易对账单文件")
    @PostMapping("/upload")
    public Result<Void> upload(ReconcileUploadParam param, @RequestPart MultipartFile file){
        ValidationUtil.validateParam(param);
        statementService.uploadAndSave(param,file);
        return Res.ok();
    }

    @Operation(summary = "手动触发交易对账单比对")
    @PostMapping("/compare")
    public Result<Void> compare(@NotNull(message = "{validation.field.reconcileStatementId.notNull}") Long id){
        statementService.compare(id);
        return Res.ok();
    }

    @Operation(summary = "对账单分页")
    @GetMapping("/page")
    public Result<PageResult<ReconcileStatementResult>> page(PageParam pageParam, ReconcileStatementQuery query){
        return Res.ok(queryService.page(pageParam,query));
    }

    @Operation(summary = "查询对账单")
    @GetMapping("/get")
    public Result<ReconcileStatementResult> findById(@NotNull(message = "{validation.field.reconcileStatementId.notNull}") Long id){
        return Res.ok(queryService.findById(id));
    }
}
