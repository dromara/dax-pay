package cn.daxpay.open.payment.app.merchant.controller.trade;

import cn.daxpay.open.payment.app.merchant.service.trade.AppMerchantGatewayPayOrderService;
import cn.daxpay.open.payment.trade.order.convert.export.GatewayOrderExportConvert;
import cn.daxpay.open.payment.trade.order.param.GatewayPayOrderQuery;
import cn.daxpay.open.payment.trade.order.result.GatewayPayOrderResult;
import cn.daxpay.open.payment.trade.order.result.export.GatewayOrderExportResult;
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
import org.springframework.web.bind.annotation.RestController;

/// # 网关支付业务单(商户移动端)
///
/// 面向商户移动端的网关支付订单查询与状态同步。业务编排委托 [AppMerchantGatewayPayOrderService]。
@PermCode(menuCode = PermCodes.Trade.GatewayOrder.MENU)
@Validated
@Tag(name = "网关支付业务单(商户移动端)")
@RestController
@RequestMapping("/app-mch/order/gateway-pay")
@RequiredArgsConstructor
public class AppMerchantGatewayPayOrderController {

    private final AppMerchantGatewayPayOrderService gatewayPayOrderService;

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "网关支付业务单分页")
    @GetMapping("/page")
    public Result<PageResult<GatewayPayOrderResult>> page(PageParam pageParam, GatewayPayOrderQuery query) {
        return Res.ok(gatewayPayOrderService.page(pageParam, query));
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "根据ID查询详情")
    @GetMapping("/get-by-id")
    public Result<GatewayPayOrderResult> findById(
            @NotNull(message = "{validation.field.id.notNull}") Long id) {
        return Res.ok(gatewayPayOrderService.findById(id));
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "同步支付状态")
    @PostMapping("/sync")
    public Result<NormalPaySyncResult> sync(
            @NotNull(message = "{validation.field.id.notNull}") Long id) {
        return Res.ok(gatewayPayOrderService.sync(id));
    }

    @PermCode(code = PermCodes.Action.EXPORT)
    @Operation(summary = "导出网关支付业务单")
    @OperateLog(title = "导出网关支付业务单", businessType = OperateLogType.EXPORT, saveParam = false)
    @PostMapping("/export")
    public void export(HttpServletResponse response, GatewayPayOrderQuery query) {
        ExcelUtils.validateExportTimeRange(query.getCreateTimeStart(), query.getCreateTimeEnd());
        ExcelUtils.export(GatewayOrderExportResult.class, "网关支付业务单", response, wrapper -> {
            WriteSheet sheet = ExcelWriterWrapper.buildSheet("网关支付业务单");
            int pageNo = 1;
            int totalRows = 0;
            while (totalRows < ExcelUtils.EXPORT_MAX_ROWS) {
                var page = gatewayPayOrderService.page(new PageParam(pageNo, ExcelUtils.EXPORT_PAGE_SIZE), query);
                var exportData = GatewayOrderExportConvert.CONVERT.toExportResultList(page.getRecords());
                if (exportData.isEmpty()) break;
                wrapper.write(exportData, sheet);
                totalRows += exportData.size();
                if (exportData.size() < ExcelUtils.EXPORT_PAGE_SIZE) break;
                pageNo++;
            }
        });
    }
}
