package cn.bootx.platform.common.core.util;

import lombok.experimental.UtilityClass;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

/**
 * BigDecimal相关的工具类
 *
 * @author network
 */
@UtilityClass
public class BigDecimalUtil {

    /**
     * 价钱保留几位小数
     */
    public final int CURRENCY_DECIMAL_PLACES = 2;

    /**
     * 加法
     * @param first 加数
     * @param lastArgs 加数
     * @return 结果
     */
    public BigDecimal add(BigDecimal first, BigDecimal... lastArgs) {

        int argsLength = lastArgs.length;
        BigDecimal result = new BigDecimal("0.00");
        if (first != null) {
            result = result.add(first);
        }
        for (int i = 0; i < argsLength; i++) {
            lastArgs[i] = lastArgs[i] == null ? new BigDecimal("0.00") : lastArgs[i];
            result = result.add(lastArgs[i]);
        }
        result = result.setScale(CURRENCY_DECIMAL_PLACES, RoundingMode.UP);
        return result;
    }

    /**
     * 减法
     * @param first 被减数
     * @param lastArgs 减数
     * @return 结果
     */
    public BigDecimal subtract(BigDecimal first, BigDecimal... lastArgs) {

        int argsLength = lastArgs.length;
        BigDecimal result = new BigDecimal("0.00");
        if (first != null) {
            result = result.add(first);
        }
        for (int i = 0; i < argsLength; i++) {
            lastArgs[i] = lastArgs[i] == null ? new BigDecimal("0.00") : lastArgs[i];
            result = result.subtract(lastArgs[i]);
        }
        result = result.setScale(CURRENCY_DECIMAL_PLACES, RoundingMode.UP);
        return result;
    }

    /**
     * 乘法
     * @param first 乘数
     * @param lastArgs 乘数
     * @return 结果
     */
    public BigDecimal multiply(BigDecimal first, BigDecimal... lastArgs) {

        int argsLength = lastArgs.length;
        BigDecimal result = new BigDecimal("0.00");
        if (first != null) {
            result = result.add(first);
        }
        for (int i = 0; i < argsLength; i++) {
            if (result.compareTo(new BigDecimal("0.00")) == 0) {
                return result;
            }
            lastArgs[i] = lastArgs[i] == null ? new BigDecimal("0.00") : lastArgs[i];
            result = result.multiply(lastArgs[i]);
        }
        result = result.setScale(CURRENCY_DECIMAL_PLACES, RoundingMode.UP);
        return result;
    }

    /**
     * 除法
     * @param first 被除数
     * @param lastArgs 除数
     * @return 结果
     */
    public BigDecimal divide(BigDecimal first, BigDecimal... lastArgs) {

        int argsLength = lastArgs.length;
        BigDecimal result = new BigDecimal("0.00");
        if (first != null) {
            result = result.add(first);
        }
        for (int i = 0; i < argsLength; i++) {
            if (result.compareTo(new BigDecimal("0.00")) == 0) {
                return result;
            }
            lastArgs[i] = lastArgs[i] == null ? new BigDecimal("1.00") : lastArgs[i];
            result = result.divide(lastArgs[i], CURRENCY_DECIMAL_PLACES, RoundingMode.UP);
        }
        result = result.setScale(CURRENCY_DECIMAL_PLACES, RoundingMode.UP);
        return result;
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
     * 获取两位小数的Zero
     * @return 两位小数的Zero
     */
    public BigDecimal getZero() {

        BigDecimal result = new BigDecimal("0.00");
        result = result.setScale(CURRENCY_DECIMAL_PLACES, RoundingMode.UP);
        return result;
    }

    /**
     * 字符串转BigDecimal两位小数
     * @param first 要转换的字符串
     * @return BigDecimal两位小数
     */
    public BigDecimal convertStringToBigDecimal(String first) {

        return BigDecimal.valueOf(Double.parseDouble(first)).setScale(CURRENCY_DECIMAL_PLACES, RoundingMode.UP);
    }

    /**
     * BigDecimal转字符串
     * @param first 要转换的bigDecimal
     * @return 转换后的字符串
     */
    public String toString(BigDecimal first) {

        NumberFormat currency = NumberFormat.getCurrencyInstance(Locale.US);
        if (first == null) {
            return currency.format(new BigDecimal("0.00"));
        }
        else {
            return currency.format(first);
        }
    }

    /**
     * 按比例拆分,用于价格拆分的场景
     * @param totalNumber 需要拆分的价格
     * @param items 需要拆分的项，以及每项所占的比例
     * @return 拆分后每项的价格
     */
    public <E> Map<E, BigDecimal> averageNumber(BigDecimal totalNumber, Map<E, BigDecimal> items) {
        return averageNumber(totalNumber, items, 2, RoundingMode.UP);
    }

    /**
     * 按比例拆分给定的数字,用于价格拆分的场景
     * @param totalNumber 需要拆分的价格
     * @param items 需要拆分的项，key 为每项的唯一标识，value为每项的数字
     * @param scale 需要保留的位数
     * @param roundingMode 舍入模式
     * @return 拆分后每项的数字
     */
    public <E> Map<E, BigDecimal> averageNumber(BigDecimal totalNumber, Map<E, BigDecimal> items, int scale,
            RoundingMode roundingMode) {
        BigDecimal number = items.values().stream().reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal remainderNumber = totalNumber;
        Map<E, BigDecimal> result = new LinkedHashMap<>();

        int i = 1;
        for (Map.Entry<E, BigDecimal> entry : items.entrySet()) {
            if (i == items.entrySet().size()) {
                result.put(entry.getKey(), remainderNumber);
                break;
            }
            BigDecimal value = BigDecimal.ZERO;
            if (BigDecimal.ZERO.compareTo(number) != 0) {
                value = (totalNumber.multiply(entry.getValue())).divide(number, scale, roundingMode);
            }

            result.put(entry.getKey(), value);
            remainderNumber = remainderNumber.subtract(value);
            i += 1;
        }

        return result;
    }

}
