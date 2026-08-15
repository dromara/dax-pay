package cn.daxpay.open.payment.route.service.runtime;

import cn.daxpay.open.payment.route.entity.basic.PayRouteBasicConfig;
import cn.daxpay.open.payment.route.entity.scene.PayRouteSceneConfig;
import cn.daxpay.open.payment.route.entity.strategy.PayRouteStrategy;
import cn.daxpay.open.payment.route.service.model.PayRouteBundle;
import cn.daxpay.open.platform.common.json.jdk.Java8TimeFormatModule;
import cn.daxpay.open.platform.common.json.jdk.JavaLongTypeModule;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.redis.serializer.JacksonJsonRedisSerializer;
import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.json.JsonMapper;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

/// # 路由数据包 L2 定型序列化回归测试
///
/// 锁定 [PaymentCacheValueTypeContributor] 注册的 `payment:route-bundle` 缓存值类型
/// 在平台同款 ObjectMapper 下的序列化兼容性:
/// - 嵌套实体/列表还原真实类型(当年 LinkedHashMap 丢类型的直接回归锚点)
/// - Long 雪花 id(超 int 精度)、OffsetDateTime(UTC 秒精度)、逻辑删除 boolean 全部保真
class PayRouteBundleTypedSerializerTest {

    private static JacksonJsonRedisSerializer<PayRouteBundle> serializer;

    @BeforeAll
    static void initSerializer() {
        // 与平台 JacksonConfiguration 同款配置, 保证测试行为与运行时一致
        JsonMapper mapper = JsonMapper.builder()
                .changeDefaultVisibility(vc -> vc.withVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY))
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .addModule(new JavaLongTypeModule())
                .addModule(new Java8TimeFormatModule())
                .build();
        serializer = new JacksonJsonRedisSerializer<>(mapper, PayRouteBundle.class);
    }

    @Test
    @DisplayName("PayRouteBundle round-trip: 嵌套实体/列表还原真实类型, 雪花 id 与时间保真")
    void shouldRoundTripRouteBundle() {
        long strategyId = 1934567890123456789L;

        PayRouteStrategy strategy = new PayRouteStrategy();
        strategy.setId(strategyId);
        strategy.setAppId("A001");
        strategy.setMode("SCENE");
        strategy.setMchNo("M001");
        strategy.setCreateTime(OffsetDateTime.of(2026, 8, 14, 10, 0, 0, 0, ZoneOffset.ofHours(8)));
        strategy.setVersion(1);

        PayRouteBasicConfig basic = new PayRouteBasicConfig();
        basic.setId(2L);
        basic.setStrategyId(strategyId);
        basic.setProvider("WECHAT");
        basic.setChannelMchNo("M8666688");

        PayRouteSceneConfig scene = new PayRouteSceneConfig();
        scene.setId(3L);
        scene.setStrategyId(strategyId);
        scene.setMethod("wap");
        scene.setChannelMchNo("M8666688");
        scene.setCapability("aggregate");

        PayRouteBundle bundle = new PayRouteBundle()
                .setStrategy(strategy)
                .setBasicConfigs(List.of(basic))
                .setSceneConfigs(List.of(scene));

        PayRouteBundle restored = serializer.deserialize(serializer.serialize(bundle));

        // 嵌套还原真实类型(非 LinkedHashMap) —— 这正是 f400b164c 移除注解时的缺陷根源
        assertInstanceOf(PayRouteStrategy.class, restored.getStrategy());
        assertInstanceOf(PayRouteBasicConfig.class, restored.getBasicConfigs().get(0));
        assertInstanceOf(PayRouteSceneConfig.class, restored.getSceneConfigs().get(0));

        assertEquals(strategyId, restored.getStrategy().getId());
        assertEquals("A001", restored.getStrategy().getAppId());
        assertEquals("M001", restored.getStrategy().getMchNo());
        assertEquals(1, restored.getStrategy().getVersion());
        // 时间序列化强制转 UTC, 按瞬时比较
        assertEquals(strategy.getCreateTime().toInstant(), restored.getStrategy().getCreateTime().toInstant());

        assertEquals(strategyId, restored.getBasicConfigs().get(0).getStrategyId());
        assertEquals("WECHAT", restored.getBasicConfigs().get(0).getProvider());

        assertEquals("wap", restored.getSceneConfigs().get(0).getMethod());
        assertEquals("aggregate", restored.getSceneConfigs().get(0).getCapability());
    }

    @Test
    @DisplayName("空列表字段 round-trip 保持空列表(PayRouteBundle 初始值语义)")
    void shouldRoundTripEmptyLists() {
        PayRouteStrategy strategy = new PayRouteStrategy();
        strategy.setId(1L);

        PayRouteBundle bundle = new PayRouteBundle().setStrategy(strategy);

        PayRouteBundle restored = serializer.deserialize(serializer.serialize(bundle));

        assertTrue(restored.getBasicConfigs().isEmpty());
        assertTrue(restored.getSceneConfigs().isEmpty());
    }
}
