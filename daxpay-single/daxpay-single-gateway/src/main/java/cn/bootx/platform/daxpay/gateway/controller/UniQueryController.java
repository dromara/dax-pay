package cn.bootx.platform.daxpay.gateway.controller;

import cn.bootx.platform.common.core.annotation.CountTime;
import cn.bootx.platform.common.core.annotation.IgnoreAuth;
import cn.bootx.platform.daxpay.param.pay.QueryPayParam;
import cn.bootx.platform.daxpay.param.pay.QueryRefundParam;
import cn.bootx.platform.daxpay.result.DaxResult;
import cn.bootx.platform.daxpay.result.order.PayOrderResult;
import cn.bootx.platform.daxpay.result.order.RefundOrderResult;
import cn.bootx.platform.daxpay.service.annotation.PaymentApi;
import cn.bootx.platform.daxpay.service.core.order.refund.service.RefundOrderQueryService;
import cn.bootx.platform.daxpay.util.DaxRes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 统一查询接口
 * @author xxm
 * @since 2024/2/7
 */
@IgnoreAuth
@Tag(name = "统一查询接口")
@RestController
@RequestMapping("/uni/query")
@RequiredArgsConstructor
public class UniQueryController {

    private final cn.bootx.platform.daxpay.service.core.order.pay.service.PayOrderQueryService PayOrderQueryService;
    private final RefundOrderQueryService payRefundQueryService;

    @CountTime
    @PaymentApi("queryPayOrder")
    @Operation(summary = "支付订单查询接口")
    @PostMapping("/payOrder")
    public DaxResult<PayOrderResult> queryPayOrder(@RequestBody QueryPayParam param){
        return DaxRes.ok(PayOrderQueryService.queryPayOrder(param));
    }

    @CountTime
    @PaymentApi("queryRefundOrder")
    @Operation(summary = "退款订单查询接口")
    @PostMapping("/refundOrder")
     public DaxResult<RefundOrderResult> queryRefundOrder(@RequestBody QueryRefundParam param){
        return DaxRes.ok(payRefundQueryService.queryRefundOrder(param));
    }
}
