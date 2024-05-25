package cn.daxpay.single.util;

import cn.bootx.platform.common.core.util.LocalDateTimeUtil;
import cn.daxpay.single.exception.pay.PayAmountAbnormalException;
import cn.daxpay.single.param.payment.pay.PayParam;
import cn.hutool.core.date.DatePattern;
import lombok.experimental.UtilityClass;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * 支付工具类
 * @author xxm
 * @since 2023/12/24
 */
@UtilityClass
public class PayUtil {
    private static final BigDecimal HUNDRED = new BigDecimal(100);

    /**
     * 校验参数
     */
    public void validation(PayParam payParam) {
        // 验证支付金额
        validationAmount(payParam);
    }

    /**
     * 检查支付金额
     */
    public void validationAmount(PayParam param) {
        // 验证支付金额
        if (param.getAmount() <= 0) {
            throw new PayAmountAbnormalException("支付金额不能小于等于0");
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
     * 元转分
     * @param amount 元的金额
     * @return 分的金额
     */
    public static int convertCentAmount(BigDecimal amount) {
        return amount.multiply(HUNDRED).setScale(0, RoundingMode.HALF_UP).intValue();
    }

    /**
     * 分转元,保留两位小数
     *
     * @param amount 元的金额
     * @return 元的金额 两位小数
     */
    public static BigDecimal conversionAmount(int amount) {
        return BigDecimal.valueOf(amount).divide(HUNDRED,2, RoundingMode.HALF_UP);
    }
}
