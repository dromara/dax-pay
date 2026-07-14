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
///
/// ## 地区别名
/// 无独立资源目录的 Accept-Language 变体会先映射到主资源 locale:
/// - `zh-Hant` / `zh-Hant-TW` → `zh-TW`
/// - `zh-HK` / `zh-Hant-HK` / `zh-MO` / `zh-Hant-MO` → `zh-HK`
/// - `zh-Hans` / `zh-Hans-CN` → `zh-CN`
/// 缺 key 时: 精确资源 → 默认 `zh-CN`（台港互不为 fallback）
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

    /// 语言码 → 地区 locale 标签（如 en → en-US, zh → zh-CN），用于反向回退
    private final Map<String, String> languageToRegionLocale = new HashMap<>();

    /// Accept-Language / 客户端变体 → 资源目录 locale 标签
    private static final Map<String, String> LOCALE_ALIASES = Map.ofEntries(
            Map.entry("zh-hans", "zh-CN"),
            Map.entry("zh-hans-cn", "zh-CN"),
            Map.entry("zh-cn", "zh-CN"),
            Map.entry("zh-hant", "zh-TW"),
            Map.entry("zh-hant-tw", "zh-TW"),
            Map.entry("zh-tw", "zh-TW"),
            Map.entry("zh-hant-hk", "zh-HK"),
            Map.entry("zh-hk", "zh-HK"),
            Map.entry("zh-hant-mo", "zh-HK"),
            Map.entry("zh-mo", "zh-HK"),
            Map.entry("en", "en-US"),
            Map.entry("en-us", "en-US"),
            Map.entry("en-gb", "en-US"),
            Map.entry("ja", "ja-JP"),
            Map.entry("ja-jp", "ja-JP"),
            Map.entry("ko", "ko-KR"),
            Map.entry("ko-kr", "ko-KR"),
            // 东盟核心：印尼 / 越南 / 泰国 / 马来西亚
            Map.entry("id", "id-ID"),
            Map.entry("id-id", "id-ID"),
            Map.entry("vi", "vi-VN"),
            Map.entry("vi-vn", "vi-VN"),
            Map.entry("th", "th-TH"),
            Map.entry("th-th", "th-TH"),
            Map.entry("ms", "ms-MY"),
            Map.entry("ms-my", "ms-MY")
    );

    public JsonMessageSource(ResourcePatternResolver resourceResolver) {
        this.resourceResolver = resourceResolver;
        this.scanAvailableLocales();
    }

    @Override
    protected MessageFormat resolveCode(String code, Locale locale) {
        Locale resolved = this.resolveResourceLocale(locale);
        Map<String, String> messages = this.loadMessages(resolved);
        String message = messages.get(code);

        // 未命中则按语言反向匹配地区: en -> en-US；zh 优先 zh-CN
        if (message == null) {
            String lang = resolved.getLanguage();
            if (!lang.isEmpty()) {
                String regionTag = this.languageToRegionLocale.get(lang);
                if (regionTag != null) {
                    Locale regionLocale = Locale.forLanguageTag(regionTag);
                    if (!regionLocale.equals(resolved)) {
                        messages = this.loadMessages(regionLocale);
                        message = messages.get(code);
                    }
                }
            }
        }

        // 仍未命中则用默认 locale 回退（台港互不回退，统一可回 zh-CN）
        if (message == null && !resolved.equals(this.defaultLocale)) {
            messages = this.loadMessages(this.defaultLocale);
            message = messages.get(code);
        }
        return message != null ? new MessageFormat(message, locale) : null;
    }

    /// 将请求 locale 规范为资源目录标签对应的 Locale
    private Locale resolveResourceLocale(Locale locale) {
        if (locale == null) {
            return this.defaultLocale;
        }
        String tag = locale.toLanguageTag();
        String lower = tag.toLowerCase(Locale.ROOT);
        String alias = LOCALE_ALIASES.get(lower);
        if (alias != null) {
            return Locale.forLanguageTag(alias);
        }
        // 脚本 Hant 无地区 → 台湾
        if ("zh".equals(locale.getLanguage()) && "Hant".equalsIgnoreCase(locale.getScript())
                && (locale.getCountry() == null || locale.getCountry().isEmpty())) {
            return Locale.forLanguageTag("zh-TW");
        }
        // 脚本 Hans → 简体
        if ("zh".equals(locale.getLanguage()) && "Hans".equalsIgnoreCase(locale.getScript())) {
            return Locale.forLanguageTag("zh-CN");
        }
        return locale;
    }

    private Map<String, String> loadMessages(Locale locale) {
        return this.cachedMessages.computeIfAbsent(locale, this::loadFromFiles);
    }

    private Map<String, String> loadFromFiles(Locale locale) {
        Map<String, String> result = new HashMap<>();
        String localeTag = locale.toLanguageTag();
        String basePath = "classpath*:i18n/" + localeTag + "/**/*.json";
        try {
            Resource[] resources = this.resourceResolver.getResources(basePath);
            for (Resource resource : resources) {
                String filePath = resource.getURL().toString();
                String relativePath = this.extractRelativePath(filePath, localeTag);
                if (relativePath == null) {
                    continue;
                }
                String content = this.readResourceContent(resource);
                if (content == null || content.isBlank()) {
                    continue;
                }
                JSONObject jsonObj = JSONUtil.parseObj(content);
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
    String extractRelativePath(String fileUrl, String localeTag) {
        String decoded = URLDecoder.decode(fileUrl, StandardCharsets.UTF_8);
        String marker = "i18n/" + localeTag + "/";
        int idx = decoded.lastIndexOf(marker);
        if (idx < 0) {
            return null;
        }
        String relative = decoded.substring(idx + marker.length());
        if (relative.endsWith(".json")) {
            relative = relative.substring(0, relative.length() - 5);
        }
        return relative.replace('/', '.').replace('\\', '.');
    }

    /// 启动时扫描 i18n 目录下所有 locale 子目录，建立「语言码 → 地区 locale」反向映射
    private void scanAvailableLocales() {
        try {
            Resource[] resources = this.resourceResolver.getResources("classpath*:i18n/**/*.json");
            for (Resource resource : resources) {
                String localeTag = this.extractLocaleTag(resource.getURL().toString());
                if (localeTag != null && this.availableLocaleTags.add(localeTag)) {
                    Locale locale = Locale.forLanguageTag(localeTag);
                    String lang = locale.getLanguage();
                    if (!lang.isEmpty()) {
                        this.languageToRegionLocale.putIfAbsent(lang, localeTag);
                    }
                }
            }
            // 多中文地区并存时，纯语言码 zh 固定落到简体，避免扫序导致落到 zh-TW/zh-HK
            if (this.availableLocaleTags.contains("zh-CN")) {
                this.languageToRegionLocale.put("zh", "zh-CN");
            }
            if (this.availableLocaleTags.contains("en-US")) {
                this.languageToRegionLocale.put("en", "en-US");
            }
            log.debug("扫描到可用 locale: {}, 语言→地区映射: {}", this.availableLocaleTags, this.languageToRegionLocale);
        }
        catch (IOException e) {
            log.warn("扫描可用 locale 失败, 语言→地区反向回退将不可用", e);
        }
    }

    /// 从资源 URL 提取 locale 标签
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
