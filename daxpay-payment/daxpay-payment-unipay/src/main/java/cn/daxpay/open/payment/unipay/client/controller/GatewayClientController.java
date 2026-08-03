package cn.daxpay.open.payment.unipay.client.controller;

import cn.daxpay.open.payment.trade.runtime.service.pay.gateway.AggregatePayService;
import cn.daxpay.open.payment.trade.runtime.service.pay.gateway.CashierAuthService;
import cn.daxpay.open.payment.trade.runtime.service.pay.gateway.CashierPayService;
import cn.daxpay.open.payment.trade.runtime.service.pay.gateway.GatewayAuthService;
import cn.daxpay.open.payment.trade.runtime.service.pay.gateway.GatewayOrderQueryService;
import cn.daxpay.open.payment.trade.runtime.service.query.PayResultQueryService;
import cn.daxpay.open.payment.auth.UnifiedAuthService;
import cn.daxpay.open.payment.unipay.param.assist.AuthCodeParam;
import cn.daxpay.open.payment.unipay.param.gateway.AggregateQrPayParam;
import cn.daxpay.open.payment.unipay.param.gateway.CashierAuthParam;
import cn.daxpay.open.payment.unipay.param.gateway.CashierPayParam;
import cn.daxpay.open.payment.unipay.param.gateway.GatewayAuthUrlParam;
import cn.daxpay.open.payment.unipay.result.assist.AuthResult;
import cn.daxpay.open.payment.unipay.result.assist.AuthUrlResult;
import cn.daxpay.open.payment.unipay.result.gateway.AggregatePayMetaResult;
import cn.daxpay.open.payment.unipay.result.gateway.CashierItemPublicResult;
import cn.daxpay.open.payment.unipay.result.gateway.GatewayOrderResult;
import cn.daxpay.open.payment.unipay.result.trade.PayResultResult;
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
    private final PayResultQueryService payResultQueryService;
    private final AggregatePayService aggregatePayService;
    private final CashierPayService cashierPayService;
    private final CashierAuthService cashierAuthService;
    private final GatewayAuthService gatewayAuthService;
    private final UnifiedAuthService unifiedAuthService;

    @Operation(summary = "查询网关订单摘要")
    @GetMapping("/order")
    public Result<GatewayOrderResult> getOrder(
            @NotBlank(message = "{validation.field.orderNo.notBlank}") String orderNo) {
        return Res.ok(gatewayOrderQueryService.queryByOrderNoNotTenant(orderNo));
    }

    @Operation(summary = "查询支付结果")
    @GetMapping("/pay-result")
    public Result<PayResultResult> getPayResult(
            @NotBlank(message = "{validation.field.tradeNo.notBlank}") String tradeNo) {
        return Res.ok(payResultQueryService.queryByTradeNo(tradeNo));
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

    /// 收银台小程序认证: 用 authCode 换 openId/userId
    ///
    /// 微信/抖音/支付宝小程序前端通过 uni.login / tt.login / my.getAuthCode 获取授权码后,
    /// 同步调用此端点换取用户标识。无需 H5 OAuth 跳转, 无商户签名。
    @Operation(summary = "收银台小程序认证(换openId/userId)")
    @PostMapping("/cashier/auth")
    public Result<AuthResult> cashierAuth(@RequestBody @Validated CashierAuthParam param) {
        return Res.ok(cashierAuthService.auth(param));
    }

    @Operation(summary = "网关H5生成授权链接")
    @PostMapping("/auth/generate-url")
    public Result<AuthUrlResult> generateAuthUrl(@RequestBody @Validated GatewayAuthUrlParam param) {
        return Res.ok(gatewayAuthService.generateAuthUrl(param));
    }

    /// OAuth 回调: 用 authCode 换取 openId/userId
    ///
    /// H5 授权重定向落地页(如 AuthCallback.vue)回调此端点, 用第三方 OAuth code 换取用户标识,
    /// 成功后返回 AuthResult(含 openId + returnPath), 前端据此跳回业务页。
    @Operation(summary = "OAuth认证回调")
    @PostMapping("/auth/callback")
    public Result<AuthResult> authCallback(@RequestBody AuthCodeParam param) {
        return Res.ok(unifiedAuthService.auth(param));
    }
}
