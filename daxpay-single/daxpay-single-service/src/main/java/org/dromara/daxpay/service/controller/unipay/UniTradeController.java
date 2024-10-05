package org.dromara.daxpay.service.controller.unipay;

import cn.bootx.platform.core.annotation.IgnoreAuth;
import org.dromara.daxpay.core.param.trade.pay.PayCloseParam;
import org.dromara.daxpay.core.param.trade.pay.PayParam;
import org.dromara.daxpay.core.param.trade.refund.RefundParam;
import org.dromara.daxpay.core.param.trade.transfer.TransferParam;
import org.dromara.daxpay.core.result.DaxResult;
import org.dromara.daxpay.core.result.trade.pay.PayResult;
import org.dromara.daxpay.core.result.trade.refund.RefundResult;
import org.dromara.daxpay.core.result.trade.transfer.TransferResult;
import org.dromara.daxpay.core.util.DaxRes;
import org.dromara.daxpay.service.common.anno.PaymentVerify;
import org.dromara.daxpay.service.service.trade.pay.PayCloseService;
import org.dromara.daxpay.service.service.trade.pay.PayService;
import org.dromara.daxpay.service.service.trade.refund.RefundService;
import org.dromara.daxpay.service.service.trade.transfer.TransferService;
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
@Tag(name = "统一交易接口")
@PaymentVerify // 所有接口都属于支付接口
@RestController
@RequestMapping("/unipay")
@RequiredArgsConstructor
public class UniTradeController {
    private final PayService payService;
    private final RefundService refundService;
    private final PayCloseService payCloseService;
    private final TransferService transferService;

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

    @Operation(summary = "关闭和撤销接口")
    @PostMapping("/close")
    public DaxResult<Void> reconcile(@RequestBody PayCloseParam param){
        payCloseService.close(param);
        return DaxRes.ok();
    }

    @Operation(summary = "转账接口")
    @PostMapping("/transfer")
    public DaxResult<TransferResult> transfer(@RequestBody TransferParam transferParam){
        return DaxRes.ok(transferService.transfer(transferParam));
    }
}
