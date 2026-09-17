package cn.daxpay.open.payment.app.merchant.controller.trade;

import cn.daxpay.open.payment.app.merchant.service.trade.AppMerchantPayTradeService;
import cn.daxpay.open.payment.trade.order.convert.export.PayTradeExportConvert;
import cn.daxpay.open.payment.trade.order.param.PayTradeQuery;
import cn.daxpay.open.payment.trade.order.result.PayTradeResult;
import cn.daxpay.open.payment.trade.order.result.export.PayTradeExportResult;
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

/// # 资金交易凭证(商户移动端)
///
/// 面向商户移动端的资金交易凭证查询与状态同步。业务编排委托 [AppMerchantPayTradeService]。
@PermCode(menuCode = PermCodes.Trade.Fund.MENU)
@Validated
@Tag(name = "资金交易凭证(商户移动端)")
@RestController
@RequestMapping("/app-mch/order/pay-trade")
@RequiredArgsConstructor
public class AppMerchantPayTradeController {

    private final AppMerchantPayTradeService payTradeService;

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "资金交易凭证分页")
    @GetMapping("/page")
    public Result<PageResult<PayTradeResult>> page(PageParam pageParam, PayTradeQuery query) {
        return Res.ok(payTradeService.page(pageParam, query));
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "根据ID查询资金交易凭证详情")
    @GetMapping("/get-by-id")
    public Result<PayTradeResult> findById(
            @NotNull(message = "{validation.field.id.notNull}") Long id) {
        return Res.ok(payTradeService.findById(id));
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "同步支付状态")
    @PostMapping("/sync")
    public Result<NormalPaySyncResult> sync(
            @NotNull(message = "{validation.field.id.notNull}") Long id) {
        return Res.ok(payTradeService.sync(id));
    }

    @PermCode(code = PermCodes.Action.EXPORT)
    @Operation(summary = "导出资金交易凭证")
    @OperateLog(title = "导出资金交易凭证", businessType = OperateLogType.EXPORT, saveParam = false)
    @PostMapping("/export")
    public void export(HttpServletResponse response, PayTradeQuery query) {
        ExcelUtils.validateExportTimeRange(query.getCreateTimeStart(), query.getCreateTimeEnd());
        ExcelUtils.export(PayTradeExportResult.class, "交易凭证", response, wrapper -> {
            WriteSheet sheet = ExcelWriterWrapper.buildSheet("交易凭证");
            int pageNo = 1;
            int totalRows = 0;
            while (totalRows < ExcelUtils.EXPORT_MAX_ROWS) {
                var page = payTradeService.page(new PageParam(pageNo, ExcelUtils.EXPORT_PAGE_SIZE), query);
                var exportData = PayTradeExportConvert.CONVERT.toExportResultList(page.getRecords());
                if (exportData.isEmpty()) break;
                wrapper.write(exportData, sheet);
                totalRows += exportData.size();
                if (exportData.size() < ExcelUtils.EXPORT_PAGE_SIZE) break;
                pageNo++;
            }
        });
    }
}
