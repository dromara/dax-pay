package org.dromara.daxpay.service.controller.unipay;

import cn.bootx.platform.core.annotation.IgnoreAuth;
import cn.bootx.platform.core.rest.Res;
import cn.bootx.platform.core.rest.result.Result;
import cn.bootx.platform.core.util.ValidationUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.dromara.daxpay.core.param.cashier.CashierCodeAuthUrlParam;
import org.dromara.daxpay.core.param.cashier.CashierCodePayParam;
import org.dromara.daxpay.core.result.assist.AuthResult;
import org.dromara.daxpay.core.result.cashier.CashierCodeResult;
import org.dromara.daxpay.core.result.trade.pay.PayResult;
import org.dromara.daxpay.core.param.cashier.CashierCodeAuthCodeParam;
import org.dromara.daxpay.service.service.cashier.CashierCodeService;
import org.dromara.daxpay.service.service.config.cashier.CashierCodeConfigService;
import org.springframework.web.bind.annotation.*;

/**
 * 通道码牌服务
 * @author xxm
 * @since 2024/9/28
 */
@IgnoreAuth
@Tag(name = "通道码牌服务")
@RestController
@RequestMapping("/unipay/cashier/code")
@RequiredArgsConstructor
public class CashierCodeController {

    private final CashierCodeConfigService codeConfigService;

    private final CashierCodeService cashierCodeService;

    @Operation(summary = "根据code和类型查询码牌信息和配置")
    @GetMapping("/findByCashierType")
    public Result<CashierCodeResult> findByCashierType(@NotBlank(message = "码牌code不可为空") String cashierCode,
                                                       @NotBlank(message = "码牌类型不可为空") String cashierType){
        return Res.ok(codeConfigService.findByCashierType(cashierCode, cashierType));
    }

    @Operation(summary = "获取码牌所需授权链接, 用于获取OpenId一类的信息")
    @PostMapping("/generateAuthUrl")
    public Result<String> generateAuthUrl(@RequestBody CashierCodeAuthUrlParam param){
        ValidationUtil.validateParam(param);
        return Res.ok(cashierCodeService.generateAuthUrl(param));
    }

    @Operation(summary = "获取授权结果")
    @PostMapping("/auth")
    public Result<AuthResult> auth(@RequestBody CashierCodeAuthCodeParam param){
        ValidationUtil.validateParam(param);
        return Res.ok(cashierCodeService.auth(param));
    }

    @Operation(summary = "发起支付")
    @PostMapping("/pay")
    public Result<PayResult> cashierPay(@RequestBody CashierCodePayParam param){
        ValidationUtil.validateParam(param);
        return Res.ok(cashierCodeService.cashierPay(param));
    }
}
