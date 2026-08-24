package cn.daxpay.open.payment.unipay.trade.controller;

import cn.daxpay.open.platform.core.annotation.IgnoreAuth;
import cn.daxpay.open.payment.unipay.param.trade.pay.NormalPayQueryParam;
import cn.daxpay.open.payment.unipay.param.trade.refund.RefundOrderQueryParam;
import cn.daxpay.open.payment.unipay.param.trade.alloc.AllocOrderQueryParam;
import cn.daxpay.open.payment.unipay.param.trade.transfer.TransferOrderQueryParam;

import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.unipay.result.trade.pay.NormalPayOrderResult;
import cn.daxpay.open.payment.unipay.result.trade.refund.RefundOrderResult;
import cn.daxpay.open.payment.unipay.result.trade.alloc.AllocOrderResult;
import cn.daxpay.open.payment.unipay.result.trade.transfer.TransferOrderResult;
import cn.daxpay.open.payment.common.util.DaxRes;
import cn.daxpay.open.payment.unipay.aop.PaymentVerify;
import cn.daxpay.open.payment.unipay.trade.service.NormalPayOrderQueryService;
import cn.daxpay.open.payment.unipay.trade.service.RefundOrderQueryService;
import cn.daxpay.open.payment.unipay.trade.service.AllocOrderQueryService;
import cn.daxpay.open.payment.unipay.trade.service.TransferOrderQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/// # 统一查询接口
///
@PaymentVerify
@IgnoreAuth
@Tag(name = "统一查询接口")
@RestController
@RequestMapping("/unipay/query")
@RequiredArgsConstructor
public class UniQueryController {

    private final NormalPayOrderQueryService normalPayOrderQueryService;
    private final RefundOrderQueryService refundOrderQueryService;
    private final AllocOrderQueryService allocOrderQueryService;
    private final TransferOrderQueryService transferOrderQueryService;

    @Operation(summary = "支付订单查询接口")
    @PostMapping("/pay-order")
    public DaxResult<NormalPayOrderResult> queryPayOrder(@RequestBody NormalPayQueryParam param){
        return DaxRes.ok(normalPayOrderQueryService.queryPayOrder(param));
    }

    @Operation(summary = "退款订单查询接口")
    @PostMapping("/refund-order")
    public DaxResult<RefundOrderResult> queryRefundOrder(@RequestBody RefundOrderQueryParam param){
        return DaxRes.ok(refundOrderQueryService.queryRefundOrder(param));
    }

    @Operation(summary = "分账订单查询接口")
    @PostMapping("/alloc-order")
    public DaxResult<AllocOrderResult> queryAllocOrder(@RequestBody AllocOrderQueryParam param){
        return DaxRes.ok(allocOrderQueryService.queryAllocOrder(param));
    }

    @Operation(summary = "转账订单查询接口")
    @PostMapping("/transfer-order")
    public DaxResult<TransferOrderResult> queryTransferOrder(@RequestBody TransferOrderQueryParam param){
        return DaxRes.ok(transferOrderQueryService.query(param));
    }

}
