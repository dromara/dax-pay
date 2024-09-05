package cn.bootx.platform.common.headerholder.local;

import cn.hutool.core.map.MapUtil;
import com.alibaba.ttl.TransmittableThreadLocal;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 请求头TTL存储类
 *
 * @author xxm
 * @since 2021/4/20
 */
public final class HolderContextHolder {

    private static final ThreadLocal<Map<String, String>> THREAD_LOCAL = new TransmittableThreadLocal<>();

    /**
     * TTL 设置数据
     */
    public static void put(String key, String value) {
        Map<String, String> map = THREAD_LOCAL.get();
        if (MapUtil.isEmpty(map)) {
            map = new HashMap<>(10);
            THREAD_LOCAL.set(map);
        }
        map.put(key, value);
    }

    /**
     * 获取TTL中的数据
     */
    public static String get(String key) {
        return Optional.ofNullable(THREAD_LOCAL.get()).map(map -> map.get(key)).orElse(null);
    }

    /**
     * 清除
     */
    public static void clear() {
        THREAD_LOCAL.remove();
    }

}
