package org.dromara.daxpay.payment.pay.service.develop;

import org.dromara.daxpay.payment.common.util.PaySignUtil;
import org.dromara.daxpay.payment.pay.result.gateway.GatewayPayUrlResult;
import org.dromara.daxpay.payment.pay.service.assist.PaymentAssistService;
import org.dromara.daxpay.payment.pay.service.trade.pay.PayService;
import org.dromara.daxpay.payment.pay.service.trade.refund.RefundService;
import org.dromara.daxpay.payment.pay.service.trade.transfer.TransferService;
import org.dromara.daxpay.payment.unipay.param.MerchantPaymentCommonParam;
import org.dromara.daxpay.payment.unipay.param.gateway.GatewayPayParam;
import org.dromara.daxpay.payment.unipay.param.trade.pay.PayParam;
import org.dromara.daxpay.payment.unipay.param.trade.refund.RefundParam;
import org.dromara.daxpay.payment.unipay.param.trade.transfer.TransferParam;
import org.dromara.daxpay.payment.unipay.result.trade.pay.PayResult;
import org.dromara.daxpay.payment.unipay.result.trade.refund.RefundResult;
import org.dromara.daxpay.payment.unipay.result.trade.transfer.TransferResult;
import org.dromara.daxpay.payment.unipay.service.gateway.GatewayPayService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 交易开发调试服务
 * @author xxm
 * @since 2024/9/6
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DevelopTradeService {
    private final PaymentAssistService paymentAssistService;
    private final PayService payService;
    private final RefundService refundService;
    private final TransferService transferService;
    private final GatewayPayService gatewayPayService;

    /**
     * 生成签名
     */
    public String genSign(MerchantPaymentCommonParam param, String privateKey){
        paymentAssistService.initMchAndApp(param.getMchNo(), param.getAppId());
        return PaySignUtil.sign(param, privateKey);
    }

    /**
     * 支付
     */
    public PayResult pay(PayParam param) {
        paymentAssistService.initMchAndApp(param.getMchNo(), param.getAppId());
        paymentAssistService.signVerify(param);
        return payService.pay(param);
    }

    /**
     * 退款
     */
    public RefundResult refund(RefundParam param) {
        paymentAssistService.initMchAndApp(param.getMchNo(), param.getAppId());
        paymentAssistService.signVerify(param);
        return refundService.refund(param);
    }

    /**
     * 转账
     */
    public TransferResult transfer(TransferParam param) {
        paymentAssistService.initMchAndApp(param.getMchNo(), param.getAppId());
        paymentAssistService.signVerify(param);
        return transferService.transfer(param);
    }

    /**
     * 网关支付
     */
    public GatewayPayUrlResult checkoutUrl(GatewayPayParam param) {
        paymentAssistService.initMchAndApp(param.getMchNo(), param.getAppId());
        paymentAssistService.signVerify(param);
        return gatewayPayService.prePay(param);
    }
}
