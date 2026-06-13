package org.dromara.daxpay.payment.common.util;

import cn.hutool.core.date.LocalDateTimeUtil;
import lombok.experimental.UtilityClass;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.*;

/// # 对象签名工具类
///
@UtilityClass
public class ObjectSignStrUtil {
    private final String FIELD_SIGN  = "sign";

    /// 生成待签名字符串
    public String buildSignStr(Object obj) {
        // 1. 扁平化
        Map<String, String> flat = flatten(obj);
        // 2. 按 ASCII 排序（排除 sign 字段）
        TreeMap<String, String> sorted = new TreeMap<>(flat);
        sorted.remove(FIELD_SIGN);
        // 3. 拼接签名字符串
        StringBuilder sb = new StringBuilder();
        sorted.forEach((k, v) -> sb.append(k).append("=").append(v).append("&"));
        return sb.substring(0, sb.length() - 1);
    }

    /// 扁平化对象，生成用于签名的键值对
    ///
    /// @param obj 要扁平化的对象（不能为 null）
    /// @return 扁平化后的 Map，键为 fieldPath，值为字符串
    public Map<String, String> flatten(Object obj) {
        if (obj == null) {
            throw new IllegalArgumentException("对象不能为 null");
        }
        Map<String, String> result = new LinkedHashMap<>();
        flatten("", obj, result);
        return result;
    }

    /// 扁平化处理
    private void flatten(String prefix, Object obj, Map<String, String> result) {
        // 空值不签名
        if (obj == null) {
            return;
        }

        // 特殊处理 BigDecimal
        if (obj instanceof BigDecimal bd) {
            // 去除尾随零，避免 100.00 vs 100
            String value = bd.stripTrailingZeros().toPlainString();
            result.put(prefix, value);
            return;
        }

        // 处理 OffsetDateTime
        if (obj instanceof OffsetDateTime value) {
            result.put(prefix, value.withOffsetSameInstant(java.time.ZoneOffset.UTC).toString());
            return;
        }

        // 处理 LocalDateTime (旧格式向下兼容)
        if (obj instanceof LocalDateTime value) {
            result.put(prefix, LocalDateTimeUtil.formatNormal(value));
            return;
        }

        // 处理 LocalDate
        if (obj instanceof LocalDate value) {
            result.put(prefix, LocalDateTimeUtil.formatNormal(value));
            return;
        }

        Class<?> clazz = obj.getClass();

        // 基本类型、字符串、包装类、日期等直接转字符串
        if (isSimpleValueType(clazz)) {
            result.put(prefix, obj.toString());
            return;
        }

        // 处理 List
        if (obj instanceof List<?> list) {
            for (int i = 0; i < list.size(); i++) {
                String key = prefix + "[" + i + "]";
                flatten(key, list.get(i), result);
            }
            return;
        }

         // 处理map
        if (obj instanceof Map<?, ?> map) {
            for (Map.Entry<?, ?> entry : map.entrySet()) {
                String key = prefix + "." + entry.getKey();
                flatten(key, entry.getValue(), result);
            }
            return;
        }

        // 处理普通对象（递归字段）
        for (Field field : getAllFields(clazz)) {
            if (Modifier.isStatic(field.getModifiers())) {
                continue; // 跳过 static 字段
            }
            try {
                field.setAccessible(true);
                Object value = field.get(obj);
                String fieldName = field.getName();
                String key = prefix.isEmpty() ? fieldName : prefix + "." + fieldName;
                flatten(key, value, result);
            } catch (Exception e) {
                throw new RuntimeException("无法访问字段: " + field.getName(), e);
            }
        }
    }

    /// 获取类及其父类的所有声明字段
    private List<Field> getAllFields(Class<?> clazz) {
        List<Field> fields = new ArrayList<>();
        while (clazz != null && clazz != Object.class) {
            fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
            clazz = clazz.getSuperclass();
        }
        return fields;
    }

    /// 判断是否为简单值类型（直接转字符串）
    private boolean isSimpleValueType(Class<?> clazz) {
        return clazz.isPrimitive() ||
                clazz == String.class ||
                clazz == Integer.class ||
                clazz == Long.class ||
                clazz == Double.class ||
                clazz == Float.class ||
                clazz == Boolean.class ||
                clazz == Byte.class ||
                clazz == Short.class ||
                clazz == Character.class ||
                clazz == java.util.Date.class ||
                clazz == java.time.Instant.class
                ;
        // 可根据需要扩展
    }
}


