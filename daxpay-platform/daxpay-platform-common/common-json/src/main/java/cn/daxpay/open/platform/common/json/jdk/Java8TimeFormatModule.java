package cn.daxpay.open.platform.common.json.jdk;

import cn.daxpay.open.platform.common.json.deserializer.DaxpayOffsetDateTimeDeserializer;
import cn.daxpay.open.platform.common.json.deserializer.DaxpayLocalTimeDeserializer;
import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonGenerator;
import tools.jackson.databind.SerializationContext;
import tools.jackson.databind.ValueSerializer;
import tools.jackson.databind.module.SimpleModule;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/// # Java 8 时间类型格式定制模块
///
/// Jackson 3 内置了时间类型支持, 此模块仅覆盖默认的序列化格式
/// OffsetDateTime -> yyyy-MM-ddTHH:mm:ssZ (UTC, 秒精度无小数)
/// LocalDate -> yyyy-MM-dd
/// LocalTime -> HH:mm:ss
public class Java8TimeFormatModule extends SimpleModule {

    private static final String NORM_DATE_PATTERN = "yyyy-MM-dd";
    private static final String NORM_TIME_PATTERN = "HH:mm:ss";

    public Java8TimeFormatModule() {
        addSerializer(OffsetDateTime.class, new OffsetDateTimeToStringSerializer());
        addSerializer(LocalDate.class, new LocalDateToStringSerializer());
        addSerializer(LocalTime.class, new LocalTimeToStringSerializer());
        addDeserializer(OffsetDateTime.class, new DaxpayOffsetDateTimeDeserializer());
        addDeserializer(LocalTime.class, new DaxpayLocalTimeDeserializer());
    }

    static class OffsetDateTimeToStringSerializer extends ValueSerializer<OffsetDateTime> {
        // 秒精度 ISO 8601 偏移时间格式, 避免纳秒级精度输出 (如 .0546373) 造成视觉混乱与协议冗余
        // 支付场景秒级足够, 符合行业惯例 (Stripe / 微信支付 / 支付宝)
        private static final DateTimeFormatter FORMATTER =
                DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX");

        @Override
        public void serialize(OffsetDateTime value, JsonGenerator gen, SerializationContext ctxt) throws JacksonException {
            gen.writeString(value.withOffsetSameInstant(ZoneOffset.UTC).format(FORMATTER));
        }
    }

    static class LocalDateToStringSerializer extends ValueSerializer<LocalDate> {
        private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(NORM_DATE_PATTERN);
        @Override
        public void serialize(LocalDate value, JsonGenerator gen, SerializationContext ctxt) throws JacksonException {
            gen.writeString(value.format(formatter));
        }
    }

    static class LocalTimeToStringSerializer extends ValueSerializer<LocalTime> {
        private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(NORM_TIME_PATTERN);
        @Override
        public void serialize(LocalTime value, JsonGenerator gen, SerializationContext ctxt) throws JacksonException {
            gen.writeString(value.format(formatter));
        }
    }
}
