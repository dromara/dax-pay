package org.dromara.daxpay.payment.admin.controller.report;

import org.dromara.daxpay.platform.core.rest.Res;
import org.dromara.daxpay.platform.core.rest.result.Result;
import org.dromara.daxpay.payment.pay.param.report.TradeReportQuery;
import org.dromara.daxpay.payment.pay.result.report.MerchantReportResult;
import org.dromara.daxpay.payment.pay.result.report.TradeReportResult;
import org.dromara.daxpay.payment.pay.result.report.TradeStatisticsReport;
import org.dromara.daxpay.payment.pay.service.report.IndexTradeReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/// # 首页交易报表
///
@Validated
@Tag(name = "运营平台首页交易报表")
@RestController
@RequestMapping("/admin/report/index")
@RequiredArgsConstructor
public class IndexTradeReportAdminController {

    private final IndexTradeReportService tradeReportService;

    @Operation(summary = "支付交易信息统计")
    @GetMapping("/pay")
    public Result<TradeReportResult> pryTradeReport(TradeReportQuery query){
        return Res.ok(tradeReportService.pryTradeReport(query));
    }

    @Operation(summary = "退款交易信息统计")
    @GetMapping("/refund")
    public Result<TradeReportResult> refundTradeReport(TradeReportQuery query){
        return Res.ok(tradeReportService.refundTradeReport(query));
    }

    @Operation(summary = "支付交易通道统计")
    @GetMapping("/pay-channel")
    public Result<List<TradeReportResult>> payChannelReport(TradeReportQuery query){
        return Res.ok(tradeReportService.payChannelReport(query));
    }

    @Operation(summary = "退款交易通道统计")
    @GetMapping("/refund-channel")
    public Result<List<TradeReportResult>> refundChannelReport(TradeReportQuery query){
        return Res.ok(tradeReportService.refundChannelReport(query));
    }

    @Operation(summary = "支付交易方式统计")
    @GetMapping("/pay-method")
    public Result<List<TradeReportResult>> payMethodReport(TradeReportQuery query){
        return Res.ok(tradeReportService.payMethodReport(query));
    }

    @Operation(summary = "商户和应用数量统计")
    @GetMapping("/merchant-count")
    public Result<MerchantReportResult> merchantCount(){
        return Res.ok(tradeReportService.merchantCount(new TradeReportQuery()));
    }

    @Operation(summary = "交易统计报表")
    @GetMapping("/trade-statistics-report")
    public Result<List<TradeStatisticsReport>> tradeStatisticsReport(TradeReportQuery query){
        return Res.ok(tradeReportService.tradeStatistics(query));
    }
}
