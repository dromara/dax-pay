package cn.bootx.platform.daxpay.serializer;

import cn.bootx.platform.common.core.util.LocalDateTimeUtil;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Java8时间类型序列化为时间戳 (10位秒级时间戳)
 * @author xxm
 * @since 2024/2/7
 */
public class LocalDateTimeToTimestampSerializer extends JsonSerializer<LocalDateTime> {
    @Override
    public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (Objects.nonNull(value)){
            long timestamp = LocalDateTimeUtil.timestamp(value)/1000;
            gen.writeNumber(timestamp);
        } else {
            gen.writeNull();
        }
    }
}
