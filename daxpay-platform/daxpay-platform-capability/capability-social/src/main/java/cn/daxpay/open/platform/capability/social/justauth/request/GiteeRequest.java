package cn.daxpay.open.platform.capability.social.justauth.request;

import cn.daxpay.open.platform.capability.social.justauth.SocialAuthConfig;
import cn.daxpay.open.platform.capability.social.justauth.SocialSourceEnum;
import cn.daxpay.open.platform.capability.social.justauth.exception.SocialException;
import cn.daxpay.open.platform.capability.social.justauth.model.AuthCallback;
import cn.daxpay.open.platform.capability.social.justauth.model.AuthToken;
import cn.daxpay.open.platform.capability.social.justauth.model.AuthUser;
import cn.hutool.json.JSONObject;

/// # Gitee 码云授权登录
///
public class GiteeRequest extends AbstractSocialAuthRequest {

    public GiteeRequest(SocialAuthConfig config) {
        super(config, SocialSourceEnum.GITEE);
    }

    /// Gitee 的 token 响应为 JSON 格式
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

    @Override
    public AuthUser getUserInfo(AuthToken token) {
        String response = this.doGet(this.userInfoUrl(token));
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
