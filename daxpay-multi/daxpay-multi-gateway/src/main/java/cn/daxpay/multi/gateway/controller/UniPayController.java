package cn.daxpay.multi.gateway.controller;

import cn.daxpay.multi.gateway.result.DaxResult;
import cn.daxpay.multi.gateway.util.DaxRes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 统一支付接口
 * @author xxm
 * @since 2024/6/4
 */
@Tag(name = "统一支付接口")
@RestController
@RequestMapping("/unipay")
@RequiredArgsConstructor
public class UniPayController {

    @Operation(summary = "支付接口")
    @GetMapping("/pay")
    public DaxResult<Void> pay(){
        return DaxRes.ok();
    }

    @Operation(summary = "退款接口")
    @GetMapping("/refund")
    public DaxResult<Void> refund(){
        return DaxRes.ok();
    }

    @Operation(summary = "关闭接口")
    @GetMapping("/close")
    public DaxResult<Void> reconcile(){
        return DaxRes.ok();
    }

    @Operation(summary = "撤销接口")
    @GetMapping("/cancel")
    public DaxResult<Void> cancel(){
        return DaxRes.ok();
    }

    @Operation(summary = "转账接口")
    @GetMapping("/transfer")
    public DaxResult<Void> transfer(){
        return DaxRes.ok();
    }
}
