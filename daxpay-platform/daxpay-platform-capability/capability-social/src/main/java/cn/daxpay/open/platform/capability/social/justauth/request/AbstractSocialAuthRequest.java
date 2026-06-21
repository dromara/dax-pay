package cn.daxpay.open.platform.capability.social.justauth.request;

import cn.daxpay.open.platform.capability.social.justauth.SocialAuthConfig;
import cn.daxpay.open.platform.capability.social.justauth.SocialSourceEnum;
import cn.daxpay.open.platform.capability.social.justauth.exception.SocialException;
import cn.daxpay.open.platform.capability.social.justauth.model.AuthCallback;
import cn.daxpay.open.platform.capability.social.justauth.model.AuthToken;
import cn.daxpay.open.platform.capability.social.justauth.model.AuthUser;
import cn.daxpay.open.platform.capability.social.justauth.util.SocialUrlBuilder;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

/// # 社交授权请求抽象模板
///
/// 参考 JustAuth 的 AuthDefaultRequest 重新实现, 封装通用的 OAuth2 授权码流程
///
@Getter
@RequiredArgsConstructor
public abstract class AbstractSocialAuthRequest implements SocialAuthRequest {

    /// 授权配置
    protected final SocialAuthConfig config;

    /// 平台来源
    protected final SocialSourceEnum source;

    /// 生成授权地址(通用 OAuth2 标准: response_type/client_id/redirect_uri/state)
    @Override
    public String authorize(String state) {
        return SocialUrlBuilder.ofBaseUrl(source.authorize())
            .queryParam("response_type", "code")
            .queryParam("client_id", config.getClientId())
            .queryParam("redirect_uri", this.buildRedirectUri())
            .queryParam("state", state)
            .build();
    }

    /// 完整登录流程: 换取令牌 + 获取用户信息, state 校验由上层负责
    @Override
    public AuthUser login(AuthCallback callback) {
        if (StrUtil.isBlank(callback.getCode())) {
            // 授权码不能为空
            throw new SocialException("error.social.authCodeBlank");
        }
        AuthToken token = this.getAccessToken(callback);
        return this.getUserInfo(token);
    }

    /// 返回来源平台名称
    public String getSourceName() {
        return source.getCode();
    }

    // ==================== 子类可复用的工具方法 ====================

    /// 构建获取 accessToken 的地址(通用 OAuth2 标准)
    protected String accessTokenUrl(String code) {
        return SocialUrlBuilder.ofBaseUrl(source.accessToken())
            .queryParam("code", code)
            .queryParam("client_id", config.getClientId())
            .queryParam("client_secret", config.getClientSecret())
            .queryParam("grant_type", "authorization_code")
            .queryParam("redirect_uri", this.buildRedirectUri())
            .build();
    }

    /// 构建获取 userInfo 的地址(通用形式: 追加 access_token)
    protected String userInfoUrl(AuthToken token) {
        return SocialUrlBuilder.ofBaseUrl(source.userInfo())
            .queryParam("access_token", token.getAccessToken())
            .build();
    }

    /// 构建回调地址(配置基础路径 + 平台编码, 形如 .../oauth-callback/{source})
    /// authorize 与 accessToken 两处必须一致, 集中在此方法保证.
    /// 配置项 redirect_uri 约定为前端回调基础路径(不含 source), 如:
    ///   http://127.0.0.1:13333/auth/oauth-callback
    /// 实际传给第三方的为:
    ///   http://127.0.0.1:13333/auth/oauth-callback/gitee
    protected String buildRedirectUri() {
        String base = config.getRedirectUri();
        if (base == null) {
            base = "";
        }
        // 去掉末尾斜杠, 避免出现 //gitee
        if (base.endsWith("/")) {
            base = base.substring(0, base.length() - 1);
        }
        return base + "/" + source.getCode();
    }

    /// GET 请求
    protected String doGet(String url) {
        return this.doGet(url, null);
    }

    /// GET 请求(带请求头)
    protected String doGet(String url, Map<String, String> headers) {
        HttpRequest request = HttpUtil.createGet(url);
        if (headers != null) {
            headers.forEach(request::header);
        }
        try (HttpResponse response = request.execute()) {
            return response.body();
        }
    }

    /// POST 请求
    protected String doPost(String url) {
        return this.doPost(url, null, null);
    }

    /// POST 请求(带请求体)
    protected String doPost(String url, String body) {
        return this.doPost(url, body, null);
    }

    /// POST 请求(带请求体和请求头)
    protected String doPost(String url, String body, Map<String, String> headers) {
        HttpRequest request = HttpUtil.createPost(url);
        if (StrUtil.isNotBlank(body)) {
            request.body(body);
        }
        if (headers != null) {
            headers.forEach(request::header);
        }
        try (HttpResponse response = request.execute()) {
            return response.body();
        }
    }

    /// 构建请求头
    protected Map<String, String> headers(String... kv) {
        Map<String, String> map = new HashMap<>();
        for (int i = 0; i + 1 < kv.length; i += 2) {
            map.put(kv[i], kv[i + 1]);
        }
        return map;
    }

    /// 将响应解析为 JSONObject
    protected JSONObject parseObj(String response) {
        return JSONUtil.parseObj(response);
    }

    /// URL 编码
    protected String encode(String value) {
        return URLUtil.encode(value);
    }

    /// 解析 urlencoded 表单字符串为 Map(GitHub/QQ 的 token 响应使用此格式)
    protected Map<String, String> parseForm(String response) {
        Map<String, String> map = new HashMap<>();
        if (StrUtil.isBlank(response)) {
            return map;
        }
        for (String pair : response.split("&")) {
            int idx = pair.indexOf("=");
            if (idx > 0) {
                String key = pair.substring(0, idx);
                String value = URLUtil.decode(pair.substring(idx + 1));
                map.put(key, value);
            }
        }
        return map;
    }
}
