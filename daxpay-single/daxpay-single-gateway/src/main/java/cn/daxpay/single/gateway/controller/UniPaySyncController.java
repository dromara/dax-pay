package cn.daxpay.single.gateway.controller;

import cn.daxpay.single.code.PaymentApiCode;
import cn.daxpay.single.param.payment.allocation.AllocSyncParam;
import cn.daxpay.single.param.payment.pay.PaySyncParam;
import cn.daxpay.single.param.payment.refund.RefundSyncParam;
import cn.daxpay.single.result.DaxResult;
import cn.daxpay.single.result.allocation.AllocationSyncResult;
import cn.daxpay.single.result.pay.SyncResult;
import cn.daxpay.single.service.annotation.PaymentSign;
import cn.daxpay.single.service.annotation.PlatformInitContext;
import cn.daxpay.single.service.core.payment.allocation.service.AllocationSyncService;
import cn.daxpay.single.service.core.payment.sync.service.PaySyncService;
import cn.daxpay.single.service.core.payment.sync.service.RefundSyncService;
import cn.daxpay.single.util.DaxRes;
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
@Tag(name = "")
@RestController
@RequestMapping("/unipay/sync/")
@RequiredArgsConstructor
public class UniPaySyncController {

    private final PaySyncService paySyncService;
    private final RefundSyncService refundSyncService;
    private final AllocationSyncService allocationSyncService;

    @PaymentSign
    @PlatformInitContext(PaymentApiCode.SYNC_PAY)
    @Operation(summary = "支付同步接口")
    @PostMapping("/pay")
    public DaxResult<SyncResult> syncPay(@RequestBody PaySyncParam param){
        return DaxRes.ok(paySyncService.sync(param));
    }

    @PaymentSign
    @PlatformInitContext(PaymentApiCode.SYNC_REFUND)
    @Operation(summary = "退款同步接口")
    @PostMapping("/refund")
    public DaxResult<SyncResult> syncRefund(@RequestBody RefundSyncParam param){
        return DaxRes.ok(refundSyncService.sync(param));
    }


    @PaymentSign
    @PlatformInitContext(PaymentApiCode.SYNC_ALLOCATION)
    @Operation(summary = "分账同步接口")
    @PostMapping("/sync")
    public DaxResult<AllocationSyncResult> sync(@RequestBody AllocSyncParam param){
        return DaxRes.ok(allocationSyncService.sync(param));
    }
}
