package org.dromara.daxpay.service.controller.unipay;

import cn.bootx.platform.core.annotation.IgnoreAuth;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.dromara.daxpay.service.service.assist.PaymentAssistService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 通道收银台服务
 * @author xxm
 * @since 2024/9/28
 */
@IgnoreAuth
@Tag(name = "通道收银台服务")
@RestController
@RequestMapping("/unipay/cashier/code")
@RequiredArgsConstructor
public class ChannelCashierController {

    private final PaymentAssistService paymentAssistService;

//    @Operation(summary = "获取收银台信息")
//    @GetMapping("/getCashierInfo")
//    public Result<ChannelCashierConfigResult> getCashierInfo(String cashierType,String appId){
//        paymentAssistService.initMchApp(appId);
//        return Res.ok(cashierConfigService.findByCashierType(cashierType));
//    }
//
//
//    @Operation(summary = "获取收银台所需授权链接, 用于获取OpenId一类的信息")
//    @PostMapping("/generateAuthUrl")
//    public Result<String> generateAuthUrl(@RequestBody CashierAuthUrlParam param){
//        ValidationUtil.validateParam(param);
//        paymentAssistService.initMchApp(param.getAppId());
//        return Res.ok(channelCashierService.generateAuthUrl(param));
//    }
//
//    @Operation(summary = "授权获取结果")
//    @PostMapping("/auth")
//    public Result<AuthResult> auth(@RequestBody CashierAuthCodeParam param){
//        ValidationUtil.validateParam(param);
//        paymentAssistService.initMchApp(param.getAppId());
//        return Res.ok(channelCashierService.auth(param));
//    }
//
//    @Operation(summary = "发起支付")
//    @PostMapping("/pay")
//    public Result<PayResult> cashierPay(@RequestBody CashierPayParam param){
//        ValidationUtil.validateParam(param);
//        paymentAssistService.initMchApp(param.getAppId());
//        return Res.ok(channelCashierService.cashierPay(param));
//    }
}
