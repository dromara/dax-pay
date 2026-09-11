package cn.daxpay.open.platform.common.json.jdk;

import cn.daxpay.open.platform.common.json.deserializer.DaxpayOffsetDateTimeDeserializer;
import cn.daxpay.open.platform.common.json.deserializer.DaxpayLocalTimeDeserializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonGenerator;
import tools.jackson.databind.BeanProperty;
import tools.jackson.databind.SerializationConfig;
import tools.jackson.databind.SerializationContext;
import tools.jackson.databind.ValueSerializer;
import tools.jackson.databind.module.SimpleModule;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.TimeZone;

/// # Java 8 时间类型格式定制模块
///
/// Jackson 3 内置了时间类型支持, 此模块仅覆盖默认的序列化格式
/// OffsetDateTime -> yyyy-MM-ddTHH:mm:ssZ (UTC, 秒精度无小数); 字段级 @JsonFormat 可覆盖
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

        /// 平台默认时区基准: 统一按 UTC 归一输出
        private static final ZoneId DEFAULT_ZONE = ZoneOffset.UTC;

        /// 实际生效的输出格式(字段无 @JsonFormat 时为默认 ISO 秒精度)
        private final DateTimeFormatter formatter;

        /// 实际生效的输出时区(输出前把同一瞬时换算到该时区)
        private final ZoneId zone;

        OffsetDateTimeToStringSerializer() {
            this(FORMATTER, DEFAULT_ZONE);
        }

        OffsetDateTimeToStringSerializer(DateTimeFormatter formatter, ZoneId zone) {
            this.formatter = formatter;
            this.zone = zone;
        }

        /// 识别字段级 @JsonFormat 并据此定制输出
        ///
        /// 平台默认保持 UTC 秒精度 ISO; 对外开放契约(unipay)的时间字段通过
        /// `@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")` 输出北京时间字面量。
        ///
        /// 注意: 反序列化器 [DaxpayOffsetDateTimeDeserializer] **刻意不感知注解**, 保持宽松解析
        /// (同时兼容 ISO 带偏移与旧格式), 避免历史调用方因格式收紧而绑定失败。这一非对称是有意为之。
        @Override
        public ValueSerializer<?> createContextual(SerializationContext ctxt, BeanProperty property) {
            if (Objects.isNull(property)) {
                return this;
            }
            return customize(property.findPropertyFormat(ctxt.getConfig(), OffsetDateTime.class));
        }

        /// 字段级格式覆盖(Jackson 3 的并行钩子, 与 [createContextual] 同源处理, 避免只走其中一条链路)
        @Override
        public ValueSerializer<?> withFormatOverrides(SerializationConfig config, JsonFormat.Value formatOverrides) {
            return customize(formatOverrides);
        }

        /// 按 @JsonFormat 的 pattern / timezone 生成定制序列化器; 未声明 pattern 时回退默认
        private ValueSerializer<?> customize(JsonFormat.Value format) {
            if (Objects.isNull(format) || Objects.isNull(format.getPattern()) || format.getPattern().isBlank()) {
                return this;
            }
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format.getPattern());
            TimeZone timeZone = format.getTimeZone();
            ZoneId zone = Objects.isNull(timeZone) ? DEFAULT_ZONE : timeZone.toZoneId();
            return new OffsetDateTimeToStringSerializer(formatter, zone);
        }

        @Override
        public void serialize(OffsetDateTime value, JsonGenerator gen, SerializationContext ctxt) throws JacksonException {
            // 把同一瞬时换算到目标时区(区域时区自动处理夏令时), 再按规定格式输出, 不带时区后缀
            ZoneOffset offset = zone.getRules().getOffset(value.toInstant());
            gen.writeString(value.withOffsetSameInstant(offset).format(formatter));
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
