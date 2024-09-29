package cn.daxpay.multi.gateway.controller;

import cn.bootx.platform.core.annotation.IgnoreAuth;
import cn.bootx.platform.core.rest.Res;
import cn.bootx.platform.core.rest.result.Result;
import cn.bootx.platform.core.util.ValidationUtil;
import cn.daxpay.multi.core.param.cashier.CashierAuthCodeParam;
import cn.daxpay.multi.core.param.cashier.CashierPayParam;
import cn.daxpay.multi.core.result.trade.pay.PayResult;
import cn.daxpay.multi.service.common.cache.MerchantCacheService;
import cn.daxpay.multi.service.entity.merchant.Merchant;
import cn.daxpay.multi.service.result.config.ChannelCashierConfigResult;
import cn.daxpay.multi.service.service.assist.PaymentAssistService;
import cn.daxpay.multi.service.service.cashier.ChannelCashierService;
import cn.daxpay.multi.service.service.config.ChannelCashierConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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

    private final ChannelCashierConfigService cashierConfigService;

    private final MerchantCacheService merchantCacheService;

    @Operation(summary = "获取商户名称")
    @GetMapping("/getMchName")
    public Result<String> getMchName(String mchNo){
        Merchant merchant = merchantCacheService.get(mchNo);
        return Res.ok(merchant.getMchName());
    }

    @Operation(summary = "获取收银台信息")
    @GetMapping("/getCashierType")
    public Result<ChannelCashierConfigResult> getCashierType(String cashierType,String appId){
        paymentAssistService.initMchAndApp(appId);
        return Res.ok(cashierConfigService.findByCashierType(cashierType));
    }


    @Operation(summary = "获取收银台所需授权链接, 用于获取OpenId一类的信息")
    @PostMapping("/generateAuthUrl")
    public Result<String> generateAuthUrl(@RequestBody CashierAuthCodeParam param){
        ValidationUtil.validateParam(param);
        paymentAssistService.initMchAndApp(param.getMchNo(), param.getAppId());
        return Res.ok(channelCashierService.generateAuthUrl(param));
    }

    @Operation(summary = "发起支付")
    @PostMapping("/pay")
    public Result<PayResult> cashierPay(@RequestBody CashierPayParam param){
        ValidationUtil.validateParam(param);
        paymentAssistService.initMchAndApp(param.getMchNo(), param.getAppId());
        return Res.ok(channelCashierService.cashierPay(param));
    }
}
