package cn.daxpay.single.gateway.controller;

import cn.bootx.platform.common.core.annotation.IgnoreAuth;
import cn.daxpay.single.code.PaymentApiCode;
import cn.daxpay.single.param.payment.pay.PayCloseParam;
import cn.daxpay.single.param.payment.pay.PayParam;
import cn.daxpay.single.param.payment.refund.RefundParam;
import cn.daxpay.single.param.payment.transfer.TransferParam;
import cn.daxpay.single.result.DaxResult;
import cn.daxpay.single.result.pay.PayCloseResult;
import cn.daxpay.single.result.pay.PayResult;
import cn.daxpay.single.result.pay.RefundResult;
import cn.daxpay.single.service.annotation.PaymentSign;
import cn.daxpay.single.service.annotation.InitPaymentContext;
import cn.daxpay.single.service.core.payment.close.service.PayCloseService;
import cn.daxpay.single.service.core.payment.pay.service.PayService;
import cn.daxpay.single.service.core.payment.refund.service.RefundService;
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
    private final PayCloseService payCloseService;

    @PaymentSign
    @InitPaymentContext(PaymentApiCode.PAY)
    @Operation(summary = "统一支付接口")
    @PostMapping("/pay")
    public DaxResult<PayResult> pay(@RequestBody PayParam payParam){
        return DaxRes.ok(payService.pay(payParam));
    }

    @PaymentSign
    @InitPaymentContext(PaymentApiCode.CLOSE)
    @Operation(summary = "支付关闭接口")
    @PostMapping("/close")
    public DaxResult<PayCloseResult> close(@RequestBody PayCloseParam param){
        return DaxRes.ok(payCloseService.close(param));
    }

    @PaymentSign
    @InitPaymentContext(PaymentApiCode.REFUND)
    @Operation(summary = "统一退款接口")
    @PostMapping("/refund")
    public DaxResult<RefundResult> refund(@RequestBody RefundParam param){
        return DaxRes.ok(refundService.refund(param));
    }

    @PaymentSign
    @InitPaymentContext(PaymentApiCode.TRANSFER)
    @Operation(summary = "统一转账接口")
    @PostMapping("/transfer")
    public DaxResult<Void> transfer(@RequestBody TransferParam param){
        return DaxRes.ok();
    }

}
