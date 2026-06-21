package cn.daxpay.open.platform.capability.social.justauth.request;

import cn.daxpay.open.platform.capability.social.justauth.SocialAuthConfig;
import cn.daxpay.open.platform.capability.social.justauth.SocialSourceEnum;
import cn.daxpay.open.platform.capability.social.justauth.exception.SocialException;
import cn.daxpay.open.platform.capability.social.justauth.model.AuthCallback;
import cn.daxpay.open.platform.capability.social.justauth.model.AuthToken;
import cn.daxpay.open.platform.capability.social.justauth.model.AuthUser;
import cn.hutool.json.JSONObject;

import java.util.Map;

/// # GitHub 授权登录
///
public class GithubRequest extends AbstractSocialAuthRequest {

    public GithubRequest(SocialAuthConfig config) {
        super(config, SocialSourceEnum.GITHUB);
    }

    /// GitHub 的 token 响应为 urlencoded 格式
    @Override
    public AuthToken getAccessToken(AuthCallback callback) {
        String response = this.doPost(this.accessTokenUrl(callback.getCode()));
        Map<String, String> res = this.parseForm(response);
        if (res.containsKey("error")) {
            throw new SocialException(res.get("error_description"));
        }
        return new AuthToken()
            .setAccessToken(res.get("access_token"))
            .setScope(res.get("scope"))
            .setTokenType(res.get("token_type"));
    }

    /// GitHub 用户信息需通过 Authorization 请求头携带 token
    @Override
    public AuthUser getUserInfo(AuthToken token) {
        String response = this.doGet(
            this.getSource().userInfo(),
            this.headers("Authorization", "token " + token.getAccessToken())
        );
        JSONObject object = this.parseObj(response);
        if (object.containsKey("error")) {
            throw new SocialException(object.getStr("error_description"));
        }
        return new AuthUser()
            .setUuid(object.getStr("id"))
            .setUsername(object.getStr("login"))
            .setNickname(object.getStr("name"))
            .setAvatar(object.getStr("avatar_url"))
            .setEmail(object.getStr("email"))
            .setSource(this.getSourceName())
            .setToken(token);
    }
}
