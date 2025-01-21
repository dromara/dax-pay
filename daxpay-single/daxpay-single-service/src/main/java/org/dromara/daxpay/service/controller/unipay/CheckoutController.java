package org.dromara.daxpay.service.controller.unipay;

import cn.bootx.platform.core.annotation.IgnoreAuth;
import cn.bootx.platform.core.rest.Res;
import cn.bootx.platform.core.rest.result.Result;
import cn.bootx.platform.core.util.ValidationUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.dromara.daxpay.core.param.checkout.*;
import org.dromara.daxpay.core.result.DaxResult;
import org.dromara.daxpay.core.result.assist.AuthResult;
import org.dromara.daxpay.core.result.checkout.CheckoutAggregateOrderAndConfigResult;
import org.dromara.daxpay.core.result.checkout.CheckoutOrderAndConfigResult;
import org.dromara.daxpay.core.result.checkout.CheckoutUrlResult;
import org.dromara.daxpay.core.result.trade.pay.PayResult;
import org.dromara.daxpay.core.util.DaxRes;
import org.dromara.daxpay.service.common.anno.PaymentVerify;
import org.dromara.daxpay.service.service.checkout.CheckoutQueryService;
import org.dromara.daxpay.service.service.checkout.CheckoutService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 收银台服务
 * @author xxm
 * @since 2024/11/26
 */
@Validated
@IgnoreAuth
@Tag(name = "收银台服务")
@RestController
@RequestMapping("/unipay/checkout")
@RequiredArgsConstructor
public class CheckoutController {
    private final CheckoutService checkoutService;
    private final CheckoutQueryService checkoutQueryService;

    @PaymentVerify
    @Operation(summary = "创建一个收银台链接")
    @PostMapping("/create")
    public DaxResult<CheckoutUrlResult> create(@RequestBody CheckoutCreatParam checkoutParam){
        return DaxRes.ok(checkoutService.create(checkoutParam));
    }

    @Operation(summary = "根据订单号和收银台方式获取收银台链接")
    @GetMapping("/getCheckoutUrl")
    public Result<String> getCheckoutUrl(String orderNo, String checkoutType){
        return Res.ok(checkoutService.getCheckoutUrl(orderNo, checkoutType));

    }

    @Operation(summary = "获取收银台订单和配置信息")
    @GetMapping("/getOrderAndConfig")
    public Result<CheckoutOrderAndConfigResult> getOrderAndConfig(String orderNo, String checkoutType){
        return Res.ok(checkoutQueryService.getOrderAndConfig(orderNo, checkoutType));
    }

    @Operation(summary = "获取聚合支付配置")
    @GetMapping("/getAggregateConfig")
    public Result<CheckoutAggregateOrderAndConfigResult> getAggregateConfig(String orderNo, String aggregateType){
        return Res.ok(checkoutQueryService.getAggregateConfig(orderNo, aggregateType));
    }

    @Operation(summary = "获取收银台所需授权链接, 用于获取OpenId一类的信息")
    @PostMapping("/generateAuthUrl")
    public Result<String> generateAuthUrl(@RequestBody CheckoutAuthUrlParam param){
        ValidationUtil.validateParam(param);
        return Res.ok(checkoutService.generateAuthUrl(param));
    }

    @Operation(summary = "获取授权结果")
    @PostMapping("/auth")
    public Result<AuthResult> auth(@RequestBody CheckoutAuthCodeParam param){
        ValidationUtil.validateParam(param);
        return Res.ok(checkoutService.auth(param));
    }


    @Operation(summary = "发起支付(普通)")
    @PostMapping("/pay")
    public Result<PayResult> pay(@RequestBody CheckoutPayParam param){
        ValidationUtil.validateParam(param);
        return Res.ok(checkoutService.pay(param));
    }


    @Operation(summary = "发起支付(聚合扫码)")
    @PostMapping("/aggregatePay")
    public Result<PayResult> aggregatePay(@RequestBody CheckoutAggregatePayParam param){
        ValidationUtil.validateParam(param);
        return Res.ok(checkoutService.aggregatePay(param));
    }


    @Operation(summary = "发起支付(聚合条码)")
    @PostMapping("/aggregateBarPay")
    public Result<PayResult> aggregateBarPay(@RequestBody CheckoutAggregateBarPayParam param){
        ValidationUtil.validateParam(param);
        return Res.ok(checkoutService.aggregateBarPay(param));
    }


    @Operation(summary = "查询订单状态")
    @GetMapping("/findStatusByOrderNo")
    public Result<Boolean> findStatusByOrderNo(@NotBlank(message = "订单号不能为空") String orderNo){
        return Res.ok(checkoutQueryService.findStatusByOrderNo(orderNo));
    }


}
