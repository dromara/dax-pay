package org.dromara.daxpay.platform.common.json.configuration;

import jakarta.annotation.PostConstruct;
import org.dromara.daxpay.platform.common.json.jdk.Java8TimeFormatModule;
import org.dromara.daxpay.platform.common.json.jdk.JavaLongTypeModule;
import org.dromara.daxpay.platform.common.json.util.JacksonUtil;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.jackson.autoconfigure.JsonMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.SerializationFeature;

import java.util.TimeZone;

/// # jackson 序列化自动配置
///
/// 使用 Spring Boot 4 标准的 JsonMapperBuilderCustomizer 定制机制, 不手动创建 ObjectMapper
@Configuration
public class JacksonConfiguration {

    /// 定制 JsonMapper Builder, 配置全局序列化行为
    /// 通过 Spring Boot 标准的 JsonMapperBuilderCustomizer 机制, 让 auto-configuration 创建的 JsonMapper 自动应用这些配置
    @Bean
    public JsonMapperBuilderCustomizer jacksonCustomizer() {
        return builder -> builder
                // 指定要序列化的域
                .changeDefaultVisibility(vc -> vc.withVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY))
                // Jackson 3 默认 WRITE_DATES_AS_TIMESTAMPS=false (DateTimeFeature), 且已有 Java8TimeFormatModule 覆盖格式, 无需额外配置
                // 忽略未知属性
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                // 对象属性为空时可以序列化
                .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
                // Long 类型序列化为字符串, 防止前端精度丢失
                .addModule(new JavaLongTypeModule())
                // Java 8 时间类型格式定制 (OffsetDateTime/LocalDate/LocalTime)
                .addModule(new Java8TimeFormatModule());
    }

    /// 初始化 JacksonUtil, 将 auto-configuration 创建的 JsonMapper 注入到工具类
    @Bean
    InitializingBean jacksonUtilInitializer(ObjectMapper objectMapper) {
        return () -> JacksonUtil.setObjectMapper(objectMapper);
    }

    /// 初始化时设置全局时区为 UTC
    @PostConstruct
    public void init() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }

    /// 序列化时忽略空值
    @Bean
    public ObjectMapper ignoreNullObjectMapper(ObjectMapper objectMapper) {
        ObjectMapper copy = objectMapper.rebuild()
                // null 值不序列化
                .changeDefaultPropertyInclusion(ic -> ic.withValueInclusion(JsonInclude.Include.NON_NULL))
                .build();
        JacksonUtil.setIgnoreNullObjectMapper(copy);
        return copy;
    }

}