package org.dromara.daxpay.sdk.util;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.json.*;
import lombok.experimental.UtilityClass;

import java.util.Collection;

/**
 * json工具类, 基于hutool的进行封装, 对java8的时间格式进行转换
 * @author xxm
 * @since 2024/6/28
 */
@UtilityClass
public class JsonUtil {
    private final JSONConfig JSON_CONFIG = JSONConfig.create()
            .setDateFormat(DatePattern.NORM_DATETIME_PATTERN);

    /**
     * 转换为实体
     */
    public <T> T toBean(String json, Class<T> clazz) {
        JSONObject jsonObject = new JSONObject(json, JSON_CONFIG);
        return JSONUtil.toBean(jsonObject, clazz);
    }

    /**
     * 转换为实体
     */
    public <T> T toBean(String json, TypeReference<T> reference) {
        JSON parse = JSONUtil.parse(json, JSON_CONFIG);
        return parse.toBean(reference);
    }

    /**
     * 序列化为字符串
     */
    public String toJsonStr(Object object) {
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
     * JSON字符串转JSONObject对象
     */
    public JSONObject parseObj(String jsonStr) {
        return JSONUtil.parseObj(jsonStr, JSON_CONFIG);
    }
}
