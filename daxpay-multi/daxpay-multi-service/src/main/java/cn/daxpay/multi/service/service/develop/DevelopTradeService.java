package cn.daxpay.multi.service.service.develop;

import cn.daxpay.multi.core.param.PaymentCommonParam;
import cn.daxpay.multi.core.param.trade.pay.PayParam;
import cn.daxpay.multi.core.param.trade.refund.RefundParam;
import cn.daxpay.multi.core.param.trade.transfer.TransferParam;
import cn.daxpay.multi.core.result.trade.pay.PayResult;
import cn.daxpay.multi.core.result.trade.refund.RefundResult;
import cn.daxpay.multi.core.result.trade.transfer.TransferResult;
import cn.daxpay.multi.service.service.assist.PaymentAssistService;
import cn.daxpay.multi.service.service.trade.pay.PayService;
import cn.daxpay.multi.service.service.trade.refund.RefundService;
import cn.daxpay.multi.service.service.trade.transfer.TransferService;
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
}
