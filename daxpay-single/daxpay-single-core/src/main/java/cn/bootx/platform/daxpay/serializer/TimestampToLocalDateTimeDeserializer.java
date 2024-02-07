package cn.bootx.platform.daxpay.serializer;

import cn.bootx.platform.common.core.util.LocalDateTimeUtil;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * 时间戳转LocalDateTime(10位秒级时间戳)
 * @author xxm
 * @since 2023/12/25
 */
public class TimestampToLocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {

    @Override
    public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String value = p.getValueAsString();
        if (StrUtil.isBlank(value)){
            return null;
        }
        long timestamp = Long.parseLong(value) * 1000;
        return LocalDateTimeUtil.of(timestamp);
    }
}
