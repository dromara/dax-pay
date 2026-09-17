package cn.daxpay.open.payment.admin.controller.trade;

import cn.daxpay.open.payment.admin.service.trade.RefundOrderAdminService;
import cn.daxpay.open.payment.trade.order.convert.export.RefundOrderExportConvert;
import cn.daxpay.open.payment.trade.order.param.RefundOrderQuery;
import cn.daxpay.open.payment.trade.order.result.RefundOrderResult;
import cn.daxpay.open.payment.trade.order.result.export.RefundOrderExportResult;
import cn.daxpay.open.payment.trade.runtime.param.RefundParam;
import cn.daxpay.open.platform.common.excel.ExcelUtils;
import cn.daxpay.open.platform.common.excel.ExcelWriterWrapper;
import cn.daxpay.open.platform.core.annotation.OperateLog;
import cn.daxpay.open.platform.core.annotation.PermCode;
import cn.daxpay.open.platform.core.code.PermCodes;
import cn.daxpay.open.platform.core.enums.common.OperateLogType;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import cn.daxpay.open.platform.core.rest.result.Result;
import org.apache.fesod.sheet.write.metadata.WriteSheet;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/// # 退款订单(管理)
///
/// 面向运营后台的退款订单管理。业务编排委托 [RefundOrderAdminService]。
@PermCode(menuCode = PermCodes.Trade.Refund.MENU)
@Validated
@Tag(name = "退款订单(管理)")
@RestController
@RequestMapping("/admin/order/refund")
@RequiredArgsConstructor
public class RefundOrderAdminController {

    private final RefundOrderAdminService refundOrderAdminService;

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "退款订单分页")
    @GetMapping("/page")
    public Result<PageResult<RefundOrderResult>> page(PageParam pageParam, RefundOrderQuery query) {
        return Res.ok(refundOrderAdminService.page(pageParam, query));
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "根据ID查询退款订单详情")
    @GetMapping("/get-by-id")
    public Result<RefundOrderResult> findById(
            @NotNull(message = "{validation.field.id.notNull}") Long id) {
        return Res.ok(refundOrderAdminService.findById(id));
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "发起退款")
    @PostMapping("/refund")
    public Result<RefundOrderResult> refund(@Valid @RequestBody RefundParam param) {
        return Res.ok(refundOrderAdminService.refund(param));
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "同步退款状态")
    @PostMapping("/sync")
    public Result<RefundOrderResult> sync(
            @NotNull(message = "{validation.field.id.notNull}") Long id) {
        return Res.ok(refundOrderAdminService.sync(id));
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "手动关闭异常退款单(仅PROGRESS且创建超7天)")
    @PostMapping("/manual-close")
    public Result<RefundOrderResult> manualClose(
            @NotNull(message = "{validation.field.id.notNull}") Long id) {
        return Res.ok(refundOrderAdminService.manualClose(id));
    }

    @PermCode(code = PermCodes.Action.EXPORT)
    @Operation(summary = "导出退款订单")
    @OperateLog(title = "导出退款订单", businessType = OperateLogType.EXPORT, saveParam = false)
    @PostMapping("/export")
    public void export(HttpServletResponse response, RefundOrderQuery query) {
        ExcelUtils.validateExportTimeRange(query.getCreateTimeStart(), query.getCreateTimeEnd());
        ExcelUtils.export(RefundOrderExportResult.class, "退款订单", response, wrapper -> {
            WriteSheet sheet = ExcelWriterWrapper.buildSheet("退款订单");
            int pageNo = 1;
            int totalRows = 0;
            while (totalRows < ExcelUtils.EXPORT_MAX_ROWS) {
                var page = refundOrderAdminService.page(new PageParam(pageNo, ExcelUtils.EXPORT_PAGE_SIZE), query);
                var exportData = RefundOrderExportConvert.CONVERT.toExportResultList(page.getRecords());
                if (exportData.isEmpty()) break;
                wrapper.write(exportData, sheet);
                totalRows += exportData.size();
                if (exportData.size() < ExcelUtils.EXPORT_PAGE_SIZE) break;
                pageNo++;
            }
        });
    }
}
