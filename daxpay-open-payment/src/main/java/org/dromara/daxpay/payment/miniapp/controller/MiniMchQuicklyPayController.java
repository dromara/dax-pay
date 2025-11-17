package org.dromara.daxpay.payment.miniapp.controller;

import cn.bootx.platform.core.annotation.ClientCode;
import cn.bootx.platform.core.annotation.RequestGroup;
import cn.bootx.platform.core.annotation.RequestPath;
import cn.bootx.platform.core.rest.Res;
import cn.bootx.platform.core.rest.result.Result;
import org.dromara.daxpay.payment.common.code.DaxPayCode;
import org.dromara.daxpay.payment.miniapp.param.quickly.QuicklyBarPayParam;
import org.dromara.daxpay.payment.miniapp.param.quickly.QuicklyQrPayParam;
import org.dromara.daxpay.payment.miniapp.service.MiniMchQuicklyPayService;
import org.dromara.daxpay.payment.unipay.result.trade.pay.PayResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 小程序快捷支付
 * @author xxm
 * @since 2025/4/21
 */
@Validated
@Tag(name = "小程序快捷支付")
@ClientCode(DaxPayCode.Client.MERCHANT)
@RestController
@RequestMapping("/mini/mch/quickly/pay")
@RequestGroup(groupCode = "MiniMchQuickly", groupName = "小程序快捷支付", moduleCode = "MiniMch")
@RequiredArgsConstructor
public class MiniMchQuicklyPayController {
    private final MiniMchQuicklyPayService quicklyPayService;

    @RequestPath("生成聚合支付码")
    @Operation(summary = "生成聚合支付码")
    @PostMapping("/genAggregateUrl")
    public Result<PayResult> genAggregateUrl(@RequestBody @Validated QuicklyQrPayParam param){
        return Res.ok(quicklyPayService.genAggregateUrl(param));
    }

    @RequestPath("被扫支付")
    @Operation(summary = "被扫支付")
    @PostMapping("/barPay")
    public Result<PayResult> barPay(@RequestBody @Validated QuicklyBarPayParam param){
        return Res.ok(quicklyPayService.barPay(param));
    }

}
