package org.dromara.daxpay.controller.gateway;

import cn.bootx.platform.core.annotation.IgnoreAuth;
import cn.bootx.platform.core.rest.Res;
import cn.bootx.platform.core.rest.result.Result;
import cn.bootx.platform.core.util.ValidationUtil;
import org.dromara.daxpay.core.param.aggregate.AggregateBarPayParam;
import org.dromara.daxpay.core.param.aggregate.AggregatePayParam;
import org.dromara.daxpay.core.param.gateway.GatewayAuthCodeParam;
import org.dromara.daxpay.core.param.gateway.GatewayAuthUrlParam;
import org.dromara.daxpay.core.param.gateway.GatewayOrderAndConfigParam;
import org.dromara.daxpay.core.param.gateway.GatewayPayParam;
import org.dromara.daxpay.core.result.DaxResult;
import org.dromara.daxpay.core.result.assist.AuthResult;
import org.dromara.daxpay.core.result.trade.pay.PayResult;
import org.dromara.daxpay.core.util.DaxRes;
import org.dromara.daxpay.service.common.anno.PaymentVerify;
import org.dromara.daxpay.service.result.gateway.AggregateOrderAndConfigResult;
import org.dromara.daxpay.service.result.gateway.GatewayOrderAndConfigResult;
import org.dromara.daxpay.service.result.gateway.GatewayOrderResult;
import org.dromara.daxpay.service.result.gateway.GatewayPayUrlResult;
import org.dromara.daxpay.service.service.gateway.GatewayPayQueryService;
import org.dromara.daxpay.service.service.gateway.GatewayPayService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 网关支付服务
 * @author xxm
 * @since 2024/11/26
 */
@Validated
@IgnoreAuth
@Tag(name = "网关支付服务")
@RestController
@RequestMapping("/unipay/gateway")
@RequiredArgsConstructor
public class GatewayPayController {
    private final GatewayPayService gatewayPayService;
    private final GatewayPayQueryService checkoutQueryService;

    @PaymentVerify
    @Operation(summary = "创建一个网关支付链接")
    @PostMapping("/prePay")
    public DaxResult<GatewayPayUrlResult> prePay(@RequestBody GatewayPayParam checkoutParam){
        // 初始化
        return DaxRes.ok(gatewayPayService.prePay(checkoutParam));
    }

    @PaymentVerify
    @Operation(summary = "发起支付(聚合付款码)")
    @PostMapping("/aggregateBarPay")
    public DaxResult<PayResult> aggregateBarPay(@RequestBody AggregateBarPayParam param){
        return DaxRes.ok(gatewayPayService.aggregateBarPay(param));
    }

    @Operation(summary = "发起支付(聚合扫码/浏览器访问)")
    @PostMapping("/aggregatePay")
    public Result<PayResult> aggregatePay(@RequestBody AggregatePayParam param){
        ValidationUtil.validateParam(param);
        return Res.ok(gatewayPayService.aggregatePay(param));
    }

    @Operation(summary = "获取收银台配置和支付订单信息")
    @GetMapping("/getOrderAndConfig")
    public Result<GatewayOrderAndConfigResult> getOrderAndConfig(@Validated GatewayOrderAndConfigParam param){
        return Res.ok(checkoutQueryService.getOrderAndConfig(param));
    }

    @Operation(summary = "获取聚合支付配置支付订单信息")
    @GetMapping("/getAggregateConfig")
    public Result<AggregateOrderAndConfigResult> getAggregateConfig(
            @NotBlank(message = "订单号不能为空") @Parameter(description = "订单号") String orderNo,
            @NotBlank(message = "聚合支付类型不能为空") @Parameter(description = "聚合支付类型")String aggregateType){
        return Res.ok(checkoutQueryService.getAggregateConfig(orderNo, aggregateType));
    }

    @Operation(summary = "获取网关支付所需授权链接, 用于获取OpenId一类的信息")
    @PostMapping("/generateAuthUrl")
    public Result<String> generateAuthUrl(@RequestBody @Validated GatewayAuthUrlParam param){
        ValidationUtil.validateParam(param);
        return Res.ok(gatewayPayService.genAuthUrl(param));
    }

    @Operation(summary = "获取网关支付授权结果")
    @PostMapping("/auth")
    public Result<AuthResult> auth(@RequestBody GatewayAuthCodeParam param){
        ValidationUtil.validateParam(param);
        return Res.ok(gatewayPayService.auth(param));
    }

    @Operation(summary = "查询订单状态")
    @GetMapping("/findStatusByOrderNo")
    public Result<Boolean> findStatusByOrderNo(@NotBlank(message = "订单号不能为空") String orderNo){
        return Res.ok(checkoutQueryService.findStatusByOrderNo(orderNo));
    }

    @Operation(summary = "查询订单信息")
    @GetMapping("/findOrderByOrderNo")
    public Result<GatewayOrderResult> findOrderByOrderNo(@NotBlank(message = "订单号不能为空") String orderNo){
        return Res.ok(checkoutQueryService.findOrderByOrderNo(orderNo));
    }
}
