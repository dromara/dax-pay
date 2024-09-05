package cn.daxpay.multi.gateway.controller;

import cn.bootx.platform.core.annotation.IgnoreAuth;
import cn.daxpay.multi.core.param.trade.pay.QueryPayParam;
import cn.daxpay.multi.core.param.trade.refund.QueryRefundParam;
import cn.daxpay.multi.core.param.trade.transfer.QueryTransferParam;
import cn.daxpay.multi.core.result.DaxResult;
import cn.daxpay.multi.core.result.trade.pay.PayOrderResult;
import cn.daxpay.multi.core.result.trade.refund.RefundOrderResult;
import cn.daxpay.multi.core.result.trade.transfer.TransferOrderResult;
import cn.daxpay.multi.core.util.DaxRes;
import cn.daxpay.multi.service.common.anno.PaymentVerify;
import cn.daxpay.multi.service.service.order.pay.PayOrderQueryService;
import cn.daxpay.multi.service.service.order.refund.RefundOrderQueryService;
import cn.daxpay.multi.service.service.order.transfer.TransferOrderQueryService;
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
 * @since 2024/6/4
 */
@IgnoreAuth
@Tag(name = "统一查询接口")
@RestController
@RequestMapping("/unipay/query")
@RequiredArgsConstructor
public class UniQueryController {

    private final PayOrderQueryService payOrderQueryService;
    private final RefundOrderQueryService refundOrderQueryService;
    private final TransferOrderQueryService transferOrderQueryService;

    @PaymentVerify
    @Operation(summary = "支付订单查询接口")
    @PostMapping("/payOrder")
    public DaxResult<PayOrderResult> queryPayOrder(@RequestBody QueryPayParam param){
        return DaxRes.ok(payOrderQueryService.queryPayOrder(param));
    }

    @PaymentVerify
    @Operation(summary = "退款订单查询接口")
    @PostMapping("/refundOrder")
     public DaxResult<RefundOrderResult> queryRefundOrder(@RequestBody QueryRefundParam param){
        return DaxRes.ok(refundOrderQueryService.queryRefundOrder(param));
    }

    @PaymentVerify
    @Operation(summary = "转账订单查询接口")
    @PostMapping("/transferOrder")
    public DaxResult<TransferOrderResult> transferOrder(@RequestBody QueryTransferParam param){
        return DaxRes.ok(transferOrderQueryService.queryTransferOrder(param));
    }

}
