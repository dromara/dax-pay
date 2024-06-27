package cn.daxpay.single.gateway.controller;

import cn.bootx.platform.common.core.annotation.IgnoreAuth;
import cn.daxpay.single.core.code.PaymentApiCode;
import cn.daxpay.single.core.param.payment.pay.PayCancelParam;
import cn.daxpay.single.core.param.payment.pay.PayCloseParam;
import cn.daxpay.single.core.param.payment.pay.PayParam;
import cn.daxpay.single.core.param.payment.refund.RefundParam;
import cn.daxpay.single.core.param.payment.transfer.TransferParam;
import cn.daxpay.single.core.result.DaxResult;
import cn.daxpay.single.core.result.pay.PayCancelResult;
import cn.daxpay.single.core.result.pay.PayCloseResult;
import cn.daxpay.single.core.result.pay.PayResult;
import cn.daxpay.single.core.result.pay.RefundResult;
import cn.daxpay.single.core.result.transfer.TransferResult;
import cn.daxpay.single.service.annotation.InitPaymentContext;
import cn.daxpay.single.service.annotation.PaymentVerify;
import cn.daxpay.single.service.core.payment.cancel.service.PayCancelService;
import cn.daxpay.single.service.core.payment.close.service.PayCloseService;
import cn.daxpay.single.service.core.payment.pay.service.PayService;
import cn.daxpay.single.service.core.payment.refund.service.RefundService;
import cn.daxpay.single.service.core.payment.transfer.service.TransferService;
import cn.daxpay.single.core.util.DaxRes;
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
    private final PayCloseService payCloseService;
    private final PayCancelService payCancelService;
    private final TransferService transferService;

    @PaymentVerify
    @InitPaymentContext(PaymentApiCode.PAY)
    @Operation(summary = "统一支付接口")
    @PostMapping("/pay")
    public DaxResult<PayResult> pay(@RequestBody PayParam payParam){
        return DaxRes.ok(payService.pay(payParam));
    }

    @PaymentVerify
    @InitPaymentContext(PaymentApiCode.CLOSE)
    @Operation(summary = "支付关闭接口")
    @PostMapping("/close")
    public DaxResult<PayCloseResult> close(@RequestBody PayCloseParam param){
        return DaxRes.ok(payCloseService.close(param));
    }

    @PaymentVerify
    @InitPaymentContext(PaymentApiCode.CANCEL)
    @Operation(summary = "支付撤销接口")
    @PostMapping("/cancel")
    public DaxResult<PayCancelResult> cancel(@RequestBody PayCancelParam param){
        return DaxRes.ok(payCancelService.cancel(param));
    }

    @PaymentVerify
    @InitPaymentContext(PaymentApiCode.REFUND)
    @Operation(summary = "统一退款接口")
    @PostMapping("/refund")
    public DaxResult<RefundResult> refund(@RequestBody RefundParam param){
        return DaxRes.ok(refundService.refund(param));
    }

    @PaymentVerify
    @InitPaymentContext(PaymentApiCode.TRANSFER)
    @Operation(summary = "统一转账接口")
    @PostMapping("/transfer")
    public DaxResult<TransferResult> transfer(@RequestBody TransferParam param){
        return DaxRes.ok(transferService.transfer(param));
    }

}
