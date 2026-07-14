package cn.daxpay.open.payment.common.util;

import cn.daxpay.open.platform.common.json.util.JsonUtil;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.util.StrUtil;
import lombok.experimental.UtilityClass;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/// # Json签名字符串工具类
///
/// 输入 JSON 字符串 → 扁平化排序Map → 签名字符串
@UtilityClass
public class JsonSignStrUtil {

    /// 生成待签名字符串
    public String buildSignStr(String jsonStr) {
        return buildSignStr(buildSortedMap(jsonStr));
    }

    /// 将 JSON 字符串扁平化为 TreeMap（已按 ASCII 排序）
    ///
    /// @param jsonStr JSON 字符串
    /// @return 扁平化后的 TreeMap（key: fieldPath, value: normalized string）
    public TreeMap<String, String> buildSortedMap(String jsonStr) {
        if (StrUtil.isBlank(jsonStr)) {
            throw new IllegalArgumentException("JSON 不能为空");
        }

        // 1. 反序列化为 Map<String, Object> 时间进行了序列化
        Map<String, Object> root = JsonUtil.toBean(jsonStr, new TypeReference<>() {});

        // 2. 扁平化
        Map<String, String> flatMap = new LinkedHashMap<>();
        flatten("", root, flatMap);

        // 3. 按 ASCII 排序（TreeMap 默认按 key 的 compareTo 排序）
        return new TreeMap<>(flatMap);
    }

    /// 递归扁平化 Map 或 List
    @SuppressWarnings({"unchecked", "rawtypes"})
    private void flatten(String prefix, Object value, Map<String, String> result) {
        switch (value) {
            case null -> {
                return;
            }
            // 处理 BigDecimal（去除末尾零）
            case BigDecimal bd -> {
                result.put(prefix, bd.stripTrailingZeros().toPlainString());
                return;
            }
            // 处理 Number（Integer, Long, Double 等）
            case Number ignored2 -> {
                // 注意：Double/Float 可能有精度问题，建议 API 用 BigDecimal
                result.put(prefix, value.toString());
                return;
            }
            // 处理 Boolean
            case Boolean ignored1 -> {
                result.put(prefix, value.toString());
                return;
            }
            // 处理 String
            case String s -> {
                result.put(prefix, s);
                return;
            }
            // 处理 List
            case List ignored -> {
                List<Object> list = (List<Object>) value;
                for (int i = 0; i < list.size(); i++) {
                    String key = prefix + "[" + i + "]";
                    flatten(key, list.get(i), result);
                }
                return;
            }
            // 处理 Map（嵌套对象）
            case Map ignored -> {
                Map<String, Object> map = (Map<String, Object>) value;
                for (Map.Entry<String, Object> entry : map.entrySet()) {
                    String key = StrUtil.isBlank(prefix) ? entry.getKey() : prefix + "." + entry.getKey();
                    flatten(key, entry.getValue(), result);
                }
                return;
            }
            default -> {}
        }

        // 其他类型转字符串
        result.put(prefix, value.toString());
    }

    /// 生成待签名字符串（排除 sign 字段）
    public String buildSignStr(Map<String, String> map) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if ("sign".equalsIgnoreCase(entry.getKey())) {
                continue;
            }
            sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }
        if (!sb.isEmpty()) {
            sb.setLength(sb.length() - 1); // 移除最后一个 &
        }
        return sb.toString();
    }
}

