package org.dromara.daxpay.core.util;

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
