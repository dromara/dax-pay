package cn.daxpay.single.gateway.controller;

import cn.bootx.platform.common.core.annotation.IgnoreAuth;
import cn.daxpay.single.code.PaymentApiCode;
import cn.daxpay.single.result.DaxResult;
import cn.daxpay.single.service.annotation.PaymentSign;
import cn.daxpay.single.service.annotation.InitPaymentContext;
import cn.daxpay.single.util.DaxRes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 对账相关接口
 * @author xxm
 * @since 2024/5/17
 */
@IgnoreAuth
@Tag(name = "对账接口处理器")
@RestController
@RequestMapping("/unipay/reconcile")
@RequiredArgsConstructor
public class UniReconcileController {

    @PaymentSign
    @InitPaymentContext(PaymentApiCode.PAY)
    @Operation(summary = "下载指定日期的资金流水")
    @PostMapping("/pay")
    public DaxResult<?> down(){
        return DaxRes.ok();
    }

}
