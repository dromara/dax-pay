package cn.daxpay.multi.service.service.develop;

import cn.daxpay.multi.core.param.PaymentCommonParam;
import cn.daxpay.multi.service.service.assist.PaymentAssistService;
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

    /**
     * 生成签名
     */
    public String genSign(PaymentCommonParam param){
        paymentAssistService.initMchAndApp(param.getMchNo(), param.getAppId());
        return paymentAssistService.genSign(param);
    }
}
