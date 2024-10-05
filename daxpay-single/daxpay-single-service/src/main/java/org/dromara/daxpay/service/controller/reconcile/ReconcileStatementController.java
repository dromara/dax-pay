package org.dromara.daxpay.service.controller.reconcile;

import cn.bootx.platform.core.annotation.RequestGroup;
import cn.bootx.platform.core.annotation.RequestPath;
import cn.bootx.platform.core.rest.Res;
import cn.bootx.platform.core.rest.param.PageParam;
import cn.bootx.platform.core.rest.result.PageResult;
import cn.bootx.platform.core.rest.result.Result;
import cn.bootx.platform.core.util.ValidationUtil;
import org.dromara.daxpay.service.param.reconcile.ReconcileStatementQuery;
import org.dromara.daxpay.service.param.reconcile.ReconcileUploadParam;
import org.dromara.daxpay.service.result.reconcile.ReconcileStatementResult;
import org.dromara.daxpay.service.service.reconcile.ReconcileStatementQueryService;
import org.dromara.daxpay.service.service.reconcile.ReconcileStatementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 对账服务控制器
 * @author xxm
 * @since 2024/8/7
 */
@Validated
@RequestGroup(moduleCode = "reconcile",moduleName = "对账服务", groupCode = "ReconcileStatement", groupName = "对账单")
@Tag(name = "对账服务控制器")
@RestController
@RequestMapping("/reconcile/statement")
@RequiredArgsConstructor
public class ReconcileStatementController {
    private final ReconcileStatementService statementService;
    private final ReconcileStatementQueryService queryService;

    @RequestPath("手动触发对账文件下载")
    @Operation(summary = "手动触发对账文件下载")
    @PostMapping("/downAndSave")
    public Result<Void> downAndSave(@NotNull(message = "对账单ID不能为空") Long id){
        statementService.downAndSave(id);
        return Res.ok();
    }

    @RequestPath("手动上传交易对账单文件")
    @Operation(summary = "手动上传交易对账单文件")
    @PostMapping("/upload")
    public Result<Void> upload(ReconcileUploadParam param, @RequestPart MultipartFile file){
        ValidationUtil.validateParam(param);
        statementService.uploadAndSave(param,file);
        return Res.ok();
    }

    @RequestPath("手动触发交易对账单比对")
    @Operation(summary = "手动触发交易对账单比对")
    @PostMapping("/compare")
    public Result<Void> compare(@NotNull(message = "对账单ID不能为空") Long id){
        statementService.compare(id);
        return Res.ok();
    }

    @RequestPath("对账单分页")
    @Operation(summary = "对账单分页")
    @GetMapping("/page")
    public Result<PageResult<ReconcileStatementResult>> page(PageParam pageParam, ReconcileStatementQuery query){
        return Res.ok(queryService.page(pageParam,query));
    }

    @RequestPath("查询对账单")
    @Operation(summary = "查询对账单")
    @GetMapping("/findById")
    public Result<ReconcileStatementResult> findById(@NotNull(message = "对账单ID不能为空") Long id){
        return Res.ok(queryService.findById(id));
    }
}
