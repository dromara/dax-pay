package cn.daxpay.open.payment.unipay.trade.controller;

import cn.daxpay.open.platform.core.annotation.IgnoreAuth;
import cn.daxpay.open.platform.core.enums.common.PaymentApiEnum;
import cn.daxpay.open.platform.core.exception.operation.OperationFailException;
import cn.daxpay.open.payment.merchant.service.permission.MerchantPermissionService;
import cn.daxpay.open.payment.unipay.param.trade.pay.NormalPayQueryParam;

import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.unipay.result.trade.pay.NormalPayOrderResult;
import cn.daxpay.open.payment.common.util.DaxRes;
import cn.daxpay.open.payment.unipay.aop.PaymentVerify;
import cn.daxpay.open.payment.unipay.trade.service.NormalPayOrderQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import cn.daxpay.open.platform.core.code.CommonCode;

/// # 统一查询接口
///
@PaymentVerify
@IgnoreAuth
@Tag(name = "统一查询接口")
@RestController
@RequestMapping("/unipay/query")
@RequiredArgsConstructor
public class UniQueryController {

    private final NormalPayOrderQueryService normalPayOrderQueryService;
    private final MerchantPermissionService permConfigService;

    @Operation(summary = "支付订单查询接口")
    @PostMapping("/pay-order")
    public DaxResult<NormalPayOrderResult> queryPayOrder(@RequestBody NormalPayQueryParam param){
        if (!permConfigService.hasApiPerm(PaymentApiEnum.PAY_ORDER.getCode())){
            // 订单: 该商户没有此接口的权限
            throw new OperationFailException(CommonCode.FAIL_CODE, "error.payment.order.merchantNoApiPermission");
        }
        return DaxRes.ok(normalPayOrderQueryService.queryPayOrder(param));
    }


}
