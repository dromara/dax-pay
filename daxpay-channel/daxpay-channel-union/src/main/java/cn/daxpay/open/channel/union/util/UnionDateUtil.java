package cn.daxpay.open.channel.union.util;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/// # 云闪付通道日期工具(主应用侧)
///
/// 银联 ACP 时间字段(yyyyMMddHHmmss)解析为 [OffsetDateTime], 与子应用侧工具对称。
public final class UnionDateUtil {

    /// 银联时间格式(yyyyMMddHHmmss)
    private static final DateTimeFormatter TXN_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    /// 东八区偏移(北京时间)
    private static final ZoneOffset CST = ZoneOffset.ofHours(8);

    private UnionDateUtil() {
    }

    /// 解析银联返回的东八区时间字符串(yyyyMMddHHmmss)为 [OffsetDateTime]
    ///
    /// 银联返回无时区时间字面量, 先用 [LocalDateTime] 接住再附加东八区偏移。
    public static OffsetDateTime parseCst(String text) {
        if (text == null || text.isBlank()) {
            return null;
        }
        return LocalDateTime.parse(text, TXN_TIME_FORMATTER).atOffset(CST);
    }
}
