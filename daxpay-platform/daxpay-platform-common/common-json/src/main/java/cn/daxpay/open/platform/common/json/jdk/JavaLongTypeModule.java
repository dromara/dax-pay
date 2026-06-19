package cn.daxpay.open.platform.common.json.jdk;

import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonGenerator;
import tools.jackson.databind.SerializationContext;
import tools.jackson.databind.ValueSerializer;
import tools.jackson.databind.module.SimpleModule;

/// # Long 类型序列化模块
///
/// 将 Long/long 类型序列化为字符串, 防止前端 JavaScript 精度丢失
public class JavaLongTypeModule extends SimpleModule {

    public JavaLongTypeModule() {
        addSerializer(Long.class, new LongToStringSerializer());
        addSerializer(Long.TYPE, new LongToStringSerializer());
    }

    static class LongToStringSerializer extends ValueSerializer<Long> {
        @Override
        public void serialize(Long value, JsonGenerator gen, SerializationContext ctxt) throws JacksonException {
            gen.writeString(value.toString());
        }
    }
}