package cn.daxpay.single.gateway.controller;

import cn.bootx.platform.common.core.annotation.IgnoreAuth;
import cn.daxpay.single.core.code.PaymentApiCode;
import cn.daxpay.single.core.param.payment.allocation.AllocSyncParam;
import cn.daxpay.single.core.param.payment.pay.PaySyncParam;
import cn.daxpay.single.core.param.payment.refund.RefundSyncParam;
import cn.daxpay.single.core.param.payment.transfer.TransferSyncParam;
import cn.daxpay.single.core.result.DaxResult;
import cn.daxpay.single.core.result.sync.AllocSyncResult;
import cn.daxpay.single.core.result.sync.PaySyncResult;
import cn.daxpay.single.core.result.sync.RefundSyncResult;
import cn.daxpay.single.core.result.sync.TransferSyncResult;
import cn.daxpay.single.service.annotation.InitPaymentContext;
import cn.daxpay.single.service.annotation.PaymentVerify;
import cn.daxpay.single.service.core.payment.sync.service.AllocSyncService;
import cn.daxpay.single.service.core.payment.sync.service.PaySyncService;
import cn.daxpay.single.service.core.payment.sync.service.RefundSyncService;
import cn.daxpay.single.service.core.payment.sync.service.TransferSyncService;
import cn.daxpay.single.core.util.DaxRes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 统一同步接口
 * @author xxm
 * @since 2024/5/26
 */
@IgnoreAuth
@Tag(name = "统一同步接口")
@RestController
@RequestMapping("/unipay/sync")
@RequiredArgsConstructor
public class UniPaySyncController {

    private final PaySyncService paySyncService;
    private final RefundSyncService refundSyncService;
    private final AllocSyncService allocSyncService;
    private final TransferSyncService transferSyncService;

    @PaymentVerify
    @InitPaymentContext(PaymentApiCode.SYNC_PAY)
    @Operation(summary = "支付同步接口")
    @PostMapping("/pay")
    public DaxResult<PaySyncResult> pay(@RequestBody PaySyncParam param){
        return DaxRes.ok(paySyncService.sync(param));
    }

    @PaymentVerify
    @InitPaymentContext(PaymentApiCode.SYNC_REFUND)
    @Operation(summary = "退款同步接口")
    @PostMapping("/refund")
    public DaxResult<RefundSyncResult> refund(@RequestBody RefundSyncParam param){
        return DaxRes.ok(refundSyncService.sync(param));
    }

    @PaymentVerify
    @InitPaymentContext(PaymentApiCode.SYNC_ALLOCATION)
    @Operation(summary = "分账同步接口")
    @PostMapping("/allocation")
    public DaxResult<AllocSyncResult> allocation(@RequestBody AllocSyncParam param){
        return DaxRes.ok(allocSyncService.sync(param));
    }

    @PaymentVerify
    @InitPaymentContext(PaymentApiCode.SYNC_TRANSFER)
    @Operation(summary = "转账同步接口")
    @PostMapping("/transfer")
    public DaxResult<TransferSyncResult> transfer(@RequestBody TransferSyncParam param){
        return DaxRes.ok(transferSyncService.sync(param));
    }

}
