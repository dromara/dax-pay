package cn.bootx.platform.daxpay.core.payment.pay.service;

import cn.bootx.platform.daxpay.common.context.AsyncPayLocal;
import cn.bootx.platform.daxpay.common.local.PaymentContextLocal;
import cn.bootx.platform.daxpay.core.order.pay.entity.PayOrder;
import cn.bootx.platform.daxpay.core.system.entity.PlatformConfig;
import cn.bootx.platform.daxpay.core.system.service.PlatformConfigService;
import cn.bootx.platform.daxpay.param.pay.PayParam;
import cn.bootx.platform.daxpay.util.PayUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * 支付支持服务
 * @author xxm
 * @since 2023/12/24
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PayAssistService {
    private final PlatformConfigService platformConfigService;
    /**
     * 初始化支付订单超时时间
     * 1. 如果支付记录为空, 超时时间读取顺序 PayParam -> 平台设置
     * 2. 如果支付记录不为空, 超时时间通过支付记录进行反推
     */
    public void initExpiredTime(PayOrder order, PayParam payParam){
        // 不是异步支付，没有超时时间
        if (PayUtil.isNotSync(payParam.getPayWays())){
            return;
        }
        AsyncPayLocal asyncPayInfo = PaymentContextLocal.get().getAsyncPayInfo();
        // 支付订单是非为空
        if (Objects.nonNull(order)){
            asyncPayInfo.setExpiredTime(order.getExpiredTime());
            return;
        }
        // 支付参数传入
        if (Objects.nonNull(payParam.getExpiredTime())){
            asyncPayInfo.setExpiredTime(payParam.getExpiredTime());
            return;
        }
        PlatformConfig config = platformConfigService.getConfig();
        PayUtil.getPaymentExpiredTime(config.getOrderTimeout());
    }
}
