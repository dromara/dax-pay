package cn.bootx.platform.core.util;

import lombok.experimental.UtilityClass;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * java8 时间工具类
 *
 * @author xxm
 * @since 2020/11/10
 */
@UtilityClass
public class DateTimeUtil {

    /**
     * 大于
     */
    public boolean gt(LocalDateTime now, LocalDateTime next) {
        long mills = now.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        long epochMilli = next.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        return mills > epochMilli;
    }

    /**
     * 小于
     */
    public boolean lt(LocalDateTime now, LocalDateTime next) {
        long mills = now.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        long epochMilli = next.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        return mills < epochMilli;
    }

    /**
     * 大于等于
     */
    public boolean ge(LocalDateTime now, LocalDateTime next) {
        long mills = now.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        long epochMilli = next.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        return mills >= epochMilli;
    }

    /**
     * 小于等于
     */
    public boolean le(LocalDateTime now, LocalDateTime next) {
        long mills = now.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        long epochMilli = next.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        return mills <= epochMilli;
    }

    /**
     * 将localDate转换成localDateTime
     */
    public LocalDateTime date2DateTime(LocalDate localDate) {
        return localDate.atTime(0, 0);

    }

    /**
     * 将long类型的timestamp转为LocalDateTime
     * @param timestamp 时间戳
     * @return LocalDateTime
     */
    public LocalDateTime parse(long timestamp) {
        Instant instant = Instant.ofEpochMilli(timestamp);
        ZoneId zone = ZoneId.systemDefault();
        return LocalDateTime.ofInstant(instant, zone);
    }

    /**
     * LocalDateTime转为long类型的timestamp
     * @param localDateTime 日期时间
     * @return timestamp
     */
    public long timestamp(LocalDateTime localDateTime) {
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = localDateTime.atZone(zone).toInstant();
        return instant.toEpochMilli();
    }

}
