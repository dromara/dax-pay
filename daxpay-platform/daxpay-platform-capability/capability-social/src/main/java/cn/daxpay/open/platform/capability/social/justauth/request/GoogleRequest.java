package cn.daxpay.open.platform.capability.social.justauth.request;

import cn.daxpay.open.platform.capability.social.justauth.SocialAuthConfig;
import cn.daxpay.open.platform.capability.social.justauth.SocialSourceEnum;
import cn.daxpay.open.platform.capability.social.justauth.exception.SocialException;
import cn.daxpay.open.platform.capability.social.justauth.model.AuthCallback;
import cn.daxpay.open.platform.capability.social.justauth.model.AuthToken;
import cn.daxpay.open.platform.capability.social.justauth.model.AuthUser;
import cn.daxpay.open.platform.capability.social.justauth.util.SocialUrlBuilder;
import cn.hutool.json.JSONObject;

/// # Google 授权登录
///
/// [Google OAuth2.0 / OpenID Connect](https://developers.google.com/identity/openid-connect/openid-connect)
/// authorize 必须显式携带 scope(openid email profile), 否则无法获取用户信息;
/// token 响应为 JSON, 用户信息接口需通过 Authorization: Bearer 头携带令牌,
/// 用户唯一标识为 sub 字段.
///
public class GoogleRequest extends AbstractSocialAuthRequest {

    public GoogleRequest(SocialAuthConfig config) {
        super(config, SocialSourceEnum.GOOGLE);
    }

    /// Google 授权地址必须显式声明 scope(openid email profile)
    /// 默认通用 authorize 方法不带 scope, 故在此重写
    @Override
    public String authorize(String state) {
        return SocialUrlBuilder.ofBaseUrl(this.getSource().authorize())
            .queryParam("response_type", "code")
            .queryParam("client_id", this.getConfig().getClientId())
            .queryParam("redirect_uri", this.buildRedirectUri())
            .queryParam("state", state)
            .queryParam("scope", "openid email profile")
            .build();
    }

    /// Google 的 token 响应为 JSON 格式
    @Override
    public AuthToken getAccessToken(AuthCallback callback) {
        String response = this.doPost(this.accessTokenUrl(callback.getCode()));
        JSONObject object = this.parseObj(response);
        if (object.containsKey("error")) {
            throw new SocialException(object.getStr("error_description"));
        }
        return new AuthToken()
            .setAccessToken(object.getStr("access_token"))
            .setRefreshToken(object.getStr("refresh_token"))
            .setScope(object.getStr("scope"))
            .setTokenType(object.getStr("token_type"))
            .setExpireIn(object.getInt("expires_in", 0));
    }

    /// Google 用户信息需通过 Authorization: Bearer 头携带令牌
    /// 唯一标识为 sub, 昵称取 name, 头像取 picture, 邮箱取 email
    @Override
    public AuthUser getUserInfo(AuthToken token) {
        String response = this.doGet(
            this.getSource().userInfo(),
            this.headers("Authorization", "Bearer " + token.getAccessToken())
        );
        JSONObject object = this.parseObj(response);
        if (object.containsKey("error")) {
            throw new SocialException(object.getStr("error_description"));
        }
        return new AuthUser()
            .setUuid(object.getStr("sub"))
            .setUsername(object.getStr("email"))
            .setNickname(object.getStr("name"))
            .setAvatar(object.getStr("picture"))
            .setEmail(object.getStr("email"))
            .setSource(this.getSourceName())
            .setToken(token);
    }
}
