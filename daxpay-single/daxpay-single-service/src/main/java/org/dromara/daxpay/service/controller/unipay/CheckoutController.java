package org.dromara.daxpay.service.controller.unipay;

import cn.bootx.platform.core.rest.Res;
import cn.bootx.platform.core.rest.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.dromara.daxpay.core.param.checkout.CheckoutParam;
import org.dromara.daxpay.core.param.checkout.CheckoutPayParam;
import org.dromara.daxpay.core.result.DaxResult;
import org.dromara.daxpay.core.result.checkout.CheckoutAggregateOrderAndConfigResult;
import org.dromara.daxpay.core.result.checkout.CheckoutOrderAndConfigResult;
import org.dromara.daxpay.core.result.checkout.CheckoutUrlResult;
import org.dromara.daxpay.core.util.DaxRes;
import org.dromara.daxpay.service.common.anno.PaymentVerify;
import org.dromara.daxpay.service.service.cashier.CheckoutQueryService;
import org.dromara.daxpay.service.service.cashier.CheckoutService;
import org.springframework.web.bind.annotation.*;

/**
 * 收银台服务
 * @author xxm
 * @since 2024/11/26
 */
@Tag(name = "收银台服务")
@RestController
@RequestMapping("/unipay/checkout")
@RequiredArgsConstructor
public class CheckoutController {
    private final CheckoutService checkoutService;
    private final CheckoutQueryService checkoutQueryService;

    @PaymentVerify
    @Operation(summary = "创建一个收银台链接")
    @PostMapping("/creat")
    public DaxResult<CheckoutUrlResult> creat(@RequestBody CheckoutParam checkoutParam){
        return DaxRes.ok(checkoutService.creat(checkoutParam));
    }

    @Operation(summary = "获取收银台订单和配置信息")
    @GetMapping("/getOrderAndConfig")
    public Result<CheckoutOrderAndConfigResult> getOrderAndConfig(String orderNo, String checkoutType){
        return Res.ok(checkoutQueryService.getOrderAndConfig(orderNo, checkoutType));
    }

    @Operation(summary = "获取聚合支付配置")
    @GetMapping("/getAggregateConfig")
    public Result<CheckoutAggregateOrderAndConfigResult> getAggregateConfig(String orderNo, String checkoutType){
        return Res.ok(checkoutQueryService.getAggregateConfig(orderNo, checkoutType));
    }

    @Operation(summary = "发起支付")
    @PostMapping("/pay")
    public Result<Void> pay(@RequestBody CheckoutPayParam checkoutParam){
        return Res.ok();
    }

}
