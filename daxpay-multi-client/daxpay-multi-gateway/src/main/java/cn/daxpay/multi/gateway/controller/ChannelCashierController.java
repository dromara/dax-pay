package cn.daxpay.multi.gateway.controller;

import cn.bootx.platform.core.annotation.IgnoreAuth;
import cn.bootx.platform.core.rest.Res;
import cn.bootx.platform.core.rest.result.Result;
import cn.bootx.platform.core.util.ValidationUtil;
import cn.daxpay.multi.core.param.assist.GenerateAuthUrlParam;
import cn.daxpay.multi.core.param.cashier.CashierAuthCodeParam;
import cn.daxpay.multi.core.param.cashier.CashierPayParam;
import cn.daxpay.multi.core.result.DaxResult;
import cn.daxpay.multi.core.result.assist.AuthUrlResult;
import cn.daxpay.multi.core.result.trade.pay.PayResult;
import cn.daxpay.multi.core.util.DaxRes;
import cn.daxpay.multi.service.common.anno.PaymentVerify;
import cn.daxpay.multi.service.service.assist.PaymentAssistService;
import cn.daxpay.multi.service.service.cashier.ChannelCashierService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
@RequestMapping("/unipay/ext/channel/cashier")
@RequiredArgsConstructor
public class ChannelCashierController {

    private final ChannelCashierService channelCashierService;

    private final PaymentAssistService paymentAssistService;


    @PaymentVerify
    @Operation(summary = "获取授权链接")
    @PostMapping("/generateAuthUrl")
    public DaxResult<AuthUrlResult> generateAuthUrl(@RequestBody GenerateAuthUrlParam param){
        return DaxRes.ok();
    }


    @Operation(summary = "通过AuthCode获取并设置认证结果")
    @PostMapping("/authAndSet")
    public Result<Void> authAndSet(@RequestBody CashierAuthCodeParam param){
        paymentAssistService.initMchAndApp(param.getMchNo(), param.getAppId());
        return Res.ok();
    }

    @Operation(summary = "发起支付")
    @PostMapping("/pay")
    public Result<PayResult> cashierPay(@RequestBody CashierPayParam param){
        ValidationUtil.validateParam(param);
        return Res.ok(channelCashierService.cashierPay(param));
    }
}
