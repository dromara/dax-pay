package cn.daxpay.open.payment.admin.controller.develop;

import cn.daxpay.open.payment.admin.param.develop.DevelopParam;
import cn.daxpay.open.payment.admin.result.develop.DevelopPayResult;
import cn.daxpay.open.payment.admin.result.develop.DevelopSignResult;
import cn.daxpay.open.payment.admin.service.develop.DevelopTradeService;
import cn.daxpay.open.payment.unipay.param.trade.pay.PayParam;
import cn.daxpay.open.platform.core.annotation.PermCode;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/// 交易开发调试(管理)
@PermCode(menuCode = "payment:develop:trade")
@Tag(name = "交易开发调试服务")
@RestController
@RequestMapping("/admin/develop/trade")
@RequiredArgsConstructor
public class DevelopTradeController {

    private final DevelopTradeService developTradeService;

    @PermCode(code = "sign", nameCn = "签名", nameEn = "Sign")
    @Operation(summary = "支付参数签名")
    @PostMapping("/sign")
    public Result<DevelopSignResult> sign(@RequestBody DevelopParam<PayParam> param) {
        return Res.ok(developTradeService.sign(param));
    }

    @PermCode(code = "pay", nameCn = "支付", nameEn = "Pay")
    @Operation(summary = "支付调试(真实发起)")
    @PostMapping("/pay")
    public Result<DevelopPayResult> pay(@RequestBody DevelopParam<PayParam> param) {
        return Res.ok(developTradeService.pay(param));
    }
}
