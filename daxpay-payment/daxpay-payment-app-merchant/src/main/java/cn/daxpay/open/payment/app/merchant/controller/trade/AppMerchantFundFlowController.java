package cn.daxpay.open.payment.app.merchant.controller.trade;

import cn.daxpay.open.payment.app.merchant.service.trade.AppMerchantFundFlowService;
import cn.daxpay.open.payment.trade.flow.convert.export.FundFlowExportConvert;
import cn.daxpay.open.payment.trade.flow.param.FundFlowQuery;
import cn.daxpay.open.payment.trade.flow.result.FundFlowResult;
import cn.daxpay.open.payment.trade.flow.result.export.FundFlowExportResult;
import cn.daxpay.open.platform.common.excel.ExcelUtils;
import cn.daxpay.open.platform.core.annotation.OperateLog;
import cn.daxpay.open.platform.core.annotation.PermCode;
import cn.daxpay.open.platform.core.code.PermCodes;
import cn.daxpay.open.platform.core.enums.common.OperateLogType;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import cn.daxpay.open.platform.core.rest.result.Result;
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

/// # 资金流水(商户移动端)
///
/// 收款/退款成功流水查询, 只读。业务编排委托 [AppMerchantFundFlowService]。
@PermCode(menuCode = PermCodes.Trade.FundFlow.MENU)
@Validated
@Tag(name = "资金流水(商户移动端)")
@RestController
@RequestMapping("/app-mch/order/fund-flow")
@RequiredArgsConstructor
public class AppMerchantFundFlowController {

    private final AppMerchantFundFlowService fundFlowService;

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "资金流水分页")
    @GetMapping("/page")
    public Result<PageResult<FundFlowResult>> page(PageParam pageParam, FundFlowQuery query) {
        return Res.ok(fundFlowService.page(pageParam, query));
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "根据ID查询资金流水详情")
    @GetMapping("/get-by-id")
    public Result<FundFlowResult> findById(
            @NotNull(message = "{validation.field.id.notNull}") Long id) {
        return Res.ok(fundFlowService.findById(id));
    }

    @PermCode(code = PermCodes.Action.EXPORT)
    @Operation(summary = "导出资金流水")
    @OperateLog(title = "导出资金流水", businessType = OperateLogType.EXPORT, saveParam = false)
    @PostMapping("/export")
    public void export(HttpServletResponse response, FundFlowQuery query) {
        // 导出时间范围必填, 跨度上限 90 天
        ExcelUtils.validateExportTimeRange(query.getCreateTimeStart(), query.getCreateTimeEnd());
        ExcelUtils.exportPaged(FundFlowExportResult.class, "资金流水", response,
                (pageNo, size) -> fundFlowService.page(new PageParam(pageNo, size), query),
                FundFlowExportConvert.CONVERT::toExportResultList);
    }
}
