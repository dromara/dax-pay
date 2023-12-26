package cn.bootx.platform.daxpay.core.payment.common.service;

import cn.bootx.platform.common.core.code.CommonCode;
import cn.bootx.platform.daxpay.common.context.PlatformLocal;
import cn.bootx.platform.daxpay.common.context.RequestLocal;
import cn.bootx.platform.daxpay.common.local.PaymentContextLocal;
import cn.bootx.platform.daxpay.core.system.entity.PlatformConfig;
import cn.bootx.platform.daxpay.core.system.service.PlatformConfigService;
import cn.bootx.platform.daxpay.param.pay.PayCommonParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
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
    public void initContext(PayCommonParam payCommonParam){
        this.initPlatform();
        this.initRequest(payCommonParam);
    }

    /**
     * 初始化平台配置上下文
     */
    private void initPlatform(){
        PlatformConfig config = platformConfigService.getConfig();
        PlatformLocal platform = PaymentContextLocal.get().getPlatform();
        platform.setSignType(config.getSignType());
        platform.setSignSecret(config.getSignSecret());
        platform.setNotifyUrl(config.getNotifyUrl());
        platform.setOrderTimeout(config.getOrderTimeout());
        platform.setWebsiteUrl(config.getWebsiteUrl());
    }


    /**
     * 初始化请求相关信息上下文
     */
    private void initRequest(PayCommonParam payCommonParam){
        RequestLocal request = PaymentContextLocal.get().getRequest();
        request.setClientIp(payCommonParam.getClientIp())
                .setExtraParam(payCommonParam.getExtraParam())
                .setSign(payCommonParam.getSign())
                .setVersion(payCommonParam.getVersion())
                .setReqTime(payCommonParam.getReqTime())
                .setReqId(MDC.get(CommonCode.TRACE_ID));
    }
}
