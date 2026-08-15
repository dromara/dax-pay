package cn.daxpay.open.platform.capability.cache.core;

import cn.daxpay.open.platform.common.json.jdk.Java8TimeFormatModule;
import cn.daxpay.open.platform.common.json.jdk.JavaLongTypeModule;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.redis.serializer.JacksonJsonRedisSerializer;
import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.json.JsonMapper;

import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

/// # POJO 缓存定型序列化器机制测试
///
/// 验证 [CacheValueTypeContributor] 机制依赖的定型序列化器行为:
/// 平台同款 ObjectMapper(Long→String + 秒精度 UTC 时间 + 忽略未知属性)下,
/// POJO 经 serialize→deserialize 还原为真实类型而非 LinkedHashMap,
/// Long(超 int 精度)/OffsetDateTime/boolean/嵌套 List 全部保真;
/// 泛型容器(List&lt;POJO&gt;)经 [tools.jackson.databind.type.TypeFactory] 构造 JavaType 注册后同样保真。
///
/// 这是对 2026-07-30 f400b164c「反序列化丢失类型」缺陷治本方案的机制锚点测试。
class JacksonJsonRedisSerializerRoundTripTest {

    private static JacksonJsonRedisSerializer<RouteLike> serializer;

    private static JacksonJsonRedisSerializer<List<RouteLikeItem>> listSerializer;

    @BeforeAll
    static void initSerializer() {
        // 与平台 JacksonConfiguration 同款配置, 保证测试行为与运行时一致
        JsonMapper mapper = JsonMapper.builder()
                .changeDefaultVisibility(vc -> vc.withVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY))
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .addModule(new JavaLongTypeModule())
                .addModule(new Java8TimeFormatModule())
                .build();
        serializer = new JacksonJsonRedisSerializer<>(mapper, RouteLike.class);
        // 泛型容器: JavaType 携带元素类型(直接用 List.class 会因类型擦除退化为 List<LinkedHashMap>)
        listSerializer = new JacksonJsonRedisSerializer<>(mapper,
                mapper.getTypeFactory().constructCollectionType(List.class, RouteLikeItem.class));
    }

    @Test
    @DisplayName("POJO round-trip 还原真实类型, Long(超 int 精度)/OffsetDateTime/嵌套 List 保真")
    void shouldRoundTripPojoWithRealType() {
        RouteLikeItem item = new RouteLikeItem();
        item.setId(1934567890123456789L);
        item.setChannelMchNo("M8666688");

        RouteLike original = new RouteLike();
        original.setId(987654321012345678L);
        original.setAppId("A001");
        original.setDeleted(true);
        // 秒精度输入(平台序列化强制 UTC 秒精度, 纳秒会被截断)
        original.setCreateTime(OffsetDateTime.of(2026, 8, 14, 18, 30, 0, 0, ZoneOffset.ofHours(8)));
        original.setItems(List.of(item));

        RouteLike restored = serializer.deserialize(serializer.serialize(original));

        assertInstanceOf(RouteLike.class, restored);
        assertEquals(original.getId(), restored.getId());
        assertEquals(original.getAppId(), restored.getAppId());
        assertTrue(restored.isDeleted());
        // 时间序列化强制转 UTC, 按瞬时比较
        assertEquals(original.getCreateTime().toInstant(), restored.getCreateTime().toInstant());
        assertEquals(1, restored.getItems().size());
        assertInstanceOf(RouteLikeItem.class, restored.getItems().get(0));
        assertEquals(item.getId(), restored.getItems().get(0).getId());
        assertEquals(item.getChannelMchNo(), restored.getItems().get(0).getChannelMchNo());
    }

    @Test
    @DisplayName("泛型容器 List<POJO> 经 JavaType 注册后元素还原真实类型(非 LinkedHashMap)")
    void shouldRoundTripGenericListWithRealElementType() {
        RouteLikeItem first = new RouteLikeItem();
        first.setId(1934567890123456789L);
        first.setChannelMchNo("M8666688");

        RouteLikeItem second = new RouteLikeItem();
        second.setId(2L);
        second.setChannelMchNo("M9999999");

        List<RouteLikeItem> restored = listSerializer.deserialize(listSerializer.serialize(List.of(first, second)));

        assertEquals(2, restored.size());
        // 元素是真实类型而非 LinkedHashMap —— 泛型容器场景的回归锚点
        assertInstanceOf(RouteLikeItem.class, restored.get(0));
        assertInstanceOf(RouteLikeItem.class, restored.get(1));
        assertEquals(first.getId(), restored.get(0).getId());
        assertEquals(first.getChannelMchNo(), restored.get(0).getChannelMchNo());
        assertEquals(second.getChannelMchNo(), restored.get(1).getChannelMchNo());
    }

    @Test
    @DisplayName("Long 序列化为字符串(String→Long 宽松转换回读), 产物可直接查验")
    void shouldSerializeLongAsString() {
        RouteLike value = new RouteLike();
        value.setId(123456789L);

        String json = new String(serializer.serialize(value), StandardCharsets.UTF_8);

        assertTrue(json.contains("\"123456789\""), "Long 应序列化为字符串形式: " + json);
    }

    @Test
    @DisplayName("实体加字段后旧 JSON 反序列化不失败(忽略未知属性, 缓存值前向兼容)")
    void shouldIgnoreUnknownProperties() {
        String json = "{\"id\":1,\"newFutureField\":\"x\"}";

        RouteLike restored = serializer.deserialize(json.getBytes(StandardCharsets.UTF_8));

        assertEquals(1L, restored.getId());
    }

    @Getter
    @Setter
    static class RouteLike {

        private Long id;

        private String appId;

        private boolean deleted;

        private OffsetDateTime createTime;

        private List<RouteLikeItem> items;
    }

    @Getter
    @Setter
    static class RouteLikeItem {

        private Long id;

        private String channelMchNo;
    }
}
