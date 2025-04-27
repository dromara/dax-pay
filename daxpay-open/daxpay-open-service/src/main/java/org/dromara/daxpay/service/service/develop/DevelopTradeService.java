package org.dromara.daxpay.service.service.develop;

import org.dromara.daxpay.core.param.PaymentCommonParam;
import org.dromara.daxpay.core.param.gateway.GatewayPayParam;
import org.dromara.daxpay.core.param.trade.pay.PayParam;
import org.dromara.daxpay.core.param.trade.refund.RefundParam;
import org.dromara.daxpay.core.param.trade.transfer.TransferParam;
import org.dromara.daxpay.core.result.trade.pay.PayResult;
import org.dromara.daxpay.core.result.trade.refund.RefundResult;
import org.dromara.daxpay.core.result.trade.transfer.TransferResult;
import org.dromara.daxpay.service.result.gateway.GatewayPayUrlResult;
import org.dromara.daxpay.service.service.assist.PaymentAssistService;
import org.dromara.daxpay.service.service.gateway.GatewayPayService;
import org.dromara.daxpay.service.service.trade.pay.PayService;
import org.dromara.daxpay.service.service.trade.refund.RefundService;
import org.dromara.daxpay.service.service.trade.transfer.TransferService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 交易开发调试服务商
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
    public String genSign(PaymentCommonParam param){
        paymentAssistService.initMchAndApp(param.getMchNo(), param.getAppId());
        return paymentAssistService.genSign(param);
    }

    /**
     * 支付
     */
    public PayResult pay(PayParam param) {
        // 初始化
        paymentAssistService.initMchAndApp(param.getMchNo(), param.getAppId());
        // 签名校验
        paymentAssistService.signVerify(param);
        return payService.pay(param);
    }

    /**
     * 退款
     */
    public RefundResult refund(RefundParam param) {
        // 初始化
        paymentAssistService.initMchAndApp(param.getMchNo(), param.getAppId());
        // 签名校验
        paymentAssistService.signVerify(param);
        return refundService.refund(param);
    }

    /**
     * 转账
     */
    public TransferResult transfer(TransferParam param) {
        // 初始化
        paymentAssistService.initMchAndApp(param.getMchNo(), param.getAppId());
        // 签名校验
        paymentAssistService.signVerify(param);
        return transferService.transfer(param);
    }

    /**
     * 网关支付
     */
    public GatewayPayUrlResult checkoutUrl(GatewayPayParam param) {
        // 初始化
        paymentAssistService.initMchAndApp(param.getMchNo(), param.getAppId());
        // 签名校验
        paymentAssistService.signVerify(param);
        return gatewayPayService.prePay(param);
    }
}
