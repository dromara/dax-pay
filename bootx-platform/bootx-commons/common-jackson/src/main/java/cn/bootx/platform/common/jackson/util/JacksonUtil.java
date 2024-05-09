package cn.bootx.platform.common.jackson.util;

import cn.bootx.platform.common.core.exception.RepetitiveOperationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * jackson常用工具类封装
 *
 * @author xxm
 * @since 2020/11/29
 */
@Slf4j
public class JacksonUtil {

    private static boolean objectMapperFlag;

    private static boolean typeObjectMapperFlag;

    private static ObjectMapper objectMapper;

    private static ObjectMapper typeObjectMapper;

    public static void setObjectMapper(ObjectMapper objectMapper) {
        if (objectMapperFlag) {
            throw new RepetitiveOperationException();
        }
        objectMapperFlag = true;
        JacksonUtil.objectMapper = objectMapper;
    }

    public static void setTypeObjectMapper(ObjectMapper typeObjectMapper) {
        if (typeObjectMapperFlag) {
            throw new RepetitiveOperationException();
        }
        typeObjectMapperFlag = true;
        JacksonUtil.typeObjectMapper = typeObjectMapper;
    }

    /**
     * 对象序列化为json字符串,转换异常将被抛出
     */
    public static String toJson(Object o) {
        try {
            return objectMapper.writeValueAsString(o);
        }
        catch (JsonProcessingException e) {
            throw new RuntimeException("json序列化失败");
        }
    }

    /**
     * JSON字符串转为实体类对象，转换异常将被抛出
     */
    public static <T> T toBean(String content, Class<T> valueType) {
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
     */
    public static <T> T toBean(String content, TypeReference<? extends T> ref) {
        try {
            return objectMapper.readValue(content, ref);
        }
        catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException("json反序列化失败");
        }
    }

    /**
     * JSON字符串转为实体类对象列表，转换异常将被抛出
     */
    public static <T> List<T> toList(String content, Class<T> valueType) {
        try {
            return objectMapper.readValue(content,
                    objectMapper.getTypeFactory().constructCollectionType(List.class, valueType));
        }
        catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException("json反序列化失败");
        }
    }

    /**
     * 对象序列化为json字符串,转换异常将被抛出(携带类型信息)
     */
    public static String toTypeJson(Object o) {
        try {
            return typeObjectMapper.writeValueAsString(o);
        }
        catch (JsonProcessingException e) {
            throw new RuntimeException("json序列化失败");
        }
    }

    /**
     * JSON字符串转为实体类对象，转换异常将被抛出 (携带类型信息)
     */
    public static <T> T toTypeBean(String content, Class<T> valueType) {
        try {
            return typeObjectMapper.readValue(content, valueType);
        }
        catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException("json反序列化失败");
        }
    }

    /**
     * JSON字符串转为实体类对象列表，转换异常将被抛出
     */
    public static <T> List<T> toTypeList(String content, Class<T> valueType) {
        try {
            return typeObjectMapper.readValue(content,
                    typeObjectMapper.getTypeFactory().constructCollectionType(List.class, valueType));
        }
        catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException("json反序列化失败");
        }
    }

}
