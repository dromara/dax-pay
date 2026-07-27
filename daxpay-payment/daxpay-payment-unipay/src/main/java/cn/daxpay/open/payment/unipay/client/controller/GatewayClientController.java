package cn.daxpay.open.payment.unipay.client.controller;

import cn.daxpay.open.payment.trade.runtime.service.pay.gateway.AggregatePayService;
import cn.daxpay.open.payment.trade.runtime.service.pay.gateway.CashierPayService;
import cn.daxpay.open.payment.trade.runtime.service.pay.gateway.GatewayAuthService;
import cn.daxpay.open.payment.trade.runtime.service.pay.gateway.GatewayOrderQueryService;
import cn.daxpay.open.payment.unipay.param.gateway.AggregateQrPayParam;
import cn.daxpay.open.payment.unipay.param.gateway.CashierPayParam;
import cn.daxpay.open.payment.unipay.param.gateway.GatewayAuthUrlParam;
import cn.daxpay.open.payment.unipay.result.assist.AuthUrlResult;
import cn.daxpay.open.payment.unipay.result.gateway.AggregatePayMetaResult;
import cn.daxpay.open.payment.unipay.result.gateway.CashierItemPublicResult;
import cn.daxpay.open.payment.unipay.result.gateway.GatewayOrderResult;
import cn.daxpay.open.payment.unipay.result.trade.pay.NormalPayResult;
import cn.daxpay.open.platform.core.annotation.IgnoreAuth;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/// # 网关 H5 侧接口(无商户签名)
///
/// 与商户签名面 [cn.daxpay.open.payment.unipay.trade.controller.UniGatewayPayController]
/// 共用前缀 `/unipay/gateway`，本类不加 `@PaymentVerify`，响应为平台 [Result] 而非 [cn.daxpay.open.payment.common.result.DaxResult]。
/// 不使用类级 `@IgnoreTenant`。引导读订单在 Service/Manager 的 `*NotTenant` 方法内完成，
/// 装载 mchNo 后后续配置/交易查询走正常租户过滤。
@IgnoreAuth
@Validated
@Tag(name = "网关支付(H5)")
@RestController
@RequestMapping("/unipay/gateway")
@RequiredArgsConstructor
public class GatewayClientController {

    private final GatewayOrderQueryService gatewayOrderQueryService;
    private final AggregatePayService aggregatePayService;
    private final CashierPayService cashierPayService;
    private final GatewayAuthService gatewayAuthService;

    @Operation(summary = "查询网关订单摘要")
    @GetMapping("/order")
    public Result<GatewayOrderResult> getOrder(
            @NotBlank(message = "{validation.field.orderNo.notBlank}") String orderNo) {
        return Res.ok(gatewayOrderQueryService.queryByOrderNoNotTenant(orderNo));
    }

    @Operation(summary = "聚合扫码元数据(autoLaunch/needOpenId)")
    @GetMapping("/aggregate/meta")
    public Result<AggregatePayMetaResult> aggregateMeta(
            @NotBlank(message = "{validation.field.orderNo.notBlank}") String orderNo,
            @NotBlank(message = "{validation.field.clientEnv.notBlank}") String clientEnv,
            String runtime) {
        return Res.ok(aggregatePayService.getMeta(orderNo, clientEnv, runtime));
    }

    @Operation(summary = "聚合扫码发起支付")
    @PostMapping("/aggregate/pay")
    public Result<NormalPayResult> aggregatePay(@RequestBody @Validated AggregateQrPayParam param) {
        return Res.ok(aggregatePayService.aggregateQrPay(param));
    }

    @Operation(summary = "收银台支付项列表")
    @GetMapping("/cashier/items")
    public Result<List<CashierItemPublicResult>> listCashierItems(
            @NotBlank(message = "{validation.field.orderNo.notBlank}") String orderNo,
            @NotBlank(message = "{validation.field.cashierType.notBlank}") String cashierType,
            String clientEnv) {
        return Res.ok(cashierPayService.listPublicItems(orderNo, cashierType, clientEnv));
    }

    @Operation(summary = "收银台发起支付")
    @PostMapping("/cashier/pay")
    public Result<NormalPayResult> cashierPay(@RequestBody @Validated CashierPayParam param) {
        return Res.ok(cashierPayService.pay(param));
    }

    @Operation(summary = "网关H5生成授权链接")
    @PostMapping("/auth/generate-url")
    public Result<AuthUrlResult> generateAuthUrl(@RequestBody @Validated GatewayAuthUrlParam param) {
        return Res.ok(gatewayAuthService.generateAuthUrl(param));
    }
}
