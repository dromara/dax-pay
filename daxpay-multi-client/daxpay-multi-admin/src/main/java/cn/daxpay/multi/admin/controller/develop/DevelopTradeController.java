package cn.daxpay.multi.admin.controller.develop;

import cn.bootx.platform.core.rest.Res;
import cn.bootx.platform.core.rest.result.Result;
import cn.daxpay.multi.core.param.trade.pay.PayParam;
import cn.daxpay.multi.core.param.trade.refund.RefundParam;
import cn.daxpay.multi.core.param.trade.transfer.TransferParam;
import cn.daxpay.multi.core.result.trade.pay.PayResult;
import cn.daxpay.multi.core.result.trade.refund.RefundResult;
import cn.daxpay.multi.core.result.trade.transfer.TransferResult;
import cn.daxpay.multi.service.service.develop.DevelopTradeService;
import cn.daxpay.multi.service.service.trade.pay.PayService;
import cn.daxpay.multi.service.service.trade.refund.RefundService;
import cn.daxpay.multi.service.service.trade.transfer.TransferService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 开发调试服务商
 * @author xxm
 * @since 2024/9/6
 */
@Tag(name = "开发调试服务商")
@RestController
@RequestMapping("/develop/trade")
@RequiredArgsConstructor
public class DevelopTradeController {
    private final DevelopTradeService developTradeService;
    private final PayService payService;
    private final RefundService refundService;
    private final TransferService transferService;

    @Operation(summary = "支付参数签名")
    @PostMapping("/sign/pay")
    public Result<String> paySign(@RequestBody PayParam param) {
        return Res.ok(developTradeService.genSign(param));
    }

    @Operation(summary = "退款参数签名")
    @PostMapping("/sign/refund")
    public Result<String> refundSign(@RequestBody RefundParam param) {
        return  Res.ok(developTradeService.genSign(param));
    }

    @Operation(summary = "支付参数签名")
    @PostMapping("/sign/transfer")
    public Result<String> transferSign(@RequestBody TransferParam param) {
        return  Res.ok(developTradeService.genSign(param));
    }


    @Operation(summary = "支付接口")
    @PostMapping("/pay")
    public Result<PayResult> pay(@RequestBody PayParam payParam){
        return Res.ok(payService.pay(payParam));
    }

    @Operation(summary = "退款接口")
    @PostMapping("/refund")
    public Result<RefundResult> refund(@RequestBody RefundParam payParam){
        return Res.ok(refundService.refund(payParam));
    }

    @Operation(summary = "转账接口")
    @PostMapping("/transfer")
    public Result<TransferResult> transfer(@RequestBody TransferParam transferParam){
        return Res.ok(transferService.transfer(transferParam));
    }

}
