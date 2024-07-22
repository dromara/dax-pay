package cn.bootx.platform.core.util;

import lombok.experimental.UtilityClass;

import java.math.BigDecimal;

/**
 * BigDecimal相关的工具类
 *
 * @author network
 */
@UtilityClass
public class BigDecimalUtil {


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

}
