package cn.bootx.platform.daxpay.openapi.controller;

import cn.bootx.platform.common.core.annotation.IgnoreAuth;
import cn.bootx.platform.common.core.rest.Res;
import cn.bootx.platform.common.core.rest.ResResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 统一支付接口
 * @author xxm
 * @since 2023/12/15
 */
@IgnoreAuth
@Tag(name = "统一支付接口")
@RestController
@RequestMapping("/unipay")
@RequiredArgsConstructor
public class UniPayController {

    @Operation(summary = "统一下单")
    @PostMapping("/pay")
    public ResResult<Void> pay(){
        return Res.ok();
    }
    @Operation(summary = "简单下单")
    @PostMapping("/simplePay")
    public ResResult<Void> simplePay(){
        return Res.ok();
    }
    @Operation(summary = "订单撤销")
    @PostMapping("/cancel")
    public ResResult<Void> cancel(){
        return Res.ok();
    }
    @Operation(summary = "订单关闭")
    @PostMapping("/close")
    public ResResult<Void> close(){
        return Res.ok();
    }
    @Operation(summary = "统一退款")
    @PostMapping("/refund")
    public ResResult<Void> refund(){
        return Res.ok();
    }
    @Operation(summary = "简单退款")
    @PostMapping("/simpleRefund")
    public ResResult<Void> simpleRefund(){
        return Res.ok();
    }
    @Operation(summary = "支付状态同步")
    @PostMapping("/syncPay")
    public ResResult<Void> syncPay(){
        return Res.ok();
    }
    @Operation(summary = "退款状态同步")
    @PostMapping("/syncRefund")
    public ResResult<Void> syncRefund(){
        return Res.ok();
    }
}
