package cn.daxpay.open.platform.common.i18n.source;

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
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
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

    /// 已加载的可用 locale 标签集合（如 en-US, zh-CN），启动时扫描
    private final Set<String> availableLocaleTags = new HashSet<>();

    /// 语言码 → 首个匹配的地区 locale 标签（如 en → en-US, zh → zh-CN），用于反向回退
    /// 当请求纯语言码（如 en）而无对应资源目录时, 据此映射到同语言的地区资源
    private final Map<String, String> languageToRegionLocale = new HashMap<>();

    public JsonMessageSource(ResourcePatternResolver resourceResolver) {
        this.resourceResolver = resourceResolver;
        this.scanAvailableLocales();
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
        // 仍未命中则按语言反向匹配地区: en -> en-US, zh -> zh-CN, zh-Hans -> zh-CN
        if (message == null) {
            String lang = locale.getLanguage();
            if (!lang.isEmpty()) {
                String regionTag = this.languageToRegionLocale.get(lang);
                if (regionTag != null) {
                    Locale regionLocale = Locale.forLanguageTag(regionTag);
                    if (!regionLocale.equals(locale)) {
                        messages = this.loadMessages(regionLocale);
                        message = messages.get(code);
                    }
                }
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

    /// 启动时扫描 i18n 目录下所有 locale 子目录，建立「语言码 → 地区 locale」反向映射
    /// 用于支持请求纯语言码（如 en）时反向匹配到地区资源（如 en-US）
    private void scanAvailableLocales() {
        try {
            Resource[] resources = this.resourceResolver.getResources("classpath*:i18n/**/*.json");
            for (Resource resource : resources) {
                String localeTag = this.extractLocaleTag(resource.getURL().toString());
                if (localeTag != null && this.availableLocaleTags.add(localeTag)) {
                    // 首次发现该 locale, 记录语言 → 地区映射
                    Locale locale = Locale.forLanguageTag(localeTag);
                    String lang = locale.getLanguage();
                    if (!lang.isEmpty()) {
                        this.languageToRegionLocale.putIfAbsent(lang, localeTag);
                    }
                }
            }
            log.debug("扫描到可用 locale: {}, 语言→地区映射: {}", this.availableLocaleTags, this.languageToRegionLocale);
        } catch (IOException e) {
            log.warn("扫描可用 locale 失败, 语言→地区反向回退将不可用", e);
        }
    }

    /// 从资源 URL 提取 locale 标签
    /// .../i18n/en-US/enum/xxx.json → "en-US"
    private String extractLocaleTag(String fileUrl) {
        String decoded = URLDecoder.decode(fileUrl, StandardCharsets.UTF_8);
        String marker = "i18n/";
        int idx = decoded.lastIndexOf(marker);
        if (idx < 0) {
            return null;
        }
        String after = decoded.substring(idx + marker.length());
        int slash = after.indexOf('/');
        return slash > 0 ? after.substring(0, slash) : null;
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
