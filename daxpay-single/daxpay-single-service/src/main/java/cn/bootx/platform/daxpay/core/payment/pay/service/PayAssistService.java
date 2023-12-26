package cn.bootx.platform.daxpay.core.payment.pay.service;

import cn.bootx.platform.daxpay.common.context.AsyncPayLocal;
import cn.bootx.platform.daxpay.common.context.NoticeLocal;
import cn.bootx.platform.daxpay.common.context.PlatformLocal;
import cn.bootx.platform.daxpay.common.local.PaymentContextLocal;
import cn.bootx.platform.daxpay.core.order.pay.entity.PayOrder;
import cn.bootx.platform.daxpay.param.pay.PayParam;
import cn.bootx.platform.daxpay.util.PayUtil;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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

    /**
     * 初始化支付相关上下文
     */
    public void initPayContext(PayOrder order, PayParam payParam){
        // 初始化支付订单超时时间
        this.initExpiredTime(order,payParam);
        // 初始化通知相关上下文
        this.initNotice(payParam);
    }


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
        PlatformLocal platform = PaymentContextLocal.get().getPlatform();
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
        LocalDateTime paymentExpiredTime = PayUtil.getPaymentExpiredTime(platform.getOrderTimeout());
        asyncPayInfo.setExpiredTime(paymentExpiredTime);
    }

    /**
     * 初始化通知相关上下文
     */
    private void initNotice(PayParam payParam){
        NoticeLocal noticeInfo = PaymentContextLocal.get().getNoticeInfo();
        PlatformLocal platform = PaymentContextLocal.get().getPlatform();
        // 异步回调
        if (!payParam.isNotNotify()){
            noticeInfo.setNotifyUrl(payParam.getReturnUrl());
            if (StrUtil.isNotBlank(payParam.getNotifyUrl())){
                noticeInfo.setNotifyUrl(platform.getNotifyUrl());
            }
        }
        // 同步回调
        if (!payParam.isNotReturn()){
            noticeInfo.setReturnUrl(payParam.getReturnUrl());
        }
        // 退出回调地址
        noticeInfo.setQuitUrl(payParam.getQuitUrl());
    }

}
