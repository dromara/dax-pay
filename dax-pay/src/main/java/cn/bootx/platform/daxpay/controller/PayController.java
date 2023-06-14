package cn.bootx.platform.daxpay.controller;

import cn.bootx.platform.common.core.rest.Res;
import cn.bootx.platform.common.core.rest.ResResult;
import cn.bootx.platform.daxpay.core.pay.service.PayCancelService;
import cn.bootx.platform.daxpay.core.pay.service.PayRefundService;
import cn.bootx.platform.daxpay.core.pay.service.PayService;
import cn.bootx.platform.daxpay.core.pay.service.PaySyncService;
import cn.bootx.platform.daxpay.dto.pay.PayResult;
import cn.bootx.platform.daxpay.param.pay.PayParam;
import cn.bootx.platform.daxpay.param.refund.RefundParam;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xxm
 * @date 2020/12/9
 */
@Tag(name = "统一支付")
@RestController
@RequestMapping("/uniPay")
@AllArgsConstructor
public class PayController {

    private final PayService payService;

    private final PayCancelService payCancelService;

    private final PayRefundService payRefundService;

    private final PaySyncService paySyncService;

    @Operation(summary = "发起支付")
    @PostMapping("/pay")
    public ResResult<PayResult> pay(@RequestBody PayParam payParam) {
        return Res.ok(payService.pay(payParam));
    }

    @Operation(summary = "取消支付(支付id)")
    @PostMapping("/cancelByPaymentId")
    public ResResult<Void> cancelByPaymentId(@Parameter(description = "支付id") Long paymentId) {
        payCancelService.cancelByPaymentId(paymentId);
        return Res.ok();
    }

    @Operation(summary = "取消支付(业务id)")
    @PostMapping("/cancelByBusinessId")
    public ResResult<Void> cancelByBusinessId(@Parameter(description = "业务id") String businessId) {
        payCancelService.cancelByBusinessId(businessId);
        return Res.ok();
    }

    @Operation(summary = "刷新指定业务id的支付单状态")
    @PostMapping("/syncByBusinessId")
    public ResResult<Void> syncByBusinessId(@Parameter(description = "业务id") String businessId) {
        paySyncService.syncByBusinessId(businessId);
        return Res.ok();
    }

    @Operation(summary = "退款(支持部分退款)")
    @PostMapping("/refund")
    public ResResult<Void> refund(@RequestBody RefundParam refundParam) {
        payRefundService.refund(refundParam);
        return Res.ok();
    }

    @Operation(summary = "全额退款(业务id)")
    @PostMapping("/refundByBusinessId")
    public ResResult<Void> refundByBusinessId(@Parameter(description = "业务id") String businessId) {
        payRefundService.refundByBusinessId(businessId);
        return Res.ok();
    }

}
