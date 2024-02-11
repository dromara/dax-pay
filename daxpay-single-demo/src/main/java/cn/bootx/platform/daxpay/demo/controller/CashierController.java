package cn.bootx.platform.daxpay.demo.controller;

import cn.bootx.platform.common.core.annotation.IgnoreAuth;
import cn.bootx.platform.common.core.rest.Res;
import cn.bootx.platform.common.core.rest.ResResult;
import cn.bootx.platform.common.core.util.ValidationUtil;
import cn.bootx.platform.daxpay.demo.param.CashierSimplePayParam;
import cn.bootx.platform.daxpay.demo.result.PayOrderResult;
import cn.bootx.platform.daxpay.demo.service.CashierService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 结算台演示
 * @author xxm
 * @since 2024/2/8
 */
@IgnoreAuth
@Tag(name = "结算台演示")
@RestController
@RequestMapping("/demo/cashier")
@RequiredArgsConstructor
public class CashierController {

    private final CashierService cashierService;

    @Operation(summary = "创建支付订单并发起")
    @PostMapping("/simplePayCashier")
    public ResResult<PayOrderResult> simplePayCashier(@RequestBody CashierSimplePayParam param){
        ValidationUtil.validateParam(param);
        return Res.ok(cashierService.simplePayCashier(param));
    }

    @Operation(summary = "查询支付订单")
    @GetMapping("/queryPayOrderSuccess")
    public ResResult<Boolean> queryPayOrder(String businessNo){
        return Res.ok(cashierService.queryPayOrderSuccess(businessNo));
    }

    @Operation(summary = "获取支付环境")
    @GetMapping("/getPayEnv")
    public ResResult<String> getPayEnv(@RequestHeader("User-Agent") String ua){
        return Res.ok(cashierService.getPayEnv(ua));
    }
}
