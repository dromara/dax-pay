package cn.daxpay.open.platform.common.json.util;

import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/// # OffsetDateTime 字符串解析工具
/// 供 Jackson 反序列化器与 Spring query/form 参数 [Converter] 共用,
/// 保证 JSON body 与 GET query / form 表单两条绑定路径的解析行为一致.
///
/// 支持三种格式:
/// ## - ISO 8601 带偏移: 2026-06-13T08:30:00Z 或 2026-06-13T16:30:00+08:00
/// ## - 旧格式: 2026-06-13 16:30:00 (按东八区解析后转 UTC)
/// ## - 旧格式毫秒: 2026-06-13 16:30:00.123
@UtilityClass
public class OffsetDateTimeParseUtil {

    private static final DateTimeFormatter FORMATTER_OLD = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private static final DateTimeFormatter FORMATTER_OLD_MS = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    private static final ZoneOffset SHANGHAI_OFFSET = ZoneOffset.ofHours(8);

    /// 将字符串解析为 OffsetDateTime
    public OffsetDateTime parse(String text) {
        if (text == null || text.isBlank()) {
            return null;
        }
        // 优先按 ISO 带偏移解析
        try {
            return OffsetDateTime.parse(text);
        } catch (Exception ignored) {
        }
        // 回退: 旧格式(无时区字面量), 先用 LocalDateTime 接住, 再附加东八区偏移转为 UTC
        try {
            DateTimeFormatter formatter = text.contains(".")
                    ? FORMATTER_OLD_MS
                    : FORMATTER_OLD;
            LocalDateTime localDateTime = LocalDateTime.parse(text, formatter);
            return localDateTime.atOffset(SHANGHAI_OFFSET).withOffsetSameInstant(ZoneOffset.UTC);
        } catch (Exception e) {
            throw new RuntimeException("无法解析日期时间: " + text, e);
        }
    }
}
