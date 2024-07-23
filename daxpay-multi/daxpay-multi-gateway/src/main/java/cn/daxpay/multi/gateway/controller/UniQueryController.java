package cn.daxpay.multi.gateway.controller;

import cn.bootx.platform.core.annotation.IgnoreAuth;
import cn.daxpay.multi.service.common.anno.PaymentVerify;
import cn.daxpay.multi.core.result.DaxResult;
import cn.daxpay.multi.core.util.DaxRes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 统一查询接口
 * @author xxm
 * @since 2024/6/4
 */
@IgnoreAuth
@Tag(name = "统一查询接口")
@RestController
@RequestMapping("/unipay/query")
@RequiredArgsConstructor
public class UniQueryController {


    @PaymentVerify
    @Operation(summary = "支付订单查询接口")
    @PostMapping("/payOrder")
    public DaxResult<Void> queryPayOrder(){
        return DaxRes.ok();
    }

    @PaymentVerify
    @Operation(summary = "退款订单查询接口")
    @PostMapping("/refundOrder")
     public DaxResult<Void> queryRefundOrder(){
        return DaxRes.ok();
    }

    @PaymentVerify
    @Operation(summary = "分账订单查询接口")
    @PostMapping("/allocOrder")
    public DaxResult<Void> queryAllocationOrder(){
        return DaxRes.ok();
    }

    @PaymentVerify
    @Operation(summary = "转账订单查询接口")
    @PostMapping("/transferOrder")
    public DaxResult<Void> transferOrder(){
        return DaxRes.ok();
    }

    @PaymentVerify
    @Operation(summary = "分账接收方查询接口")
    @PostMapping("/allocationReceiver")
    public DaxResult<Void> queryAllocReceive(){
        return DaxRes.ok();
    }

}
