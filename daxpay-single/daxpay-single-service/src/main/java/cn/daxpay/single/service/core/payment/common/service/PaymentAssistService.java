package cn.daxpay.single.service.core.payment.common.service;

import cn.daxpay.single.param.PaymentCommonParam;
import cn.daxpay.single.service.common.context.RequestLocal;
import cn.daxpay.single.service.common.local.PaymentContextLocal;
import cn.daxpay.single.service.core.system.config.service.PlatformConfigService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 支付、退款等各类操作支持服务
 * @author xxm
 * @since 2023/12/26
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentAssistService {
    private final PlatformConfigService platformConfigService;

    /**
     * 初始化上下文
     */
    public void initContext(PaymentCommonParam paymentCommonParam){
        platformConfigService.initPlatform();
        this.initRequest(paymentCommonParam);
    }
    /**
     * 初始化请求相关信息上下文
     */
    public void initRequest(PaymentCommonParam paymentCommonParam){
        RequestLocal request = PaymentContextLocal.get().getRequestInfo();
        request.setClientIp(paymentCommonParam.getClientIp())
                .setSign(paymentCommonParam.getSign())
                .setReqTime(paymentCommonParam.getReqTime());
    }
}
