package org.dromara.daxpay.platform.common.request.context.local;

import cn.hutool.core.map.MapUtil;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

/// # 请求上下文存储
///
public final class RequestContextStorage {

    private static final ThreadLocal<Map<String, String>> THREAD_LOCAL = new ThreadLocal<>();

    private RequestContextStorage() {
    }

    /// 设置数据
    public static void put(String key, String value) {
        String normalizedKey = normalizeKey(key);
        if (normalizedKey == null) {
            return;
        }
        Map<String, String> map = THREAD_LOCAL.get();
        if (MapUtil.isEmpty(map)) {
            map = new HashMap<>(16);
            THREAD_LOCAL.set(map);
        }
        map.put(normalizedKey, value);
    }

    /// 获取数据
    public static String get(String key) {
        String normalizedKey = normalizeKey(key);
        if (normalizedKey == null) {
            return null;
        }
        return Optional.ofNullable(THREAD_LOCAL.get()).map(map -> map.get(normalizedKey)).orElse(null);
    }

    /// 清除
    public static void clear() {
        THREAD_LOCAL.remove();
    }

    /// 标准化key
    public static String normalizeKey(String key) {
        return Optional.ofNullable(key)
                .map(String::trim)
                .filter(str -> !str.isEmpty())
                .map(str -> str.toLowerCase(Locale.ROOT))
                .orElse(null);
    }
}
