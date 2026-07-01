package cn.daxpay.open.payment.unipay.controller;

import cn.daxpay.open.platform.core.annotation.IgnoreAuth;
import cn.daxpay.open.platform.core.enums.common.PaymentApiEnum;
import cn.daxpay.open.platform.core.exception.operation.OperationFailException;
import cn.daxpay.open.platform.core.code.CommonCode;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.common.util.DaxRes;
import cn.daxpay.open.payment.common.service.MerchantPermissionService;
import cn.daxpay.open.payment.old.pay.anno.PaymentVerify;
import cn.daxpay.open.payment.pay.service.PayCloseService;
import cn.daxpay.open.payment.pay.service.NormalPayService;
import cn.daxpay.open.payment.unipay.param.trade.pay.NormalPayCloseParam;
import cn.daxpay.open.payment.unipay.param.trade.pay.NormalPayParam;
import cn.daxpay.open.payment.unipay.result.trade.pay.NormalPayResult;
import cn.daxpay.open.platform.core.enums.pay.trade.TradeSourceEnum;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/// # 统一支付接口
///
@PaymentVerify
@IgnoreAuth
@Tag(name = "统一交易接口")
@RestController
@RequestMapping("/unipay")
@RequiredArgsConstructor
public class UniTradeController {
    private final NormalPayService normalPayService;
    private final PayCloseService payCloseService;
    private final MerchantPermissionService permConfigService;

    @Operation(summary = "支付接口")
    @PostMapping("/pay")
    public DaxResult<NormalPayResult> pay(@RequestBody NormalPayParam payParam){
        if (!permConfigService.hasApiPerm(PaymentApiEnum.PAY.getCode())){
            // 订单: 该商户没有此接口的权限
            throw new OperationFailException(CommonCode.FAIL_CODE, "error.payment.order.merchantNoApiPermission");
        }
        return DaxRes.ok(normalPayService.pay(payParam));
    }

    @Operation(summary = "关闭和撤销接口")
    @PostMapping("/close")
    public DaxResult<Void> close(@RequestBody NormalPayCloseParam param){
        if ( permConfigService.hasApiPerm(PaymentApiEnum.CLOSE.getCode())){
            // 订单: 该商户没有此接口的权限
            throw new OperationFailException(CommonCode.FAIL_CODE, "error.payment.order.merchantNoApiPermission");
        }
        payCloseService.close(param);
        return DaxRes.ok();
    }

}
