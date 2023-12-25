package cn.bootx.platform.daxpay.serializer;

import cn.bootx.platform.common.core.util.LocalDateTimeUtil;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * 时间戳转LocalDateTime
 * @author xxm
 * @since 2023/12/25
 */
public class TimestampToLocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {

    @Override
    public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
        long timestamp = p.getLongValue();
        return LocalDateTimeUtil.of(timestamp);
    }
}
