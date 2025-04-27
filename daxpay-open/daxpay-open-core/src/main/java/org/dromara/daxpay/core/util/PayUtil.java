package org.dromara.daxpay.core.util;

import org.dromara.daxpay.core.enums.AggregatePayTypeEnum;
import org.dromara.daxpay.core.exception.OperationFailException;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.StrUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.experimental.UtilityClass;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
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
     * 获取支付单的超时分钟数, 舍去秒数， 所以会有大约一分钟的误差
     */
    public int getPaymentExpiredTime(LocalDateTime date) {
        Duration duration = LocalDateTimeUtil.between(LocalDateTime.now(), date);
        return Math.toIntExact(duration.getSeconds()/60);
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

    /**
     * 获取付款码类型
     */
    public AggregatePayTypeEnum getBarCodeType(String barCode) {
        // 支付宝
        String[] ali = { "25", "26", "27", "28", "29", "30" };
        if (StrUtil.startWithAny(barCode.substring(0, 2), ali)){
            return AggregatePayTypeEnum.ALIPAY;
        }
        // 微信
        String[] wx = { "10", "11", "12", "13", "14", "15" };
        if (StrUtil.startWithAny(barCode.substring(0, 2), wx)){
            return AggregatePayTypeEnum.WECHAT;
        }
        // 银联
        if (StrUtil.startWith(barCode.substring(0, 2), "62")){
            return AggregatePayTypeEnum.UNION_PAY;
        }
        throw new OperationFailException("不支持的条码类型");
    }
}
