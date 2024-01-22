package cn.bootx.platform.daxpay.util;

import cn.bootx.platform.common.core.util.LocalDateTimeUtil;
import cn.bootx.platform.daxpay.code.PayChannelEnum;
import cn.bootx.platform.daxpay.entity.RefundableInfo;
import cn.bootx.platform.daxpay.exception.pay.PayAmountAbnormalException;
import cn.bootx.platform.daxpay.exception.pay.PayFailureException;
import cn.bootx.platform.daxpay.param.pay.PayChannelParam;
import cn.bootx.platform.daxpay.param.pay.PayParam;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.util.IdUtil;
import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;

/**
 * 支付工具类
 * @author xxm
 * @since 2023/12/24
 */
@UtilityClass
public class PayUtil {
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
        for (PayChannelParam payChannelParam : payModeList) {
            // 支付金额小于等于零
            if (payChannelParam.getAmount() < 0) {
                throw new PayAmountAbnormalException();
            }
        }
    }

    /**
     * 获取支付宝的过期时间
     */
    public String getAliExpiredTime(int minute) {
        return minute + "m";
    }

    /**
     * 获取支付宝的过期时间
     */
    public String getAliTimeExpire(LocalDateTime dateTime) {
        return LocalDateTimeUtil.format(dateTime, DatePattern.NORM_DATETIME_PATTERN);
    }

    /**
     * 获取微信的过期时间
     */
    public String getWxExpiredTime(LocalDateTime dateTime) {
        return LocalDateTimeUtil.format(dateTime, DatePattern.PURE_DATETIME_PATTERN);
    }

    /**
     * 过滤出需要的可退款数据
     */
    public RefundableInfo refundableInfoFilter(List<RefundableInfo> refundableInfos, PayChannelEnum payChannelEnum){
        return refundableInfos.stream()
                .filter(o -> Objects.equals(o.getChannel(), payChannelEnum.getCode()))
                .findFirst()
                .orElseThrow(() -> new PayFailureException("退款数据不存在"));
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

    /**
     * 生成退款号
     */
    public String getRefundNo(){
//        return "R" + IdUtil.getSnowflakeNextIdStr();
        return IdUtil.getSnowflakeNextIdStr();
    }
}
