package cn.daxpay.single.gateway.controller;

import cn.bootx.platform.common.core.annotation.IgnoreAuth;
import cn.daxpay.single.code.PaymentApiCode;
import cn.daxpay.single.param.payment.allocation.AllocationFinishParam;
import cn.daxpay.single.param.payment.allocation.AllocationStartParam;
import cn.daxpay.single.param.payment.pay.PayCloseParam;
import cn.daxpay.single.param.payment.pay.PayParam;
import cn.daxpay.single.param.payment.pay.PaySyncParam;
import cn.daxpay.single.param.payment.refund.RefundParam;
import cn.daxpay.single.param.payment.refund.RefundSyncParam;
import cn.daxpay.single.result.DaxResult;
import cn.daxpay.single.result.allocation.AllocationResult;
import cn.daxpay.single.result.pay.PayCloseResult;
import cn.daxpay.single.result.pay.PayResult;
import cn.daxpay.single.result.pay.RefundResult;
import cn.daxpay.single.result.pay.SyncResult;
import cn.daxpay.single.service.annotation.PaymentApi;
import cn.daxpay.single.service.core.payment.allocation.service.AllocationService;
import cn.daxpay.single.service.core.payment.close.service.PayCloseService;
import cn.daxpay.single.service.core.payment.pay.service.PayService;
import cn.daxpay.single.service.core.payment.refund.service.RefundService;
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
 * 统一支付接口
 * @author xxm
 * @since 2023/12/15
 */
@IgnoreAuth
@Tag(name = "统一支付接口")
@RestController
@RequestMapping("/unipay")
@RequiredArgsConstructor
public class UniPayController {
    private final PayService payService;
    private final RefundService refundService;
    private final PaySyncService paySyncService;
    private final PayCloseService payCloseService;
    private final RefundSyncService refundSyncService;
    private final AllocationService allocationService;


    @PaymentApi(PaymentApiCode.PAY)
    @Operation(summary = "统一支付接口")
    @PostMapping("/pay")
    public DaxResult<PayResult> pay(@RequestBody PayParam payParam){
        return DaxRes.ok(payService.pay(payParam));
    }

    @PaymentApi(PaymentApiCode.CLOSE)
    @Operation(summary = "支付关闭接口")
    @PostMapping("/close")
    public DaxResult<PayCloseResult> close(@RequestBody PayCloseParam param){
        return DaxRes.ok(payCloseService.close(param));
    }

    @PaymentApi(PaymentApiCode.REFUND)
    @Operation(summary = "统一退款接口")
    @PostMapping("/refund")
    public DaxResult<RefundResult> refund(@RequestBody RefundParam param){
        return DaxRes.ok(refundService.refund(param));
    }

    @PaymentApi(PaymentApiCode.SYNC_PAY)
    @Operation(summary = "支付同步接口")
    @PostMapping("/syncPay")
    public DaxResult<SyncResult> syncPay(@RequestBody PaySyncParam param){
        return DaxRes.ok(paySyncService.sync(param));
    }

    @PaymentApi(PaymentApiCode.SYNC_REFUND)
    @Operation(summary = "退款同步接口")
    @PostMapping("/syncRefund")
    public DaxResult<SyncResult> syncRefund(@RequestBody RefundSyncParam param){
        return DaxRes.ok(refundSyncService.sync(param));
    }

    @PaymentApi(PaymentApiCode.ALLOCATION)
    @Operation(summary = "开启分账接口")
    @PostMapping("/allocation")
    public DaxResult<AllocationResult> allocation(@RequestBody AllocationStartParam param){
        return DaxRes.ok(allocationService.allocation(param));
    }

    @PaymentApi(PaymentApiCode.ALLOCATION_FINISH)
    @Operation(summary = "分账完结接口")
    @PostMapping("/allocationFinish")
    public DaxResult<Void> allocationFinish(@RequestBody AllocationFinishParam param){
        allocationService.finish(param);
        return DaxRes.ok();
    }



}
