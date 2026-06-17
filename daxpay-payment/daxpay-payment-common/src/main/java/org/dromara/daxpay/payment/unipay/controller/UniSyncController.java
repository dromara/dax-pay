package org.dromara.daxpay.payment.unipay.controller;

import org.dromara.daxpay.platform.core.annotation.IgnoreAuth;
import org.dromara.daxpay.payment.unipay.param.trade.pay.PaySyncParam;
import org.dromara.daxpay.payment.unipay.param.trade.refund.RefundSyncParam;
import org.dromara.daxpay.payment.unipay.param.trade.transfer.TransferSyncParam;
import org.dromara.daxpay.payment.common.result.DaxResult;
import org.dromara.daxpay.payment.unipay.result.trade.pay.PaySyncResult;
import org.dromara.daxpay.payment.unipay.result.trade.refund.RefundSyncResult;
import org.dromara.daxpay.payment.unipay.result.trade.transfer.TransferSyncResult;
import org.dromara.daxpay.payment.common.util.DaxRes;
import org.dromara.daxpay.payment.old.pay.anno.PaymentVerify;
import org.dromara.daxpay.payment.old.pay.service.trade.pay.PaySyncService;
import org.dromara.daxpay.payment.old.pay.service.trade.refund.RefundSyncService;
import org.dromara.daxpay.payment.old.pay.service.trade.transfer.TransferSyncService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/// # 统一同步接口
///
@PaymentVerify
@IgnoreAuth
@Tag(name = "统一同步接口")
@RestController
@RequestMapping("/unipay/sync/order")
@RequiredArgsConstructor
public class UniSyncController {

    private final PaySyncService paySyncService;

    private final RefundSyncService refundSyncService;

    private final TransferSyncService transferSyncService;

    @Operation(summary = "支付订单同步接口")
    @PostMapping("/pay")
    public DaxResult<PaySyncResult> pay(@RequestBody PaySyncParam param){
        return DaxRes.ok(paySyncService.sync(param));
    }

    @Operation(summary = "退款订单同步接口")
    @PostMapping("/refund")
    public DaxResult<RefundSyncResult> refund(@RequestBody RefundSyncParam param){
        return DaxRes.ok(refundSyncService.sync(param));
    }

    @Operation(summary = "转账订单同步接口")
    @PostMapping("/transfer")
    public DaxResult<TransferSyncResult> transfer(@RequestBody TransferSyncParam param){
        return DaxRes.ok(transferSyncService.sync(param));
    }
}
