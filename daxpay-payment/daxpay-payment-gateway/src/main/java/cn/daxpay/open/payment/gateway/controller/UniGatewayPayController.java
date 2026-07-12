package cn.daxpay.open.payment.gateway.controller;

import cn.daxpay.open.payment.unipay.aop.PaymentVerify;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.common.util.DaxRes;
import cn.daxpay.open.payment.gateway.service.GatewayOrderQueryService;
import cn.daxpay.open.payment.gateway.service.GatewayPayAssistService;
import cn.daxpay.open.payment.merchant.service.permission.MerchantPermissionService;
import cn.daxpay.open.payment.gateway.param.GatewayOrderQueryParam;
import cn.daxpay.open.payment.gateway.param.GatewayPrePayParam;
import cn.daxpay.open.payment.gateway.result.GatewayOrderResult;
import cn.daxpay.open.payment.gateway.result.GatewayPrePayResult;
import cn.daxpay.open.platform.core.annotation.IgnoreAuth;
import cn.daxpay.open.platform.core.code.CommonCode;
import cn.daxpay.open.platform.core.enums.common.PaymentApiEnum;
import cn.daxpay.open.platform.core.exception.operation.OperationFailException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/// # 网关支付开放接口
@PaymentVerify
@IgnoreAuth
@Tag(name = "网关支付接口")
@RestController
@RequestMapping("/unipay/gateway")
@RequiredArgsConstructor
public class UniGatewayPayController {

    private final GatewayPayAssistService gatewayPayAssistService;
    private final GatewayOrderQueryService gatewayOrderQueryService;
    private final MerchantPermissionService permConfigService;

    @Operation(summary = "网关预下单")
    @PostMapping("/pre-pay")
    public DaxResult<GatewayPrePayResult> prePay(@RequestBody GatewayPrePayParam param) {
        if (!permConfigService.hasApiPerm(PaymentApiEnum.PRE_PAY.getCode())) {
            throw new OperationFailException(CommonCode.FAIL_CODE, "error.payment.order.merchantNoApiPermission");
        }
        return DaxRes.ok(gatewayPayAssistService.prePay(param));
    }

    @Operation(summary = "网关订单查询")
    @PostMapping("/query")
    public DaxResult<GatewayOrderResult> query(@RequestBody GatewayOrderQueryParam param) {
        return DaxRes.ok(gatewayOrderQueryService.query(param));
    }
}
