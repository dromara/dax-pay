package cn.daxpay.open.platform.common.json.util;

import cn.daxpay.open.platform.core.exception.RepetitiveOperationException;
import tools.jackson.core.JacksonException;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

/// # jackson常用工具类封装
///
/// 平台通用 JSON 默认工具，统一负责接口、配置字段、缓存对象等通用场景的 JSON 序列化与反序列化。
/// 平台公共链路新增 JSON 处理时，应优先使用此类而非 Hutool JsonUtil。
///
/// 使用约束：
/// - 从 JSON 恢复对象时必须显式指定目标类型，禁止依赖多态自动恢复
/// - Redis 对象缓存使用此类序列化后存为 JSON，读取时显式转换目标类型
///
/// @see cn.daxpay.open.platform.common.json.util.JsonUtil
@Slf4j
@UtilityClass
public class JacksonUtil {

    private static boolean objectMapperFlag;

    private static boolean ignoreNullObjectMapperFlag;

    /// 获取标准 ObjectMapper 供外部序列化器使用
    @Getter
    private static ObjectMapper objectMapper;

    /// 忽略空值
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

    /// 对象序列化为json字符串,转换异常将被抛出
    /// 默认忽略空值
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
        catch (JacksonException e) {
            throw new RuntimeException("json序列化失败");
        }
    }

    /// JSON字符串转为实体类对象，转换异常将被抛出
    public <T> T toBean(String content, Class<T> valueType) {
        try {
            return objectMapper.readValue(content, valueType);
        }
        catch (JacksonException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException("json反序列化失败");
        }
    }

    /// JSON字符串转为实体类对象，转换异常将被抛出
    /// 默认忽略空值
    public <T> T toBean(String content, TypeReference<? extends T> ref) {
        return toBean(content, ref,true);
    }

    /// JSON字符串转为实体类对象，转换异常将被抛出
    public <T> T toBean(String content, TypeReference<? extends T> ref, boolean ignoreNull) {
        try {
            if (ignoreNull){
                return ignoreNullObjectMapper.readValue(content, ref);
            } else {
                return objectMapper.readValue(content, ref);
            }
        }
        catch (JacksonException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException("json反序列化失败");
        }
    }

    /// 通用类型转换，适用于非泛型目标类型
    ///
    /// 用于将 Map、Object、Redis 读取结果等对象转换为指定实体类。
    /// 如果来源本身是 JSON 字符串，应优先使用 toBean 做反序列化；
    /// 如果来源已经是对象结构（如 LinkedHashMap、Object），则使用 convert 做显式类型转换。
    ///
    /// @param from 来源对象
    /// @param to   目标类型
    public <T> T convert(Object from, Class<T> to) {
        try {
            return objectMapper.convertValue(from, to);
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException("json类型转换失败");
        }
    }

    /// 通用类型转换，适用于泛型目标类型
    ///
    /// 用于将 Map、Object、Redis 读取结果等对象转换为复杂泛型结构，
    /// 例如 List<UserResult>、Map<String, UserResult> 等。
    /// 如果来源本身是 JSON 字符串，应优先使用 toBean 做反序列化；
    /// 如果来源已经是对象结构（如 LinkedHashMap、Object），则使用 convert 做显式类型转换。
    ///
    /// @param from 来源对象
    /// @param to   泛型目标类型
    public <T> T convert(Object from, TypeReference<T> to) {
        try {
            return objectMapper.convertValue(from, to);
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException("json类型转换失败");
        }
    }
}

