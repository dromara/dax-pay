package org.dromara.daxpay.payment.miniapp.controller;

import cn.bootx.platform.core.annotation.ClientCode;
import cn.bootx.platform.core.annotation.RequestGroup;
import cn.bootx.platform.core.annotation.RequestPath;
import cn.bootx.platform.core.rest.Res;
import cn.bootx.platform.core.rest.result.Result;
import org.dromara.daxpay.payment.common.code.DaxPayCode;
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

/**
 * 小程序报表
 * @author xxm
 * @since 2025/4/28
 */
@Validated
@Tag(name = "小程序报表")
@ClientCode(DaxPayCode.Client.MERCHANT)
@RestController
@RequestMapping("/mini/mch/report")
@RequestGroup(groupCode = "MiniMchReport", groupName = "小程序首页报表", moduleCode = "MiniMch")
@RequiredArgsConstructor
public class MiniMchReportController {
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

}
