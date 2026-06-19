package cn.daxpay.open.platform.core.util;

import lombok.experimental.UtilityClass;

import java.time.*;

/// # java8 时间工具类
///
/// 所有时间基于 UTC，与数据库 timestamptz 列保持一致
/// 前端展示时由客户端按用户时区转换
@UtilityClass
public class DateTimeUtil {

    /// 大于
    public boolean gt(OffsetDateTime now, OffsetDateTime next) {
        return now.toEpochSecond() > next.toEpochSecond();
    }

    /// 小于
    public boolean lt(OffsetDateTime now, OffsetDateTime next) {
        return now.toEpochSecond() < next.toEpochSecond();
    }

    /// 大于等于
    public boolean ge(OffsetDateTime now, OffsetDateTime next) {
        return now.toEpochSecond() >= next.toEpochSecond();
    }

    /// 小于等于
    public boolean le(OffsetDateTime now, OffsetDateTime next) {
        return now.toEpochSecond() <= next.toEpochSecond();
    }

    /// 获取当前 UTC 时间
    public OffsetDateTime nowUtc() {
        return OffsetDateTime.now(ZoneOffset.UTC);
    }

    /// 将 long 类型的 timestamp 转为 OffsetDateTime (UTC)
    public OffsetDateTime fromEpochMilli(long timestamp) {
        return Instant.ofEpochMilli(timestamp).atOffset(ZoneOffset.UTC);
    }

    /// OffsetDateTime 转为 long 类型的 timestamp
    public long toEpochMilli(OffsetDateTime dateTime) {
        return dateTime.toInstant().toEpochMilli();
    }

    /// 将 localDate 转换成当天开始时间的 OffsetDateTime (UTC)
    /// 用于日期范围查询等场景
    public OffsetDateTime dateStartUtc(LocalDate localDate) {
        return localDate.atStartOfDay(ZoneOffset.UTC).toOffsetDateTime();
    }

    /// 将 localDate 转换成当天结束时间的 OffsetDateTime (UTC)
    public OffsetDateTime dateEndUtc(LocalDate localDate) {
        return localDate.atTime(LocalTime.MAX).atZone(ZoneOffset.UTC).toOffsetDateTime();
    }

    /// 旧版兼容: 将 Asia/Shanghai 时区的 LocalDateTime 转为 UTC OffsetDateTime
    public OffsetDateTime shanghaiToUtc(LocalDateTime shanghaiTime) {
        if (shanghaiTime == null) return null;
        return shanghaiTime.atOffset(ZoneOffset.ofHours(8)).withOffsetSameInstant(ZoneOffset.UTC);
    }

    /// 旧版兼容: 将 UTC OffsetDateTime 转为 Asia/Shanghai 时区的 LocalDateTime
    public LocalDateTime utcToShanghai(OffsetDateTime utcTime) {
        if (utcTime == null) return null;
        return utcTime.withOffsetSameInstant(ZoneOffset.ofHours(8)).toLocalDateTime();
    }

    /// 格式化为 ISO 8601 标准时间字符串 (UTC)
    public String formatIso(OffsetDateTime dateTime) {
        if (dateTime == null) return null;
        return dateTime.withOffsetSameInstant(ZoneOffset.UTC).toString();
    }

}

