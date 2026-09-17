package cn.daxpay.open.platform.core.util;

import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.assertEquals;

/// 终端展示时区格式化测试
///
/// 沉淀「导出/报文时间必须挂时区」这条约定: 库内与对象上都是 UTC, 不挂时区直接 format 会少 8 小时。
class DateTimeUtilTest {

    /// UTC 09:54:19 对应的本地(东八区)时间
    private static final OffsetDateTime UTC_TIME = OffsetDateTime.of(2026, 9, 17, 9, 54, 19, 0, ZoneOffset.UTC);

    @Test
    void formatByZoneShouldConvertUtcToBusinessZone() {
        assertEquals("2026-09-17 17:54:19", DateTimeUtil.formatByZone(UTC_TIME, DateTimeUtil.ZONE_CST));
    }

    @Test
    void formatByZoneShouldKeepUtcWhenZoneIsUtc() {
        // 不挂时区时的旧行为(输出 UTC 墙上时间), 作为对照固化下来
        assertEquals("2026-09-17 09:54:19", DateTimeUtil.formatByZone(UTC_TIME, ZoneOffset.UTC));
    }

    @Test
    void formatByZoneShouldFallbackToBusinessZoneWhenZoneMissing() {
        assertEquals("2026-09-17 17:54:19", DateTimeUtil.formatByZone(UTC_TIME, null));
    }

    @Test
    void formatByZoneShouldReturnEmptyForNullTime() {
        assertEquals("", DateTimeUtil.formatByZone(null, DateTimeUtil.ZONE_CST));
    }
}
