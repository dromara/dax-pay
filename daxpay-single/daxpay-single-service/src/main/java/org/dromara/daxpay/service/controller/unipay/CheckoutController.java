package org.dromara.daxpay.service.controller.unipay;

import cn.bootx.platform.core.rest.Res;
import cn.bootx.platform.core.rest.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.dromara.daxpay.core.param.cashier.CheckoutParam;
import org.dromara.daxpay.core.param.cashier.CheckoutPayParam;
import org.dromara.daxpay.core.result.DaxResult;
import org.dromara.daxpay.core.result.cashier.CheckoutUrlResult;
import org.dromara.daxpay.core.util.DaxRes;
import org.dromara.daxpay.service.common.anno.PaymentVerify;
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

    @PaymentVerify
    @Operation(summary = "创建一个收银台链接")
    @PostMapping("/creat")
    public DaxResult<CheckoutUrlResult> creat(@RequestBody CheckoutParam checkoutParam){
        return DaxRes.ok(checkoutService.creat(checkoutParam));
    }

    @Operation(summary = "获取收银台订单信息")
    @GetMapping("/info")
    public Result<Void> getInfo(){
        return Res.ok();
    }

    @Operation(summary = "发起支付")
    @PostMapping("/pay")
    public Result<Void> pay(@RequestBody CheckoutPayParam checkoutParam){
        return Res.ok();
    }

}
