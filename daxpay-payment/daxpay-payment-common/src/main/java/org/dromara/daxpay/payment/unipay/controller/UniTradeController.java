package org.dromara.daxpay.payment.unipay.controller;

import org.dromara.daxpay.platform.core.annotation.IgnoreAuth;
import org.dromara.daxpay.platform.core.enums.common.PaymentApiEnum;
import org.dromara.daxpay.platform.core.exception.operation.OperationFailException;
import org.dromara.daxpay.platform.core.code.CommonCode;
import org.dromara.daxpay.payment.common.result.DaxResult;
import org.dromara.daxpay.payment.common.util.DaxRes;
import org.dromara.daxpay.payment.common.service.MerchantPermissionService;
import org.dromara.daxpay.payment.old.pay.anno.PaymentVerify;
import org.dromara.daxpay.payment.pay.service.PayCloseService;
import org.dromara.daxpay.payment.pay.service.PayService;
import org.dromara.daxpay.payment.unipay.param.trade.pay.PayCloseParam;
import org.dromara.daxpay.payment.unipay.param.trade.pay.PayParam;
import org.dromara.daxpay.payment.unipay.result.trade.pay.PayResult;
import org.dromara.daxpay.platform.core.enums.pay.trade.TradeSourceEnum;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/// # 统一支付接口
///
@PaymentVerify
@IgnoreAuth
@Tag(name = "统一交易接口")
@RestController
@RequestMapping("/unipay")
@RequiredArgsConstructor
public class UniTradeController {
    private final PayService payService;
    private final PayCloseService payCloseService;
    private final MerchantPermissionService permConfigService;

    @Operation(summary = "支付接口")
    @PostMapping("/pay")
    public DaxResult<PayResult> pay(@RequestBody PayParam payParam){
        if (!permConfigService.hasApiPerm(PaymentApiEnum.PAY.getCode())){
            // 订单: 该商户没有此接口的权限
            throw new OperationFailException(CommonCode.FAIL_CODE, "error.payment.order.merchantNoApiPermission");
        }
        return DaxRes.ok(payService.pay(payParam));
    }

    @Operation(summary = "关闭和撤销接口")
    @PostMapping("/close")
    public DaxResult<Void> close(@RequestBody PayCloseParam param){
        if ( permConfigService.hasApiPerm(PaymentApiEnum.CLOSE.getCode())){
            // 订单: 该商户没有此接口的权限
            throw new OperationFailException(CommonCode.FAIL_CODE, "error.payment.order.merchantNoApiPermission");
        }
        payCloseService.close(param);
        return DaxRes.ok();
    }

}
