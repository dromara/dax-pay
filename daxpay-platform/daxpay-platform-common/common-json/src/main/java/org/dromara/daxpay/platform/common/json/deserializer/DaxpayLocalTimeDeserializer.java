package org.dromara.daxpay.platform.common.json.deserializer;

import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonParser;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.ValueDeserializer;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/// # LocalTime 反序列化器, 支持 HH:mm:ss 和 HH:mm:ss.SSS 两种格式
///
public class DaxpayLocalTimeDeserializer extends ValueDeserializer<LocalTime> {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");
    private static final DateTimeFormatter FORMATTER_MS = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");

    @Override
    public LocalTime deserialize(JsonParser p, DeserializationContext ctxt) throws JacksonException {
        String text = p.getString();
        if (text == null || text.isBlank()) {
            return null;
        }
        try {
            if (text.contains(".")) {
                return LocalTime.parse(text, FORMATTER_MS);
            }
            return LocalTime.parse(text, FORMATTER);
        } catch (Exception e) {
            return LocalTime.parse(text);
        }
    }
}