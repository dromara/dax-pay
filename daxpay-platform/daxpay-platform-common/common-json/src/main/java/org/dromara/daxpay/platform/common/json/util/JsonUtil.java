package org.dromara.daxpay.platform.common.json.util;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.json.*;
import lombok.experimental.UtilityClass;

import java.util.Collection;

/// # json工具类, 基于hutool的进行封装
///
/// <strong>仅限以下场景使用，禁止作为平台通用 JSON 基础设施：</strong>
/// - 支付回调报文手工拼装与解析
/// - 支付签名字符串构造与规范化
/// - 按通道差异定制的 JSON 字段处理
/// - 手工接收/发送 JSON 字符串的支付协议场景
///
/// 平台通用 JSON 操作（接口、配置字段、缓存对象等）请使用 {@link JacksonUtil}。
///
/// 注意: 无法处理LocalDate, LocalTime格式, 需要使用JacksonUtil进行处理
///
/// @see JacksonUtil
@UtilityClass
public class JsonUtil {
    private final JSONConfig JSON_CONFIG = JSONConfig.create()
            .setDateFormat(DatePattern.NORM_DATETIME_PATTERN);

    /// 序列化为字符串
    public String toJsonStr(Object object) {
        JSONObject jsonObject = new JSONObject(object, JSON_CONFIG);
        return JSONUtil.toJsonStr(jsonObject);
    }

    /// 序列化为字符串
    public String toJsonStr(Collection<?> object) {
        JSONArray jsonObject = new JSONArray(object, JSON_CONFIG);
        return JSONUtil.toJsonStr(jsonObject);
    }

    /// 转换为实体, 仅供处理验签时使用, 其他场景不要使用
    public <T> T toBean(String json, TypeReference<T> reference) {
        JSON parse = JSONUtil.parse(json, JSON_CONFIG);
        return parse.toBean(reference);
    }
}
