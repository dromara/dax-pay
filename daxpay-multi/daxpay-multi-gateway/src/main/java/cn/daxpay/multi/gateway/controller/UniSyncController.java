package cn.daxpay.multi.gateway.controller;

import cn.daxpay.multi.core.result.DaxResult;
import cn.daxpay.multi.core.util.DaxRes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 统一同步接口
 * @author xxm
 * @since 2024/6/4
 */
@Tag(name = "统一同步接口")
@RestController
@RequestMapping("/unipay/sync")
public class UniSyncController {

    @Operation(summary = "支付同步接口")
    @PostMapping("/pay")
    public DaxResult<Void> pay(){
        return DaxRes.ok();
    }

    @Operation(summary = "退款同步接口")
    @PostMapping("/refund")
    public DaxResult<Void> refund(){
        return DaxRes.ok();
    }

    @Operation(summary = "分账同步接口")
    @PostMapping("/allocation")
    public DaxResult<Void> allocation(){
        return DaxRes.ok();
    }

    @Operation(summary = "转账同步接口")
    @PostMapping("/transfer")
    public DaxResult<Void> transfer(){
        return DaxRes.ok();
    }
}
