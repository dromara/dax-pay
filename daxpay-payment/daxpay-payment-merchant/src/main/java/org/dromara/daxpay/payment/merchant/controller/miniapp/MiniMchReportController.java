package org.dromara.daxpay.payment.merchant.controller.miniapp;

import org.dromara.daxpay.platform.core.rest.Res;
import org.dromara.daxpay.platform.core.rest.result.Result;
import org.dromara.daxpay.payment.pay.param.report.TradeReportQuery;
import org.dromara.daxpay.payment.pay.result.report.TradeReportResult;
import org.dromara.daxpay.payment.pay.service.report.IndexTradeReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/// # 小程序报表
///
@Validated
@Tag(name = "小程序报表")
@RestController
@RequestMapping("/mini/mch/report")
@RequiredArgsConstructor
public class MiniMchReportController {
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

}
