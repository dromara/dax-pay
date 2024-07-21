package cn.daxpay.multi.core.util;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.LocalDateTimeUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.experimental.UtilityClass;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

/**
 * 支付工具类
 * @author xxm
 * @since 2023/12/24
 */
@UtilityClass
public class PayUtil {
    private static final BigDecimal HUNDRED = new BigDecimal(100);

    /**
     * 获取支付宝的过期时间 yyyy-MM-dd HH:mm:ss
     */
    @Deprecated
    public String getAliTimeExpire(LocalDateTime dateTime) {
        return LocalDateTimeUtil.format(dateTime, DatePattern.NORM_DATETIME_PATTERN);
    }

    /**
     * 获取云闪付的过期时间 yyyyMMddHHmmss
     */
    @Deprecated
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
    public int convertCentAmount(BigDecimal amount) {
        return amount.multiply(HUNDRED).setScale(0, RoundingMode.HALF_UP).intValue();
    }

    /**
     * 分转元,保留两位小数
     *
     * @param amount 元的金额
     * @return 元的金额 两位小数
     */
    public BigDecimal conversionAmount(int amount) {
        return BigDecimal.valueOf(amount).divide(HUNDRED,2, RoundingMode.HALF_UP);
    }

    /**
     * 保留两位小数
     */
    public BigDecimal toDecimal(BigDecimal amount) {
        return amount.setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * 比较大小
     * @param first 数字1
     * @param last 数字2
     * @return first > last =1 / first == last = 0 / first < last = -1
     */
    public int compareTo(BigDecimal first, BigDecimal last) {
        BigDecimal newFirst = BigDecimal.ZERO;
        BigDecimal newLast = BigDecimal.ZERO;
        if (first != null) {
            newFirst = first;
        }
        if (last != null) {
            newLast = last;
        }
        return newFirst.compareTo(newLast);
    }

    /**
     * 是否大于
     */
    public boolean isGreaterThan(BigDecimal first, BigDecimal last) {
        return compareTo(first, last) > 0;
    }

    /**
     * 是否大于或等于
     */
    public boolean isGreaterAndEqualThan(BigDecimal first, BigDecimal last) {
        return compareTo(first, last) >= 0;
    }

    /**
     * 是否小于
     */
    public boolean isLessThan(BigDecimal first, BigDecimal last) {
        return compareTo(first, last) < 0;
    }

    /**
     * 是否等于
     */
    public boolean isEqual(BigDecimal first, BigDecimal last) {
        return compareTo(first, last) == 0;
    }

    /**
     * 获取请求参数
     */
    public Map<String, String> toMap(HttpServletRequest request) {
        Map<String, String> params = new HashMap<>();
        Map<String, String[]> requestParams = request.getParameterMap();

        for (String name : requestParams.keySet()) {
            String[] values = requestParams.get(name);
            String valueStr = "";

            for (int i = 0; i < values.length; ++i) {
                valueStr = i == values.length - 1 ? valueStr + values[i] : valueStr + values[i] + ",";
            }

            params.put(name, valueStr);
        }
        return params;
    }
}
