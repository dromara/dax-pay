package org.dromara.daxpay.payment.unipay.controller;

import org.dromara.daxpay.platform.core.annotation.IgnoreAuth;
import org.dromara.daxpay.platform.core.enums.common.PaymentApiEnum;
import org.dromara.daxpay.platform.core.exception.operation.OperationFailException;
import org.dromara.daxpay.payment.common.service.MerchantPermissionService;
import org.dromara.daxpay.payment.unipay.param.trade.pay.QueryPayParam;

import org.dromara.daxpay.payment.common.result.DaxResult;
import org.dromara.daxpay.payment.unipay.result.trade.pay.PayOrderResult;
import org.dromara.daxpay.payment.common.util.DaxRes;
import org.dromara.daxpay.payment.old.pay.anno.PaymentVerify;
import org.dromara.daxpay.payment.old.pay.service.order.pay.PayOrderQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.dromara.daxpay.platform.core.code.CommonCode;

/// # 统一查询接口
///
@PaymentVerify
@IgnoreAuth
@Tag(name = "统一查询接口")
@RestController
@RequestMapping("/unipay/query")
@RequiredArgsConstructor
public class UniQueryController {

    private final PayOrderQueryService payOrderQueryService;
    private final MerchantPermissionService permConfigService;

    @Operation(summary = "支付订单查询接口")
    @PostMapping("/pay-order")
    public DaxResult<PayOrderResult> queryPayOrder(@RequestBody QueryPayParam param){
        if (!permConfigService.hasApiPerm(PaymentApiEnum.PAY_ORDER.getCode())){
            // 订单: 该商户没有此接口的权限
            throw new OperationFailException(CommonCode.FAIL_CODE, "error.payment.order.merchantNoApiPermission");
        }
        return DaxRes.ok(payOrderQueryService.queryPayOrder(param));
    }


}
