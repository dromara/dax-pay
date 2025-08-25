package org.dromara.daxpay.server.controller.merchant.report;

import cn.bootx.platform.core.annotation.ClientCode;
import cn.bootx.platform.core.annotation.RequestGroup;
import cn.bootx.platform.core.annotation.RequestPath;
import cn.bootx.platform.core.rest.Res;
import cn.bootx.platform.core.rest.result.Result;
import org.dromara.daxpay.service.common.code.DaxPayCode;
import org.dromara.daxpay.service.pay.param.report.TradeReportQuery;
import org.dromara.daxpay.service.pay.result.report.TradeReportResult;
import org.dromara.daxpay.service.pay.result.report.TradeStatisticsReport;
import org.dromara.daxpay.service.pay.service.report.IndexTradeReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 首页交易报表
 * @author xxm
 * @since 2024/11/17
 */
@Validated
@Tag(name = "商户平台交易报表")
@RequestGroup(groupCode = "IndexTradeReport", groupName = "商户平台交易报表", moduleCode = "report",moduleName = "(DaxPay)统计报表")
@ClientCode(DaxPayCode.Client.MERCHANT)
@RestController
@RequestMapping("/merchant/report/index")
@RequiredArgsConstructor
public class IndexTradeReportMerchantController {

    private final IndexTradeReportService tradeReportService;

    @RequestPath("支付交易信息统计")
    @Operation(summary = "支付交易信息统计")
    @GetMapping("/pay")
    public Result<TradeReportResult> pryTradeReport(TradeReportQuery query){
        return Res.ok(tradeReportService.pryTradeReport(query));
    }
    @RequestPath("退款交易信息统计")
    @Operation(summary = "退款交易信息统计")
    @GetMapping("/refund")
    public Result<TradeReportResult> refundTradeReport(TradeReportQuery query){
        return Res.ok(tradeReportService.refundTradeReport(query));
    }

    @RequestPath("支付交易通道统计")
    @Operation(summary = "支付交易通道统计")
    @GetMapping("/payChannel")
    public Result<List<TradeReportResult>> payChannelReport(TradeReportQuery query){
        return Res.ok(tradeReportService.payChannelReport(query));
    }

    @RequestPath("退款交易通道统计")
    @Operation(summary = "退款交易通道统计")
    @GetMapping("/refundChannel")
    public Result<List<TradeReportResult>> refundChannelReport(TradeReportQuery query){
        return Res.ok(tradeReportService.refundChannelReport(query));
    }

    @RequestPath("支付交易方式统计")
    @Operation(summary = "支付交易方式统计")
    @GetMapping("/payMethod")
    public Result<List<TradeReportResult>> payMethodReport(TradeReportQuery query){
        return Res.ok(tradeReportService.payMethodReport(query));
    }

    @RequestPath("交易统计报表")
    @Operation(summary = "交易统计报表")
    @GetMapping("/tradeStatisticsReport")
    public Result<List<TradeStatisticsReport>> tradeStatisticsReport(TradeReportQuery query){
        return Res.ok(tradeReportService.tradeStatistics(query));
    }
}
