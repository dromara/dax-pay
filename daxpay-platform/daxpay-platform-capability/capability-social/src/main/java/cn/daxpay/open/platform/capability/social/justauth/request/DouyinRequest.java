package cn.daxpay.open.platform.capability.social.justauth.request;

import cn.daxpay.open.platform.capability.social.justauth.SocialAuthConfig;
import cn.daxpay.open.platform.capability.social.justauth.SocialSourceEnum;
import cn.daxpay.open.platform.capability.social.justauth.exception.SocialException;
import cn.daxpay.open.platform.capability.social.justauth.model.AuthCallback;
import cn.daxpay.open.platform.capability.social.justauth.model.AuthToken;
import cn.daxpay.open.platform.capability.social.justauth.model.AuthUser;
import cn.daxpay.open.platform.capability.social.justauth.util.SocialUrlBuilder;
import cn.hutool.json.JSONObject;

/// # 抖音(网站应用扫码登录)
///
/// 抖音开放平台 OAuth2 流程, 使用 client_key 标识应用, 响应体统一包裹在 data 节点下
///
public class DouyinRequest extends AbstractSocialAuthRequest {

    public DouyinRequest(SocialAuthConfig config) {
        super(config, SocialSourceEnum.DOUYIN);
    }

    /// 抖音授权地址使用 client_key, 扫码登录固定 scope=user_info
    @Override
    public String authorize(String state) {
        return SocialUrlBuilder.ofBaseUrl(this.getSource().authorize())
            .queryParam("client_key", this.getConfig().getClientId())
            .queryParam("response_type", "code")
            .queryParam("scope", "user_info")
            .queryParam("redirect_uri", this.buildRedirectUri())
            .queryParam("state", state)
            .build();
    }

    /// 抖音使用 POST JSON 换取 access_token, 响应包裹在 data 节点下
    @Override
    public AuthToken getAccessToken(AuthCallback callback) {
        JSONObject param = new JSONObject();
        param.set("client_key", this.getConfig().getClientId());
        param.set("client_secret", this.getConfig().getClientSecret());
        param.set("code", callback.getCode());
        param.set("grant_type", "authorization_code");
        JSONObject object = this.parseObj(this.doPost(
            this.getSource().accessToken(), param.toString(), this.headers("Content-Type", "application/json")
        ));
        JSONObject data = object.getJSONObject("data");
        if (data == null || !data.containsKey("access_token")) {
            throw new SocialException(object.getStr("message"));
        }
        return new AuthToken()
            .setAccessToken(data.getStr("access_token"))
            .setRefreshToken(data.getStr("refresh_token"))
            .setOpenId(data.getStr("open_id"))
            .setExpireIn(data.getInt("expires_in", 0));
    }

    /// 抖音用户信息接口需携带 access_token 头 + open_id 参数, 响应包裹在 data 节点下
    @Override
    public AuthUser getUserInfo(AuthToken token) {
        String url = SocialUrlBuilder.ofBaseUrl(this.getSource().userInfo())
            .queryParam("open_id", token.getOpenId())
            .build();
        String response = this.doGet(url, this.headers("access-token", token.getAccessToken()));
        JSONObject object = this.parseObj(response);
        JSONObject data = object.getJSONObject("data");
        if (data == null) {
            throw new SocialException(object.getStr("message"));
        }
        return new AuthUser()
            .setUuid(token.getOpenId())
            .setUsername(data.getStr("nickname"))
            .setNickname(data.getStr("nickname"))
            .setAvatar(data.getStr("avatar"))
            .setSource(this.getSourceName())
            .setToken(token);
    }
}
