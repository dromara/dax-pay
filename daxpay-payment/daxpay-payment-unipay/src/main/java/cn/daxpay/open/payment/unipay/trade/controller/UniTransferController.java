package cn.daxpay.open.payment.unipay.trade.controller;

import cn.daxpay.open.platform.core.annotation.IgnoreAuth;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.common.util.DaxRes;
import cn.daxpay.open.payment.unipay.aop.PaymentVerify;
import cn.daxpay.open.payment.unipay.param.trade.transfer.TransferParam;
import cn.daxpay.open.payment.unipay.result.trade.transfer.TransferCreateResult;
import cn.daxpay.open.payment.unipay.trade.service.TransferOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/// # 统一转账接口
///
@PaymentVerify
@IgnoreAuth
@Tag(name = "统一转账接口")
@RestController
@RequestMapping("/unipay")
@RequiredArgsConstructor
public class UniTransferController {

    private final TransferOrderService transferOrderService;

    @Operation(summary = "转账接口")
    @PostMapping("/transfer")
    public DaxResult<TransferCreateResult> transfer(@RequestBody TransferParam param){
        return DaxRes.ok(transferOrderService.create(param));
    }

}
