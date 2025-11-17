package org.dromara.daxpay.payment.common.controller.assist;

import cn.bootx.platform.core.annotation.ClientCode;
import cn.bootx.platform.core.annotation.RequestGroup;
import cn.bootx.platform.core.annotation.RequestPath;
import cn.bootx.platform.core.rest.Res;
import cn.bootx.platform.core.rest.result.Result;
import org.dromara.daxpay.payment.common.code.DaxPayCode;
import org.dromara.daxpay.payment.pay.result.gateway.GatewayPayUrlResult;
import org.dromara.daxpay.payment.pay.service.develop.DevelopTradeService;
import org.dromara.daxpay.payment.unipay.param.MerchantPaymentCommonParam;
import org.dromara.daxpay.payment.unipay.param.gateway.GatewayPayParam;
import org.dromara.daxpay.payment.unipay.param.trade.pay.PayParam;
import org.dromara.daxpay.payment.unipay.param.trade.refund.RefundParam;
import org.dromara.daxpay.payment.unipay.param.trade.transfer.TransferParam;
import org.dromara.daxpay.payment.unipay.result.trade.pay.PayResult;
import org.dromara.daxpay.payment.unipay.result.trade.refund.RefundResult;
import org.dromara.daxpay.payment.unipay.result.trade.transfer.TransferResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 开发调试服务商
 * @author xxm
 * @since 2024/9/6
 */
@Validated
@Tag(name = "交易开发调试服务")
@RestController
@ClientCode({DaxPayCode.Client.ADMIN, DaxPayCode.Client.MERCHANT})
@RequestGroup(groupCode = "DevelopTrade", groupName = "交易开发调试服务", moduleCode = "paymentAssist")
@RequestMapping("/develop/trade")
@RequiredArgsConstructor
public class DevelopTradeController {
    private final DevelopTradeService developTradeService;

    @Operation(summary = "支付参数签名")
    @RequestPath("支付参数签名")
    @PostMapping("/sign/pay")
    public Result<String> paySign(@RequestBody DevelopParam<PayParam> param) {
        return Res.ok(developTradeService.genSign(param.param, param.privateKey));
    }

    @Operation(summary = "退款参数签名")
    @RequestPath("退款参数签名")
    @PostMapping("/sign/refund")
    public Result<String> refundSign(@RequestBody DevelopParam<RefundParam> param) {
        return  Res.ok(developTradeService.genSign(param.param, param.privateKey));
    }

    @Operation(summary = "转账参数签名")
    @RequestPath("转账参数签名")
    @PostMapping("/sign/transfer")
    public Result<String> transferSign(@RequestBody DevelopParam<TransferParam> param) {
        return  Res.ok(developTradeService.genSign(param.param, param.privateKey));
    }

    @Operation(summary = "网关支付签名")
    @RequestPath("网关支付签名")
    @PostMapping("/sign/gateway")
    public Result<String> gatewaySign(@RequestBody DevelopParam<GatewayPayParam> param){
        return Res.ok(developTradeService.genSign(param.getParam(), param.getPrivateKey()));
    }

    @Operation(summary = "支付接口")
    @RequestPath("支付接口")
    @PostMapping("/pay")
    public Result<PayResult> pay(@RequestBody @Validated DevelopParam<PayParam> param){
        return Res.ok(developTradeService.pay(param.param));
    }

    @Operation(summary = "退款接口")
    @RequestPath("退款接口")
    @PostMapping("/refund")
    public Result<RefundResult> refund(@RequestBody @Validated DevelopParam<RefundParam> param){
        return Res.ok(developTradeService.refund(param.param));
    }

    @Operation(summary = "转账接口")
    @RequestPath("转账接口")
    @PostMapping("/transfer")
    public Result<TransferResult> transfer(@RequestBody @Validated DevelopParam<TransferParam> param){
        return Res.ok(developTradeService.transfer(param.param));
    }

    @Operation(summary = "网关支付链接创建接口")
    @RequestPath("网关支付链接创建接口")
    @PostMapping("/gateway")
    public Result<GatewayPayUrlResult> checkoutUrl(@RequestBody @Validated DevelopParam<GatewayPayParam> param){
        return Res.ok(developTradeService.checkoutUrl(param.param));
    }


    /**
     * 交易调试参数
     */
    @Data
    @Accessors(chain = true)
    public static class DevelopParam<T extends MerchantPaymentCommonParam> {
        /** 交易参数 */
        private T param;
        /** 生成签名的私钥 */
        private String privateKey;
    }
}
