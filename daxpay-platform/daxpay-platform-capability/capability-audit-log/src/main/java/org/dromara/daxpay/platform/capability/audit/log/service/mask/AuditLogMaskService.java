package org.dromara.daxpay.platform.capability.audit.log.service.mask;

import org.dromara.daxpay.platform.core.annotation.PartialMaskRule;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

/// # 审计日志脱敏服务
///
/// 支持全量脱敏、部分脱敏（保留前后N位）与内容截断
@Slf4j
@Service
public class AuditLogMaskService {

    /// 默认敏感键集合（全量脱敏，大小写不敏感）
    private static final Set<String> DEFAULT_SENSITIVE_KEYS = new HashSet<>(Arrays.asList(
            "password", "token", "secret", "credential", "accesstoken", "refreshtoken",
            "idcard", "phone", "mobile", "bankcard", "cvv", "ssn", "passport"
    ));

    /// 全量脱敏替换值
    private static final String MASK_VALUE_FULL = "******";

    /// 执行脱敏处理
    ///
    /// @param json             待处理的 JSON 字符串
    /// @param enabled          是否启用脱敏
    /// @param fullMaskKeys     全量脱敏键名单（为空时使用默认列表）
    /// @param partialMaskRules 部分脱敏规则（优先级高于全量脱敏）
    /// @param maxLength        最大长度（0 或负数表示不截断）
    /// @return 处理后的字符串
    public String process(String json, boolean enabled, String[] fullMaskKeys, 
                          PartialMaskRule[] partialMaskRules, int maxLength) {
        if (StrUtil.isEmpty(json)) {
            return json;
        }

        String result = json;

        // 脱敏处理
        if (enabled) {
            result = maskJson(result, fullMaskKeys, partialMaskRules);
        }

        // 截断处理
        if (maxLength > 0 && result.length() > maxLength) {
            result = truncate(result, maxLength);
        }

        return result;
    }

    /// 递归 JSON 脱敏
    private String maskJson(String json, String[] fullMaskKeys, PartialMaskRule[] partialMaskRules) {
        // 构建规则集合
        Map<String, PartialMaskRule> partialRuleMap = buildPartialRuleMap(partialMaskRules);
        Set<String> fullMaskKeySet = buildFullMaskKeySet(fullMaskKeys, partialRuleMap.keySet());
        
        try {
            JSONObject jsonObject = JSONUtil.parseObj(json);
            maskObject(jsonObject, partialRuleMap, fullMaskKeySet);
            return jsonObject.toString();
        } catch (Exception e) {
            // JSON 解析失败，尝试字符串级别替换
            log.debug("JSON parse failed, try string level mask: {}", e.getMessage());
            return maskStringLevel(json, partialRuleMap, fullMaskKeySet);
        }
    }

    /// 递归处理对象中的敏感字段
    private void maskObject(JSONObject jsonObject, Map<String, PartialMaskRule> partialRuleMap,
                             Set<String> fullMaskKeySet) {
        for (String key : jsonObject.keySet()) {
            String lowerKey = key.toLowerCase();
            Object value = jsonObject.get(key);

            // 仅处理字符串类型
            if (!(value instanceof String strValue)) {
                // 递归处理嵌套对象和数组（非字符串类型不脱敏）
                if (value instanceof JSONObject) {
                    maskObject((JSONObject) value, partialRuleMap, fullMaskKeySet);
                } else if (value instanceof JSONArray _value) {
                    maskArray(_value, partialRuleMap, fullMaskKeySet);
                }
                continue;
            }

            // 检查是否命中部分脱敏规则（优先级高）
            PartialMaskRule partialRule = partialRuleMap.get(lowerKey);
            if (partialRule != null) {
                jsonObject.set(key, partialMask(strValue, partialRule.keepPrefix(), partialRule.keepSuffix()));
                continue;
            }

            // 检查是否命中全量脱敏
            if (fullMaskKeySet.contains(lowerKey)) {
                jsonObject.set(key, MASK_VALUE_FULL);
                continue;
            }

            // 递归处理嵌套对象和数组
            if (value instanceof JSONObject) {
                maskObject((JSONObject) value, partialRuleMap, fullMaskKeySet);
            } else if (value instanceof JSONArray _value) {
                maskArray(_value, partialRuleMap, fullMaskKeySet);
            }
        }
    }

    /// 递归处理数组中的敏感字段
    private void maskArray(JSONArray jsonArray, Map<String, PartialMaskRule> partialRuleMap,
                           Set<String> fullMaskKeySet) {
        for (Object item : jsonArray) {
            if (item instanceof JSONObject) {
                maskObject((JSONObject) item, partialRuleMap, fullMaskKeySet);
            }
        }
    }

    /// 构建部分脱敏规则 Map（key 转小写）
    /// 重复 key 采用后定义覆盖前定义
    private Map<String, PartialMaskRule> buildPartialRuleMap(PartialMaskRule[] partialRules) {
        Map<String, PartialMaskRule> ruleMap = new HashMap<>();
        if (partialRules != null) {
            for (PartialMaskRule rule : partialRules) {
                String lowerKey = rule.key().toLowerCase();
                if (ruleMap.containsKey(lowerKey)) {
                    log.warn("部分脱敏规则重复 key: {}，后定义覆盖前定义", rule.key());
                }
                ruleMap.put(lowerKey, rule);
            }
        }
        return ruleMap;
    }

    /// 构建全量脱敏键集合
    /// - 如果 fullMaskKeys 为空，使用默认列表
    /// - 如果配置了 fullMaskKeys，使用配置的（不与默认合并）
    /// - 从全量列表中排除部分脱敏规则的 key（部分脱敏优先）
    private Set<String> buildFullMaskKeySet(String[] fullMaskKeys, Set<String> partialKeys) {
        Set<String> keySet;
        if (fullMaskKeys == null || fullMaskKeys.length == 0) {
            // 使用默认列表
            keySet = new HashSet<>(DEFAULT_SENSITIVE_KEYS);
        } else {
            // 使用配置的列表
            keySet = new HashSet<>();
            for (String key : fullMaskKeys) {
                keySet.add(key.toLowerCase());
            }
        }
        // 排除部分脱敏的 key（部分脱敏优先）
        keySet.removeAll(partialKeys);
        return keySet;
    }

    /// 部分脱敏：保留前后 N/M 位，中间用 * 掩码
    /// 如果字符串长度 <= keepPrefix + keepSuffix，直接全量脱敏
    private String partialMask(String value, int keepPrefix, int keepSuffix) {
        if (StrUtil.isEmpty(value)) {
            return MASK_VALUE_FULL;
        }
        int totalKeep = keepPrefix + keepSuffix;
        if (value.length() <= totalKeep) {
            // 长度不足，全量脱敏
            return MASK_VALUE_FULL;
        }
        String prefix = value.substring(0, keepPrefix);
        String suffix = value.substring(value.length() - keepSuffix);
        int maskLen = value.length() - totalKeep;
        String mask = "*".repeat(maskLen);
        return prefix + mask + suffix;
    }

    /// 字符串级别脱敏（JSON解析失败时的降级处理）
    private String maskStringLevel(String text, Map<String, PartialMaskRule> partialRuleMap,
                                   Set<String> fullMaskKeySet) {
        if (text == null) {
            return null;
        }

        String result = text;

        // 先处理部分脱敏（优先级高）
        for (Map.Entry<String, PartialMaskRule> entry : partialRuleMap.entrySet()) {
            String key = entry.getKey();
            PartialMaskRule rule = entry.getValue();
            int keepPrefix = rule.keepPrefix();
            int keepSuffix = rule.keepSuffix();

            // 匹配双引号格式
            String regex = "\"" + Pattern.quote(key) + "\"\\s*:\\s*\"([^\"]*)\"";
            result = Pattern.compile(regex).matcher(result)
                    .replaceAll(mr -> "\"" + key + "\":\"" + partialMask(mr.group(1), keepPrefix, keepSuffix) + "\"");

            // 匹配单引号格式
            String singleRegex = "'" + Pattern.quote(key) + "'\\s*:\\s*'([^']*)'";
            result = Pattern.compile(singleRegex).matcher(result)
                    .replaceAll(mr -> "'" + key + "':'" + partialMask(mr.group(1), keepPrefix, keepSuffix) + "'");
        }

        // 再处理全量脱敏
        for (String key : fullMaskKeySet) {
            // 匹配双引号格式
            String regex = "\"" + Pattern.quote(key) + "\"\\s*:\\s*\"[^\"]*\"";
            result = result.replaceAll(regex, "\"" + key + "\":\"" + MASK_VALUE_FULL + "\"");

            // 匹配单引号格式
            String singleRegex = "'" + Pattern.quote(key) + "'\\s*:\\s*'[^']*'";
            result = result.replaceAll(singleRegex, "'" + key + "':'" + MASK_VALUE_FULL + "'");
        }

        return result;
    }

    /// 内容截断
    private String truncate(String text, int maxLength) {
        if (text == null || text.length() <= maxLength) {
            return text;
        }
        return text.substring(0, maxLength) + "...[truncated]";
    }

    /// 判断是否需要处理
    /// 仅在 saveParam/saverReturn 为 true 且内容非空时执行
    public boolean shouldProcess(boolean saveFlag, String content) {
        return saveFlag && StrUtil.isNotEmpty(content);
    }
}


