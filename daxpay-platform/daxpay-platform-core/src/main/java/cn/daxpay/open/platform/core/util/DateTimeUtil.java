package cn.daxpay.open.platform.core.util;

import lombok.experimental.UtilityClass;

import java.time.OffsetDateTime;

/// # java8 时间工具类
///
/// 所有时间基于 UTC，与数据库 timestamptz 列保持一致
/// 前端展示时由客户端按用户时区转换
@UtilityClass
public class DateTimeUtil {

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

}
