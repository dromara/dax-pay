package cn.daxpay.open.payment.common.json;

import cn.daxpay.open.platform.common.json.jdk.JavaLongTypeModule;
import cn.daxpay.open.platform.common.json.jdk.Java8TimeFormatModule;
import org.springframework.stereotype.Component;
import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonGenerator;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.SerializationContext;
import tools.jackson.databind.ValueSerializer;
import tools.jackson.databind.module.SimpleModule;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/// # 对外开放接口(unipay)契约报文序列化器
///
/// 与平台主 mapper **同源配置**([ObjectMapper#rebuild]), 仅把 `OffsetDateTime` 的输出替换为
/// 北京时间(见 [UnipayTimeFormat])。
///
/// ## 为什么需要它
///
/// unipay 契约报文里的时间有两种来源:
/// - **契约 DTO 字段**(`DaxResult.resTime` / 查询结果等): 直接加字段注解
///   `@JsonFormat(pattern = UnipayTimeFormat.PATTERN, timezone = UnipayTimeFormat.ZONE)`, 走主 mapper 即可;
/// - **内部 DTO 字段**(如商户通知内容快照 `data.*`): 内部 DTO 被运营端/商户端接口共用,
///   **不能加注解**(加了会把北京时间带到内部接口, 破坏前端按用户时区渲染),
///   故用本序列化器在生成契约报文时单独转换。
@Component
public class UnipayJson {

    private final ObjectMapper objectMapper;

    public UnipayJson(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper.rebuild()
                // 与主 mapper 保持一致的长整型/常规时间处理, 仅覆盖 OffsetDateTime 输出
                .addModule(new JavaLongTypeModule())
                .addModule(new Java8TimeFormatModule())
                .addModule(new BeijingTimeModule())
                .build();
    }

    /// 序列化为 unipay 契约报文
    public String toJson(Object value) {
        return objectMapper.writeValueAsString(value);
    }

    /// 时间格式模块: OffsetDateTime → 北京时间(秒精度, 无时区后缀)
    static class BeijingTimeModule extends SimpleModule {
        BeijingTimeModule() {
            // 注册在主模块之后, 同类型序列化器后者生效
            addSerializer(OffsetDateTime.class, new BeijingTimeSerializer());
        }
    }

    /// 北京时间序列化器
    static class BeijingTimeSerializer extends ValueSerializer<OffsetDateTime> {

        private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(UnipayTimeFormat.PATTERN);

        private static final ZoneId ZONE = ZoneId.of(UnipayTimeFormat.ZONE);

        @Override
        public void serialize(OffsetDateTime value, JsonGenerator gen, SerializationContext ctxt) throws JacksonException {
            // 把同一瞬时换算到东八区后按规定格式输出, 不带时区后缀
            ZoneOffset offset = ZONE.getRules().getOffset(value.toInstant());
            gen.writeString(value.withOffsetSameInstant(offset).format(FORMATTER));
        }
    }
}
