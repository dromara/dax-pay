package org.dromara.daxpay.payment.unipay.controller;

import cn.bootx.platform.core.annotation.IgnoreAuth;
import cn.bootx.platform.core.rest.Res;
import cn.bootx.platform.core.rest.result.Result;
import cn.bootx.platform.core.util.ValidationUtil;
import org.dromara.daxpay.payment.pay.result.gateway.CashierCodePayInfoResult;
import org.dromara.daxpay.payment.unipay.param.gateway.CashierCodeAuthParam;
import org.dromara.daxpay.payment.unipay.param.gateway.CashierCodePayParam;
import org.dromara.daxpay.payment.unipay.result.assist.AuthResult;
import org.dromara.daxpay.payment.unipay.result.trade.pay.PayResult;
import org.dromara.daxpay.payment.unipay.service.gateway.CashierCodePayService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 码牌收银服务
 * @author xxm
 * @since 2025/10/10
 */
@Validated
@IgnoreAuth
@Tag(name = "码牌收银服务")
@RestController
@RequestMapping("/unipay/gateway/cashier/code")
@RequiredArgsConstructor
public class CashierCodePayController {
    private final CashierCodePayService cashierCodePayService;

    @Operation(summary = "获取配置和支付订单信息")
    @GetMapping("/getCodeConfig")
    public Result<CashierCodePayInfoResult> getCashierCodeConfig(
            @NotBlank(message = "码牌编码不能为空") @Parameter(description = "码牌编码") String code,
            @NotBlank(message = "支付场景不能为空") @Parameter(description = "支付场景")String scene){
        return Res.ok(cashierCodePayService.getCashierCodeConfig(code, scene));
    }

    @Operation(summary = "获取用于获取OpenId的授权链接")
    @PostMapping("/generateAuthUrl")
    public Result<String> generateAuthUrl(
            @NotBlank(message = "码牌编码不能为空") @Parameter(description = "码牌编码") String code,
            @NotBlank(message = "支付场景不能为空") @Parameter(description = "支付场景")String scene){
        return Res.ok(cashierCodePayService.genAuthUrl(code, scene));
    }

    @Operation(summary = "发起支付")
    @PostMapping("/pay")
    public Result<PayResult> aggregateBarPay(@RequestBody @Validated CashierCodePayParam param){
        return Res.ok(cashierCodePayService.cashierCodePay(param));
    }

    @Operation(summary = "获取网关支付授权结果")
    @PostMapping("/auth")
    public Result<AuthResult> auth(@RequestBody @Validated CashierCodeAuthParam param){
        ValidationUtil.validateParam(param);
        return Res.ok(cashierCodePayService.auth(param));
    }
}
