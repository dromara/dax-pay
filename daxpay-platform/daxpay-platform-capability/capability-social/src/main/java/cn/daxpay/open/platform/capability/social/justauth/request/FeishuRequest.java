package cn.daxpay.open.platform.capability.social.justauth.request;

import cn.daxpay.open.platform.capability.social.justauth.SocialAuthConfig;
import cn.daxpay.open.platform.capability.social.justauth.SocialSourceEnum;
import cn.daxpay.open.platform.capability.social.justauth.exception.SocialException;
import cn.daxpay.open.platform.capability.social.justauth.model.AuthCallback;
import cn.daxpay.open.platform.capability.social.justauth.model.AuthToken;
import cn.daxpay.open.platform.capability.social.justauth.model.AuthUser;
import cn.daxpay.open.platform.capability.social.justauth.util.SocialUrlBuilder;
import cn.hutool.json.JSONObject;

/// # 飞书(企业自建应用)授权登录
///
public class FeishuRequest extends AbstractSocialAuthRequest {

    /// 飞书 app_access_token 获取地址
    private static final String APP_ACCESS_TOKEN_URL = "https://open.feishu.cn/open-apis/auth/v3/app_access_token/internal/";

    public FeishuRequest(SocialAuthConfig config) {
        super(config, SocialSourceEnum.FEISHU);
    }

    /// 飞书授权地址使用 app_id
    @Override
    public String authorize(String state) {
        return SocialUrlBuilder.ofBaseUrl(this.getSource().authorize())
            .queryParam("app_id", this.getConfig().getClientId())
            .queryParam("redirect_uri", this.encode(this.getConfig().getRedirectUri()))
            .queryParam("state", state)
            .build();
    }

    /// 飞书需先获取 app_access_token, 再换取用户 access_token
    @Override
    public AuthToken getAccessToken(AuthCallback callback) {
        String appAccessToken = this.getAppAccessToken();
        JSONObject param = new JSONObject();
        param.set("app_access_token", appAccessToken);
        param.set("grant_type", "authorization_code");
        param.set("code", callback.getCode());
        JSONObject response = this.parseObj(this.doPost(
            this.getSource().accessToken(), param.toString(), this.headers("Content-Type", "application/json")
        ));
        this.checkResponse(response);
        JSONObject data = response.getJSONObject("data");
        return new AuthToken()
            .setAccessToken(data.getStr("access_token"))
            .setRefreshToken(data.getStr("refresh_token"))
            .setExpireIn(data.getInt("expires_in", 0))
            .setTokenType(data.getStr("token_type"))
            .setOpenId(data.getStr("open_id"));
    }

    /// 飞书用户信息通过 Authorization Bearer 头携带 access_token, 响应包裹在 data 中
    @Override
    public AuthUser getUserInfo(AuthToken token) {
        String response = this.doGet(
            this.getSource().userInfo(),
            this.headers("Content-Type", "application/json", "Authorization", "Bearer " + token.getAccessToken())
        );
        JSONObject object = this.parseObj(response);
        this.checkResponse(object);
        JSONObject data = object.getJSONObject("data");
        return new AuthUser()
            .setUuid(data.getStr("union_id"))
            .setUsername(data.getStr("name"))
            .setNickname(data.getStr("name"))
            .setAvatar(data.getStr("avatar_url"))
            .setEmail(data.getStr("email"))
            .setSource(this.getSourceName())
            .setToken(token);
    }

    /// 获取飞书 app_access_token(企业自建应用)
    /// 注: 首期不缓存, 每次请求重新获取, 后续可改为 Redis 缓存以降低调用频次
    private String getAppAccessToken() {
        JSONObject param = new JSONObject();
        param.set("app_id", this.getConfig().getClientId());
        param.set("app_secret", this.getConfig().getClientSecret());
        String response = this.doPost(APP_ACCESS_TOKEN_URL, param.toString(), this.headers("Content-Type", "application/json"));
        JSONObject object = this.parseObj(response);
        this.checkResponse(object);
        return object.getStr("app_access_token");
    }

    /// 校验飞书响应(code 不为 0 表示错误)
    private void checkResponse(JSONObject object) {
        if (object.getInt("code", -1) != 0) {
            throw new SocialException(object.getStr("message"));
        }
    }
}
