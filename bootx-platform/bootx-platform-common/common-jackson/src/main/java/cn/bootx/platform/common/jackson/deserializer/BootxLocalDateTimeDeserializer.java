package cn.bootx.platform.common.jackson.deserializer;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.LocalDateTimeUtil;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

/**
 * java8日期时间反序列化, 支持反序列化秒和毫秒级别的时间
 * @author xxm
 * @since 2024/1/16
 */
public class BootxLocalDateTimeDeserializer extends LocalDateTimeDeserializer {

    /**
     * 解析日期时间
     */
    @Override
    protected LocalDateTime _fromString(JsonParser p, DeserializationContext ctxt, String string0) {
        // 首先解析毫秒级时间, 如果解析失败, 则解析秒级时间
        try {
            return LocalDateTimeUtil.parse(string0, DatePattern.NORM_DATETIME_MS_PATTERN);
        } catch (DateTimeParseException e) {
            return LocalDateTimeUtil.parse(string0, DatePattern.NORM_DATETIME_PATTERN);
        }
    }
}
