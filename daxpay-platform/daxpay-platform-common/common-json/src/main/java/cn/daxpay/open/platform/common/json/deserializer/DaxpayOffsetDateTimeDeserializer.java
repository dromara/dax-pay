package cn.daxpay.open.platform.common.json.deserializer;

import cn.daxpay.open.platform.common.json.util.OffsetDateTimeParseUtil;
import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonParser;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.ValueDeserializer;

import java.time.OffsetDateTime;

/// # OffsetDateTime 反序列化器
/// 解析逻辑详见 [OffsetDateTimeParseUtil], 支持三种格式: ISO 带偏移 / `yyyy-MM-dd HH:mm:ss` / `yyyy-MM-dd HH:mm:ss.SSS`
public class DaxpayOffsetDateTimeDeserializer extends ValueDeserializer<OffsetDateTime> {

    @Override
    public OffsetDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws JacksonException {
        return OffsetDateTimeParseUtil.parse(p.getString());
    }
}
