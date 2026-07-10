package cn.daxpay.open.platform.capability.douyin.auth.service;

import cn.daxpay.open.platform.capability.douyin.auth.result.DouyinAuthResult;
import cn.daxpay.open.platform.core.exception.operation.OperationFailException;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 抖音 H5 认证服务
///
/// 封装抖音开放平台 H5 场景的两个核心操作:
/// - **构造静默授权链接**(silent_auth): 在抖音 App 内 WebView 打开后自动获取 code
/// - **code 换 openId**: 调用 `/oauth/access_token/` 接口换取用户 openId
///
/// 与微信公众号 SDK([WechatMpAuthService]) 不同, 抖音 H5 静默授权仅需简单 HTTP 调用,
/// 不依赖第三方 SDK, 直接使用 Hutool HttpUtil 完成。
///
/// ## 能力边界
/// - 生成静默授权链接 / authCode 换 openId(HTTP 直连, 无官方 SDK)
/// - 不做配置存储 / state / queryCode 缓存(由调用方自行管理)
///
/// 参考文档: https://developer.open-douyin.com/docs/resource/zh-CN/dop/ability/opensdk/user-authorization/get-login-openid
@Slf4j
@Service
public class DouyinH5AuthService {

    /// 抖音静默授权地址(在抖音 App 内 WebView 打开后自动完成授权并重定向)
    private static final String SILENT_AUTH_URL = "https://aweme.snssdk.com/passport/open/silent_auth/";

    /// 抖音 access_token 换取地址(code → openId)
    private static final String ACCESS_TOKEN_URL = "https://open.douyin.com/oauth/access_token/";

    /// 静默授权 scope(固定 login_id)
    private static final String SCOPE_LOGIN_ID = "login_id";

    /// 构造抖音静默授权链接
    ///
    /// 抖音 silent_auth 不支持在 redirect_uri 中携带 query 参数,
    /// authToken 通过 path 段或 state 传递, state 用于 CSRF 校验。
    ///
    /// @param clientKey   抖音开放平台 Client Key
    /// @param redirectUri 授权回调地址(不含 query 参数)
    /// @param state       CSRF 校验随机串
    /// @return 完整的静默授权链接
    public String buildSilentAuthUrl(String clientKey, String redirectUri, String state) {
        return SILENT_AUTH_URL
                + "?client_key=" + URLUtil.encode(clientKey)
                + "&response_type=code"
                + "&scope=" + SCOPE_LOGIN_ID
                + "&redirect_uri=" + URLUtil.encode(redirectUri)
                + "&state=" + URLUtil.encode(state);
    }

    /// 通过授权码 code 换取用户 openId
    ///
    /// 调用抖音开放平台 `/oauth/access_token/` 接口(POST form-urlencoded),
    /// 响应 JSON 包裹在 `data` 节点下, `data.open_id` 为用户唯一标识。
    ///
    /// @param clientKey    抖音开放平台 Client Key
    /// @param clientSecret 抖音开放平台 Client Secret
    /// @param code         用户授权码
    /// @return 授权结果(openId + accessToken)
    public DouyinAuthResult getOpenIdByCode(String clientKey, String clientSecret, String code) {
        HttpRequest request = HttpUtil.createPost(ACCESS_TOKEN_URL)
                .header("Content-Type", "application/x-www-form-urlencoded");
        request.form("client_key", clientKey);
        request.form("client_secret", clientSecret);
        request.form("code", code);
        request.form("grant_type", "authorization_code");

        String body;
        try (HttpResponse response = request.execute()) {
            body = response.body();
        }
        log.info("抖音 access_token 换票响应: {}", body);
        JSONObject object = JSONUtil.parseObj(body);
        JSONObject data = object.getJSONObject("data");
        if (data == null) {
            // 抖音: 换取用户标识失败: {0}
            throw new OperationFailException("error.douyin.authFailed",
                    object.getStr("message", body));
        }
        // error_code 非 0 表示业务错误
        int errorCode = data.getInt("error_code", -1);
        if (errorCode != 0) {
            // 抖音: 换取用户标识失败: {0}
            throw new OperationFailException("error.douyin.authFailed",
                    data.getStr("description", "error_code=" + errorCode));
        }
        String openId = data.getStr("open_id");
        if (StrUtil.isBlank(openId)) {
            // 抖音: 获取用户标识失败
            throw new OperationFailException("error.douyin.authFailed", "openId is blank");
        }
        return new DouyinAuthResult()
                .setOpenId(openId)
                .setAccessToken(data.getStr("access_token"));
    }
}
