package cn.daxpay.open.payment.admin.controller.trade;

import cn.daxpay.open.payment.admin.service.trade.NormalPayOrderAdminService;
import cn.daxpay.open.payment.trade.order.convert.export.NormalOrderExportConvert;
import cn.daxpay.open.payment.trade.order.param.NormalPayOrderQuery;
import cn.daxpay.open.payment.trade.order.result.NormalPayOrderResult;
import cn.daxpay.open.payment.trade.order.result.export.NormalOrderExportResult;
import cn.daxpay.open.payment.unipay.result.trade.pay.NormalPaySyncResult;
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
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/// # 普通支付业务单(管理)
///
/// 面向运营后台的业务订单(容器)管理。业务编排委托 [NormalPayOrderAdminService]。
@PermCode(menuCode = PermCodes.Trade.Order.MENU)
@Validated
@Tag(name = "普通支付业务单(管理)")
@RestController
@RequestMapping("/admin/order/normal-pay")
@RequiredArgsConstructor
public class NormalPayOrderAdminController {

    private final NormalPayOrderAdminService normalPayOrderAdminService;

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "普通支付业务单分页")
    @GetMapping("/page")
    public Result<PageResult<NormalPayOrderResult>> page(PageParam pageParam, NormalPayOrderQuery query) {
        return Res.ok(normalPayOrderAdminService.page(pageParam, query));
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "根据ID查询普通支付业务单详情")
    @GetMapping("/get-by-id")
    public Result<NormalPayOrderResult> findById(
            @NotNull(message = "{validation.field.id.notNull}") Long id) {
        return Res.ok(normalPayOrderAdminService.findById(id));
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "同步支付状态")
    @PostMapping("/sync")
    public Result<NormalPaySyncResult> sync(
            @NotNull(message = "{validation.field.id.notNull}") Long id) {
        return Res.ok(normalPayOrderAdminService.sync(id));
    }

    @PermCode(code = PermCodes.Action.EXPORT)
    @Operation(summary = "导出普通支付业务单")
    @OperateLog(title = "导出普通支付业务单", businessType = OperateLogType.EXPORT, saveParam = false)
    @PostMapping("/export")
    public void export(HttpServletResponse response, NormalPayOrderQuery query) {
        ExcelUtils.validateExportTimeRange(query.getCreateTimeStart(), query.getCreateTimeEnd());
        ExcelUtils.export(NormalOrderExportResult.class, "普通支付业务单", response, wrapper -> {
            WriteSheet sheet = ExcelWriterWrapper.buildSheet("普通支付业务单");
            int pageNo = 1;
            int totalRows = 0;
            while (totalRows < ExcelUtils.EXPORT_MAX_ROWS) {
                var page = normalPayOrderAdminService.page(new PageParam(pageNo, ExcelUtils.EXPORT_PAGE_SIZE), query);
                var exportData = NormalOrderExportConvert.CONVERT.toExportResultList(page.getRecords());
                if (exportData.isEmpty()) break;
                wrapper.write(exportData, sheet);
                totalRows += exportData.size();
                if (exportData.size() < ExcelUtils.EXPORT_PAGE_SIZE) break;
                pageNo++;
            }
        });
    }
}
