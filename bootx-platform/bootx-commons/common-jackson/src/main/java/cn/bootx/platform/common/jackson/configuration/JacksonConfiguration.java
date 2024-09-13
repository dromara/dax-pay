package cn.bootx.platform.common.jackson.configuration;

import cn.bootx.platform.common.jackson.jdk.Java8TimeModule;
import cn.bootx.platform.common.jackson.jdk.JavaLongTypeModule;
import cn.bootx.platform.common.jackson.util.JacksonUtil;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * jackson 序列化自动配置
 * 要先于 JacksonAutoConfiguration 中 注入 ObjectMapper
 * 注意: @AutoConfigureBefore 只可以用在自动配置上, 普通配置类不生效
 * @author xxm
 * @since 2020/4/23 22:28
 */
@Configuration
@AutoConfigureBefore(JacksonAutoConfiguration.class)
public class JacksonConfiguration {

    /**
     * 对象映射, 不会记录被序列化的类型信息, 就是最常见的那种json格式数据
     */
    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper()
            // 指定要序列化的域
            .setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY)
            // 不将日期写为时间戳
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            // 忽略未知属性
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            // 对象属性为空时可以序列化
            .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
            .registerModule(new Java8TimeModule())
            .registerModule(new Jdk8Module())
            .registerModule(new JavaLongTypeModule())
            .registerModule(new SimpleModule());
        JacksonUtil.setObjectMapper(objectMapper);
        return objectMapper;
    }

    /**
     * 序列化配置 ObjectMapper 对象 会记录被序列化的类型信息, 反序列化时直接能反序列化回原始的对象类型
     */
    @Bean
    public ObjectMapper typeObjectMapper(ObjectMapper objectMapper) {
        // 对象映射器
        ObjectMapper copy = objectMapper.copy();
        // 序列化是记录被序列化的类型信息
        // 指定序列化输入的类型为非最终类型，除了少数“自然”类型（字符串、布尔值、整数、双精度），它们可以从 JSON 正确推断； 以及所有非最终类型的数组
        copy.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL,
                JsonTypeInfo.As.WRAPPER_ARRAY)
            // null 值不序列化
            .setSerializationInclusion(JsonInclude.Include.NON_NULL);
        JacksonUtil.setTypeObjectMapper(copy);
        return copy;
    }

    /**
     * 序列化时忽略空值
     */
    @Bean
    public ObjectMapper ignoreNullObjectMapper(ObjectMapper objectMapper) {
        // 对象映射器
        ObjectMapper copy = objectMapper.copy()
                // null 值不序列化
                .setSerializationInclusion(JsonInclude.Include.NON_NULL);
        JacksonUtil.setIgnoreNullObjectMapper(copy);
        return copy;
    }

}
