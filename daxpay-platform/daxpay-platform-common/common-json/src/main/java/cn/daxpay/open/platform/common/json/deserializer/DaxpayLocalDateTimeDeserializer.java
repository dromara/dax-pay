package cn.daxpay.open.platform.common.json.deserializer;

import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonParser;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.ValueDeserializer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/// # LocalDateTime 反序列化器, 支持 yyyy-MM-dd HH:mm:ss 和 yyyy-MM-dd HH:mm:ss.SSS 两种格式
///
public class DaxpayLocalDateTimeDeserializer extends ValueDeserializer<LocalDateTime> {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter FORMATTER_MS = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    @Override
    public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws JacksonException {
        String text = p.getString();
        if (text == null || text.isBlank()) {
            return null;
        }
        try {
            if (text.contains(".")) {
                return LocalDateTime.parse(text, FORMATTER_MS);
            }
            return LocalDateTime.parse(text, FORMATTER);
        } catch (Exception e) {
            return LocalDateTime.parse(text);
        }
    }
}