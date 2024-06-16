package cn.daxpay.single.util;

import cn.bootx.platform.common.core.util.LocalDateTimeUtil;
import cn.hutool.core.date.DatePattern;
import lombok.experimental.UtilityClass;

import javax.servlet.http.HttpServletRequest;
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
