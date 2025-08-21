package cn.bootx.platform.core.util;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.json.*;
import lombok.experimental.UtilityClass;

import java.util.Collection;

/**
 * json工具类, 基于hutool的进行封装, 仅用于与业务系统交互使用, 为了保持与SDK
 * 对java8的LocalDateTime时间格式进行转换, 但无法处理LocalDate, LocalTime格式, 需要使用JacksonUtil进行处理
 * @author xxm
 * @since 2024/6/28
 */
@UtilityClass
public class JsonUtil {
    private final JSONConfig JSON_CONFIG = JSONConfig.create().setDateFormat(DatePattern.NORM_DATETIME_PATTERN);


    /**
     * 序列化为字符串
     */
    public String toJsonStr(Object object){
        JSONObject jsonObject = new JSONObject(object, JSON_CONFIG);
        return JSONUtil.toJsonStr(jsonObject);
    }

    /**
     * 序列化为字符串
     */
    public String toJsonStr(Collection<?> object){
        JSONArray jsonObject = new JSONArray(object, JSON_CONFIG);
        return JSONUtil.toJsonStr(jsonObject);
    }

    /**
     * 转换为实体, 仅供处理验签时使用, 其他场景不要使用
     */
    public <T> T toBean(String json, TypeReference<T> reference) {
        JSON parse = JSONUtil.parse(json, JSON_CONFIG);
        return parse.toBean(reference);
    }
}
