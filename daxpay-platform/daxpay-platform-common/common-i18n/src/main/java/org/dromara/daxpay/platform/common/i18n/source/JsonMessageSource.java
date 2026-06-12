package org.dromara.daxpay.platform.common.i18n.source;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.AbstractMessageSource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/// # 基于 JSON 文件的消息源, 支持目录嵌套和多文件拆分
///
/// 扫描规则: 扫描 classpath*:i18n/{locale}/**\/*.json, 文件路径映射为 key 前缀
/// 例如: i18n/zh-CN/channel/enum.json → key 前缀 "channel.enum"
/// JSON 内容: { "alipay": "支付宝" } → 完整 key: "channel.enum.alipay"
@Slf4j
public class JsonMessageSource extends AbstractMessageSource {

    /// 缓存: locale -> (key -> message)
    private final Map<Locale, Map<String, String>> cachedMessages = new ConcurrentHashMap<>();

    private final ResourcePatternResolver resourceResolver;

    /// 默认语言（当请求的语言没有翻译文件时使用）
    @Setter
    private Locale defaultLocale = Locale.CHINA;

    public JsonMessageSource(ResourcePatternResolver resourceResolver) {
        this.resourceResolver = resourceResolver;
    }

    @Override
    protected MessageFormat resolveCode(String code, Locale locale) {
        Map<String, String> messages = this.loadMessages(locale);
        String message = messages.get(code);
        // 未命中则按语言（不含地区）回退: zh-CN -> zh
        if (message == null) {
            Locale langOnly = Locale.of(locale.getLanguage());
            if (!langOnly.equals(locale)) {
                messages = this.loadMessages(langOnly);
                message = messages.get(code);
            }
        }
        // 仍未命中则用默认 locale 回退
        if (message == null && !locale.equals(this.defaultLocale)) {
            messages = this.loadMessages(this.defaultLocale);
            message = messages.get(code);
        }
        return message != null ? new MessageFormat(message, locale) : null;
    }

    private Map<String, String> loadMessages(Locale locale) {
        return this.cachedMessages.computeIfAbsent(locale, this::loadFromFiles);
    }

    private Map<String, String> loadFromFiles(Locale locale) {
        Map<String, String> result = new HashMap<>();
        String localeTag = locale.toLanguageTag();
        // 兼容 zh-CN 和 zh_CN 两种格式
        String basePath = "classpath*:i18n/" + localeTag + "/**/*.json";
        try {
            Resource[] resources = this.resourceResolver.getResources(basePath);
            for (Resource resource : resources) {
                String filePath = resource.getURL().toString();
                // 提取相对路径: 从 i18n/{locale}/ 之后到 .json 之前
                String relativePath = this.extractRelativePath(filePath, localeTag);
                if (relativePath == null) {
                    continue;
                }
                // 读取文件内容
                String content = this.readResourceContent(resource);
                if (content == null || content.isBlank()) {
                    continue;
                }
                // 解析 JSON
                JSONObject jsonObj = JSONUtil.parseObj(content);
                // 递归展平
                this.flattenJson(relativePath, jsonObj, result);
            }
            log.debug("加载国际化文件完成, locale={}, 条目数={}", localeTag, result.size());
        }
        catch (IOException e) {
            log.warn("扫描国际化文件失败, locale={}", localeTag, e);
        }
        return result;
    }

    /// 从完整 URL 路径中提取 locale 后的相对路径
    /// 例如: .../i18n/zh-CN/channel/enum.json → channel.enum
    /// 从资源 URL 解析 i18n 相对路径前缀（供单测校验）
    String extractRelativePath(String fileUrl, String localeTag) {
        String decoded = URLDecoder.decode(fileUrl, StandardCharsets.UTF_8);
        String marker = "i18n/" + localeTag + "/";
        int idx = decoded.lastIndexOf(marker);
        if (idx < 0) {
            return null;
        }
        String relative = decoded.substring(idx + marker.length());
        // 去掉 .json 后缀
        if (relative.endsWith(".json")) {
            relative = relative.substring(0, relative.length() - 5);
        }
        // 路径分隔符替换为 .
        return relative.replace('/', '.').replace('\\', '.');
    }

    /// 递归展平 JSON 对象
    /// 例如: { "alipay": "支付宝", "amount": { "exceed": "金额超限" } }
    /// 前缀 "channel.enum" → channel.enum.alipay="支付宝", channel.enum.amount.exceed="金额超限"
    private void flattenJson(String prefix, JSONObject json, Map<String, String> result) {
        for (Map.Entry<String, Object> entry : json.entrySet()) {
            String key = prefix.isEmpty() ? entry.getKey() : prefix + "." + entry.getKey();
            Object value = entry.getValue();
            if (value instanceof JSONObject nested) {
                this.flattenJson(key, nested, result);
            }
            else if (value != null) {
                String existing = result.put(key, value.toString());
                if (existing != null) {
                    log.warn("检测到重复的国际化 key: {}, 旧值={}, 新值={}", key, existing, value);
                }
            }
        }
    }

    private String readResourceContent(Resource resource) {
        try (InputStream is = resource.getInputStream()) {
            return new String(is.readAllBytes(), StandardCharsets.UTF_8);
        }
        catch (IOException e) {
            log.warn("读取国际化文件失败: {}", resource.getDescription(), e);
            return null;
        }
    }
}
