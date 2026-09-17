package cn.daxpay.open.payment.admin.controller.trade;

import cn.daxpay.open.payment.admin.service.trade.FundFlowAdminService;
import cn.daxpay.open.payment.trade.flow.convert.export.FundFlowExportConvert;
import cn.daxpay.open.payment.trade.flow.param.FundFlowQuery;
import cn.daxpay.open.payment.trade.flow.result.FundFlowResult;
import cn.daxpay.open.payment.trade.flow.result.export.FundFlowExportResult;
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
import cn.idev.excel.write.metadata.WriteSheet;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/// # 资金流水(管理)
///
/// 收款/退款成功流水查询, 只读。
@PermCode(menuCode = PermCodes.Trade.FundFlow.MENU)
@Validated
@Tag(name = "资金流水(管理)")
@RestController
@RequestMapping("/admin/fund-flow")
@RequiredArgsConstructor
public class FundFlowAdminController {

    private final FundFlowAdminService fundFlowAdminService;

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "资金流水分页")
    @GetMapping("/page")
    public Result<PageResult<FundFlowResult>> page(PageParam pageParam, FundFlowQuery query) {
        return Res.ok(fundFlowAdminService.page(pageParam, query));
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "资金流水详情")
    @GetMapping("/get-by-id")
    public Result<FundFlowResult> getById(
            @NotNull(message = "{validation.field.id.notNull}") Long id) {
        return Res.ok(fundFlowAdminService.findById(id));
    }

    @PermCode(code = PermCodes.Action.EXPORT)
    @Operation(summary = "导出资金流水")
    @OperateLog(title = "导出资金流水", businessType = OperateLogType.EXPORT, saveParam = false)
    @PostMapping("/export")
    public void export(HttpServletResponse response, FundFlowQuery query) {
        ExcelUtils.validateExportTimeRange(query.getCreateTimeStart(), query.getCreateTimeEnd());
        ExcelUtils.export(FundFlowExportResult.class, "资金流水", response, wrapper -> {
            WriteSheet sheet = ExcelWriterWrapper.buildSheet("资金流水");
            int pageNo = 1;
            int totalRows = 0;
            while (totalRows < ExcelUtils.EXPORT_MAX_ROWS) {
                var page = fundFlowAdminService.page(new PageParam(pageNo, ExcelUtils.EXPORT_PAGE_SIZE), query);
                var exportData = FundFlowExportConvert.CONVERT.toExportResultList(page.getRecords());
                if (exportData.isEmpty()) break;
                wrapper.write(exportData, sheet);
                totalRows += exportData.size();
                if (exportData.size() < ExcelUtils.EXPORT_PAGE_SIZE) break;
                pageNo++;
            }
        });
    }
}
