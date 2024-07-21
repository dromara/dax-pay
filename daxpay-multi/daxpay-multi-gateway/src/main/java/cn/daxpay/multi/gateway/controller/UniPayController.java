package cn.daxpay.multi.gateway.controller;

import cn.bootx.platform.core.annotation.IgnoreAuth;
import cn.daxpay.multi.core.param.trade.refund.RefundParam;
import cn.daxpay.multi.core.result.trade.RefundResult;
import cn.daxpay.multi.service.common.anno.PaymentVerify;
import cn.daxpay.multi.core.param.trade.pay.PayParam;
import cn.daxpay.multi.core.result.DaxResult;
import cn.daxpay.multi.core.result.trade.PayResult;
import cn.daxpay.multi.core.util.DaxRes;
import cn.daxpay.multi.service.service.trade.pay.PayService;
import cn.daxpay.multi.service.service.trade.refund.RefundService;
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
 * @since 2024/6/4
 */
@IgnoreAuth
@Tag(name = "统一支付接口")
@PaymentVerify // 所有接口都属于支付接口
@RestController
@RequestMapping("/unipay")
@RequiredArgsConstructor
public class UniPayController {
    private final PayService payService;
    private final RefundService refundService;

    @Operation(summary = "支付接口")
    @PostMapping("/pay")
    public DaxResult<PayResult> pay(@RequestBody PayParam payParam){
        return DaxRes.ok(payService.pay(payParam));
    }

    @Operation(summary = "退款接口")
    @PostMapping("/refund")
    public DaxResult<RefundResult> refund(@RequestBody RefundParam payParam){
        return DaxRes.ok(refundService.refund(payParam));
    }

    @Operation(summary = "关闭接口")
    @PostMapping("/close")
    public DaxResult<Void> reconcile(){
        return DaxRes.ok();
    }

    @Operation(summary = "撤销接口")
    @PostMapping("/cancel")
    public DaxResult<Void> cancel(){
        return DaxRes.ok();
    }

    @Operation(summary = "转账接口")
    @PostMapping("/transfer")
    public DaxResult<Void> transfer(){
        return DaxRes.ok();
    }
}
