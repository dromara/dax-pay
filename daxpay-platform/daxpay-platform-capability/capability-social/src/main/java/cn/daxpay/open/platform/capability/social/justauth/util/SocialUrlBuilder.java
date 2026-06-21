package cn.daxpay.open.platform.capability.social.justauth.util;

import cn.hutool.core.util.StrUtil;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/// # 社交登录 URL 构建工具
///
/// 参考 JustAuth 的 UrlBuilder 重新实现, 用于在基础地址上追加查询参数
///
public final class SocialUrlBuilder {

    private final StringBuilder url;

    private SocialUrlBuilder(String baseUrl) {
        this.url = new StringBuilder(baseUrl);
    }

    /// 以基础地址创建构建器
    public static SocialUrlBuilder ofBaseUrl(String baseUrl) {
        return new SocialUrlBuilder(baseUrl);
    }

    /// 追加查询参数, 值为 null 或空串时跳过
    public SocialUrlBuilder queryParam(String key, Object value) {
        if (value == null) {
            return this;
        }
        String strValue = String.valueOf(value);
        if (StrUtil.isBlank(strValue)) {
            return this;
        }
        // 首个参数用 ? 分隔, 后续用 & 分隔
        if (this.url.indexOf("?") == -1) {
            this.url.append("?");
        } else {
            this.url.append("&");
        }
        this.url.append(URLEncoder.encode(key, StandardCharsets.UTF_8));
        this.url.append("=");
        this.url.append(URLEncoder.encode(strValue, StandardCharsets.UTF_8));
        return this;
    }

    /// 构建最终 URL
    public String build() {
        return this.url.toString();
    }
}
