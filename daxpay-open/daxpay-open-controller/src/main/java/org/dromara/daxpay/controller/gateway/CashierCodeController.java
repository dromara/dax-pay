package org.dromara.daxpay.controller.gateway;

import cn.bootx.platform.core.annotation.IgnoreAuth;
import cn.bootx.platform.core.rest.Res;
import cn.bootx.platform.core.rest.result.Result;
import cn.bootx.platform.core.util.ValidationUtil;
import org.dromara.daxpay.core.param.gateway.*;
import org.dromara.daxpay.core.result.assist.AuthResult;
import org.dromara.daxpay.core.result.trade.pay.PayResult;
import org.dromara.daxpay.service.result.gateway.GatewayCashierCodeConfigResult;
import org.dromara.daxpay.service.service.gateway.CashierPayService;
import org.dromara.daxpay.service.service.gateway.GatewayPayQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 网关支付码牌服务
 * @author xxm
 * @since 2025/4/12
 */
@Validated
@IgnoreAuth
@Tag(name = "网关支付码牌服务")
@RestController
@RequestMapping("/unipay/gateway/cashier")
@RequiredArgsConstructor
public class CashierCodeController {
    private final CashierPayService cashierPayService;
    private final GatewayPayQueryService checkoutQueryService;

    @Operation(summary = "获取收银码牌配置信息")
    @GetMapping("/getCodeConfig")
    public Result<GatewayCashierCodeConfigResult> getCashierCodeConfig(
            @NotBlank(message = "码牌编码不能为空") @Parameter(description = "码牌编码") String cashierCode,
            @NotBlank(message = "收银台类型不能为空") @Parameter(description = "收银台类型")String cashierType){
        return Res.ok(checkoutQueryService.getCashierCodeConfig(cashierCode, cashierType));
    }

    @Operation(summary = "获取收银码牌支付的授权链接, 用于获取OpenId一类的信息")
    @PostMapping("/code/generateAuthUrl")
    public Result<String> getCashierCodeConfig(@RequestBody @Validated GatewayCashierCodeAuthUrlParam param){
        return Res.ok(cashierPayService.genAuthUrl(param));
    }

    @Operation(summary = "获取网关支付授权结果")
    @PostMapping("/code/auth")
    public Result<AuthResult> auth(@RequestBody @Validated GatewayCashierCodeAuthParam param){
        ValidationUtil.validateParam(param);
        return Res.ok(cashierPayService.auth(param));
    }

    @Operation(summary = "发起支付(码牌)")
    @PostMapping("/code/pay")
    public Result<PayResult> aggregateBarPay(@RequestBody @Validated GatewayCashierCodePayParam param){
        return Res.ok(cashierPayService.cashierCodePay(param));
    }

    @Operation(summary = "发起支付(收银台)")
    @PostMapping("/pay")
    public Result<PayResult> pay(@RequestBody @Validated GatewayCashierPayParam param){
        ValidationUtil.validateParam(param);
        return Res.ok(cashierPayService.cashierPay(param));
    }

    @Operation(summary = "发起支付(收银台付款码)")
    @PostMapping("/barPay")
    public Result<PayResult> cashierBarPay(@RequestBody GatewayCashierBarPayParam param){
        ValidationUtil.validateParam(param);
        return Res.ok(cashierPayService.cashierBarPay(param));
    }
}
