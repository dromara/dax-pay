package cn.bootx.platform.common.jackson.util;

import cn.bootx.platform.core.exception.RepetitiveOperationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * jackson常用工具类封装
 *
 * @author xxm
 * @since 2020/11/29
 */
@Slf4j
@UtilityClass
public class JacksonUtil {

    private static boolean objectMapperFlag;

    private static boolean ignoreNullObjectMapperFlag;

    /** 标准 */
    private static ObjectMapper objectMapper;

    /** 忽略空值 */
    private static ObjectMapper ignoreNullObjectMapper;

    public void setObjectMapper(ObjectMapper objectMapper) {
        if (objectMapperFlag) {
            throw new RepetitiveOperationException();
        }
        objectMapperFlag = true;
        JacksonUtil.objectMapper = objectMapper;
    }

    public void setIgnoreNullObjectMapper(ObjectMapper ignoreNullObjectMapper) {
        if (ignoreNullObjectMapperFlag) {
            throw new RepetitiveOperationException();
        }
        ignoreNullObjectMapperFlag = true;
        JacksonUtil.ignoreNullObjectMapper = ignoreNullObjectMapper;
    }

    /**
     * 对象序列化为json字符串,转换异常将被抛出
     * 默认忽略空值
     */
    public String toJson(Object o) {
        return toJson(o,true);
    }

    public String toJson(Object o, boolean ignoreNull) {
        try {
            if (ignoreNull){
                return ignoreNullObjectMapper.writeValueAsString(o);
            }else {
                return objectMapper.writeValueAsString(o);
            }
        }
        catch (JsonProcessingException e) {
            throw new RuntimeException("json序列化失败");
        }
    }

    /**
     * JSON字符串转为实体类对象，转换异常将被抛出
     */
    public <T> T toBean(String content, Class<T> valueType) {
        try {
            return objectMapper.readValue(content, valueType);
        }
        catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException("json反序列化失败");
        }
    }

    /**
     * JSON字符串转为实体类对象，转换异常将被抛出
     * 默认忽略空值
     */
    public <T> T toBean(String content, TypeReference<? extends T> ref) {
        return toBean(content, ref,true);
    }

    /**
     * JSON字符串转为实体类对象，转换异常将被抛出
     */
    public <T> T toBean(String content, TypeReference<? extends T> ref, boolean ignoreNull) {
        try {
            if (ignoreNull){
                return ignoreNullObjectMapper.readValue(content, ref);
            } else {
                return objectMapper.readValue(content, ref);
            }
        }
        catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException("json反序列化失败");
        }
    }

    /**
     * JSON字符串转为实体类对象列表，转换异常将被抛出
     * 默认忽略空值
     */
    public <T> List<T> toList(String content, Class<T> valueType) {
       return toList(content, valueType,true);
    }

    /**
     * JSON字符串转为实体类对象列表，转换异常将被抛出
     * 默认忽略空值
     */
    public <T> List<T> toList(String content, Class<T> valueType, boolean ignoreNull) {
        try {
            if (ignoreNull){
                return ignoreNullObjectMapper.readValue(content, ignoreNullObjectMapper.getTypeFactory().constructCollectionType(List.class, valueType));
            } else {
                return objectMapper.readValue(content, objectMapper.getTypeFactory().constructCollectionType(List.class, valueType));

            }
        }
        catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException("json反序列化失败");
        }
    }
}
