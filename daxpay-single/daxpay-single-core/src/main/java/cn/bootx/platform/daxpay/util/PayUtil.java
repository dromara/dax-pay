package cn.bootx.platform.daxpay.util;

import cn.bootx.platform.common.core.util.LocalDateTimeUtil;
import cn.bootx.platform.daxpay.code.PayChannelEnum;
import cn.bootx.platform.daxpay.exception.pay.PayAmountAbnormalException;
import cn.bootx.platform.daxpay.exception.pay.PayFailureException;
import cn.bootx.platform.daxpay.param.payment.pay.PayParam;
import cn.hutool.core.date.DatePattern;
import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * 支付工具类
 * @author xxm
 * @since 2023/12/24
 */
@UtilityClass
public class PayUtil {

    /**
     * 校验参数
     */
    public void validation(PayParam payParam) {
        // 验证支付金额
        validationAmount(payParam.getPayChannels());
        // 验证异步支付方式
        validationAsyncPay(payParam);
        // 验证是否可以分账
        validationAllocation(payParam);
    }

    /**
     * 验证是否可以分账
     */
    private void validationAllocation(PayParam payParam) {
        // 如果分账不启用, 不需要校验
        if (!payParam.isAllocation()) {
            return;
        }
        // 只有异步支付方式支持分账
        if (isNotSync(payParam.getPayChannels())) {
            throw new PayFailureException("分账只支持包含异步支付通道的订单");
        }
    }


    /**
     * 检查异步支付方式
     */
    public void validationAsyncPay(PayParam payParam) {
        // 组合支付时只允许有一个异步支付方式
        List<PayChannelParam> payModeList = payParam.getPayChannels();

        long asyncPayCount = payModeList.stream()
                .map(PayChannelParam::getChannel)
                .map(PayChannelEnum::findByCode)
                .filter(PayChannelEnum.ASYNC_TYPE::contains)
                .count();
        if (asyncPayCount > 1) {
            throw new PayFailureException("组合支付时只允许有一个异步支付方式");
        }

    }

    /**
     * 检查支付金额
     */
    public void validationAmount(List<PayChannelParam> payModeList) {
        // 验证支付金额
        for (PayChannelParam payChannelParam : payModeList) {
            // 支付金额小于等于零
            if (payChannelParam.getAmount() < 0) {
                throw new PayAmountAbnormalException();
            }
        }
    }

    /**
     * 获取支付宝的过期时间 yyyy-MM-dd HH:mm:ss
     */
    public String getAliTimeExpire(LocalDateTime dateTime) {
        return LocalDateTimeUtil.format(dateTime, DatePattern.NORM_DATETIME_PATTERN);
    }

    /**
     * 获取微信的过期时间 yyyyMMddHHmmss
     */
    public String getWxExpiredTime(LocalDateTime dateTime) {
        return LocalDateTimeUtil.format(dateTime, DatePattern.PURE_DATETIME_PATTERN);
    }

    /**
     * 获取云闪付的过期时间 yyyyMMddHHmmss
     */
    public String getUnionExpiredTime(LocalDateTime dateTime) {
        return LocalDateTimeUtil.format(dateTime, DatePattern.PURE_DATETIME_PATTERN);
    }

    /**
     * 获取支付单的超时时间
     */
    public LocalDateTime getPaymentExpiredTime(Integer minute) {
        return LocalDateTimeUtil.offset(LocalDateTime.now(), minute, ChronoUnit.MINUTES);
    }

    /**
     * 判断是否有异步支付
     */
    public boolean isNotSync(List<PayChannelParam> payChannelParams) {
        return payChannelParams.stream()
                .map(PayChannelParam::getChannel)
                .noneMatch(PayChannelEnum.ASYNC_TYPE_CODE::contains);
    }

}
