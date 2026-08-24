package cn.daxpay.open.payment.unipay.trade.controller;

import cn.daxpay.open.platform.core.annotation.IgnoreAuth;
import cn.daxpay.open.payment.unipay.param.trade.pay.NormalPaySyncParam;
import cn.daxpay.open.payment.unipay.param.trade.refund.RefundSyncParam;
import cn.daxpay.open.payment.unipay.param.trade.alloc.AllocSyncParam;
import cn.daxpay.open.payment.unipay.param.trade.transfer.TransferSyncParam;

import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.unipay.result.trade.pay.NormalPaySyncResult;
import cn.daxpay.open.payment.unipay.result.trade.refund.RefundSyncResult;
import cn.daxpay.open.payment.unipay.result.trade.alloc.AllocSyncResult;
import cn.daxpay.open.payment.unipay.result.trade.transfer.TransferSyncResult;
import cn.daxpay.open.payment.common.util.DaxRes;
import cn.daxpay.open.payment.unipay.aop.PaymentVerify;
import cn.daxpay.open.payment.trade.runtime.service.sync.PaySyncService;
import cn.daxpay.open.payment.unipay.trade.service.RefundOrderSyncService;
import cn.daxpay.open.payment.unipay.trade.service.AllocOrderSyncService;
import cn.daxpay.open.payment.unipay.trade.service.TransferOrderSyncService;

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
    private final RefundOrderSyncService refundOrderSyncService;
    private final AllocOrderSyncService allocOrderSyncService;
    private final TransferOrderSyncService transferOrderSyncService;

    @Operation(summary = "支付订单同步接口")
    @PostMapping("/pay")
    public DaxResult<NormalPaySyncResult> pay(@RequestBody NormalPaySyncParam param){
        return DaxRes.ok(paySyncService.sync(param));
    }

    @Operation(summary = "退款订单同步接口")
    @PostMapping("/refund")
    public DaxResult<RefundSyncResult> refund(@RequestBody RefundSyncParam param){
        return DaxRes.ok(refundOrderSyncService.sync(param));
    }

    @Operation(summary = "分账订单同步接口")
    @PostMapping("/alloc")
    public DaxResult<AllocSyncResult> alloc(@RequestBody AllocSyncParam param){
        return DaxRes.ok(allocOrderSyncService.sync(param));
    }

    @Operation(summary = "转账订单同步接口")
    @PostMapping("/transfer")
    public DaxResult<TransferSyncResult> transfer(@RequestBody TransferSyncParam param){
        return DaxRes.ok(transferOrderSyncService.sync(param));
    }

}
