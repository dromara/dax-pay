package cn.daxpay.open.payment.unipay.trade.controller;

import cn.daxpay.open.payment.unipay.aop.PaymentVerify;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.common.util.DaxRes;
import cn.daxpay.open.payment.trade.runtime.service.pay.gateway.GatewayOrderQueryService;
import cn.daxpay.open.payment.trade.runtime.service.pay.gateway.GatewayPayAssistService;
import cn.daxpay.open.payment.unipay.param.gateway.GatewayOrderQueryParam;
import cn.daxpay.open.payment.unipay.param.gateway.GatewayPrePayParam;
import cn.daxpay.open.payment.unipay.result.gateway.GatewayOrderResult;
import cn.daxpay.open.payment.unipay.result.gateway.GatewayPrePayResult;
import cn.daxpay.open.platform.core.annotation.IgnoreAuth;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/// # 网关支付开放接口
///
/// 商户签名 API（`@PaymentVerify`），与 [UniTradeController] 等同属 trade 开放层；
/// 产品语义「网关支付」体现在 URL `/unipay/gateway` 与 param/result.gateway，而非独立 controller 包。
@PaymentVerify
@IgnoreAuth
@Tag(name = "网关支付接口")
@RestController
@RequestMapping("/unipay/gateway")
@RequiredArgsConstructor
public class UniGatewayPayController {

    private final GatewayPayAssistService gatewayPayAssistService;
    private final GatewayOrderQueryService gatewayOrderQueryService;

    @Operation(summary = "网关预下单")
    @PostMapping("/pre-pay")
    public DaxResult<GatewayPrePayResult> prePay(@RequestBody GatewayPrePayParam param) {
        return DaxRes.ok(gatewayPayAssistService.prePay(param));
    }

    @Operation(summary = "网关订单查询")
    @PostMapping("/query")
    public DaxResult<GatewayOrderResult> query(@RequestBody GatewayOrderQueryParam param) {
        return DaxRes.ok(gatewayOrderQueryService.query(param));
    }
}
