package org.dromara.daxpay.service.controller.report;

import cn.bootx.platform.core.annotation.RequestPath;
import cn.bootx.platform.core.rest.Res;
import cn.bootx.platform.core.rest.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.dromara.daxpay.service.param.report.TradeReportQuery;
import org.dromara.daxpay.service.result.report.TradeReportResult;
import org.dromara.daxpay.service.service.report.IndexTradeReportService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 首页交易报表
 * @author xxm
 * @since 2024/11/17
 */
@Tag(name = "首页交易报表")
@RestController
@RequestMapping("/report/index")
@RequiredArgsConstructor
public class IndexTradeReportController {

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
}
