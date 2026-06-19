package cn.daxpay.open.platform.common.json.deserializer;

import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonParser;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.ValueDeserializer;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/// # OffsetDateTime 反序列化器, 支持三种格式:
/// ## - ISO 8601 带偏移: 2026-06-13T08:30:00Z 或 2026-06-13T16:30:00+08:00
/// ## - 旧格式: 2026-06-13 16:30:00 (按 Asia/Shanghai 时区解析后转 UTC)
/// ## - 旧格式毫秒: 2026-06-13 16:30:00.123
///
public class DaxpayOffsetDateTimeDeserializer extends ValueDeserializer<OffsetDateTime> {

    private static final DateTimeFormatter FORMATTER_OLD = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter FORMATTER_OLD_MS = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
    private static final ZoneOffset SHANGHAI_OFFSET = ZoneOffset.ofHours(8);

    @Override
    public OffsetDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws JacksonException {
        String text = p.getString();
        if (text == null || text.isBlank()) {
            return null;
        }
        try {
            return OffsetDateTime.parse(text);
        } catch (Exception ignored) {
        }
        try {
            if (text.contains(".")) {
                LocalDateTime localDateTime = LocalDateTime.parse(text, FORMATTER_OLD_MS);
                return localDateTime.atOffset(SHANGHAI_OFFSET).withOffsetSameInstant(ZoneOffset.UTC);
            }
            LocalDateTime localDateTime = LocalDateTime.parse(text, FORMATTER_OLD);
            return localDateTime.atOffset(SHANGHAI_OFFSET).withOffsetSameInstant(ZoneOffset.UTC);
        } catch (Exception e) {
            throw new RuntimeException("无法解析日期时间: " + text, e);
        }
    }
}
