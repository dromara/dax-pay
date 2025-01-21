package org.dromara.daxpay.service.controller.unipay;

import cn.bootx.platform.core.annotation.IgnoreAuth;
import org.dromara.daxpay.core.param.trade.pay.QueryPayParam;
import org.dromara.daxpay.core.param.trade.refund.QueryRefundParam;
import org.dromara.daxpay.core.param.trade.transfer.QueryTransferParam;
import org.dromara.daxpay.core.result.DaxResult;
import org.dromara.daxpay.core.result.trade.pay.PayOrderResult;
import org.dromara.daxpay.core.result.trade.refund.RefundOrderResult;
import org.dromara.daxpay.core.result.trade.transfer.TransferOrderResult;
import org.dromara.daxpay.core.util.DaxRes;
import org.dromara.daxpay.service.common.anno.PaymentVerify;
import org.dromara.daxpay.service.service.order.pay.PayOrderQueryService;
import org.dromara.daxpay.service.service.order.refund.RefundOrderQueryService;
import org.dromara.daxpay.service.service.order.transfer.TransferOrderQueryService;
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
@PaymentVerify
@IgnoreAuth
@Tag(name = "统一查询接口")
@RestController
@RequestMapping("/unipay/query")
@RequiredArgsConstructor
public class UniQueryController {

    private final PayOrderQueryService payOrderQueryService;
    private final RefundOrderQueryService refundOrderQueryService;
    private final TransferOrderQueryService transferOrderQueryService;

    @Operation(summary = "支付订单查询接口")
    @PostMapping("/payOrder")
    public DaxResult<PayOrderResult> queryPayOrder(@RequestBody QueryPayParam param){
        return DaxRes.ok(payOrderQueryService.queryPayOrder(param));
    }

    @Operation(summary = "退款订单查询接口")
    @PostMapping("/refundOrder")
     public DaxResult<RefundOrderResult> queryRefundOrder(@RequestBody QueryRefundParam param){
        return DaxRes.ok(refundOrderQueryService.queryRefundOrder(param));
    }

    @Operation(summary = "转账订单查询接口")
    @PostMapping("/transferOrder")
    public DaxResult<TransferOrderResult> transferOrder(@RequestBody QueryTransferParam param){
        return DaxRes.ok(transferOrderQueryService.queryTransferOrder(param));
    }

}
