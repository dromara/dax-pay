package cn.daxpay.open.platform.capability.social.justauth.request;

import cn.daxpay.open.platform.capability.social.justauth.SocialAuthConfig;
import cn.daxpay.open.platform.capability.social.justauth.SocialSourceEnum;
import cn.daxpay.open.platform.capability.social.justauth.exception.SocialException;
import cn.daxpay.open.platform.capability.social.justauth.model.AuthCallback;
import cn.daxpay.open.platform.capability.social.justauth.model.AuthToken;
import cn.daxpay.open.platform.capability.social.justauth.model.AuthUser;
import cn.daxpay.open.platform.capability.social.justauth.util.SocialUrlBuilder;
import cn.hutool.json.JSONObject;

/// # 钉钉(新版扫码登录)授权登录
///
public class DingTalkRequest extends AbstractSocialAuthRequest {

    public DingTalkRequest(SocialAuthConfig config) {
        super(config, SocialSourceEnum.DINGTALK);
    }

    /// 钉钉授权地址使用 client_id
    @Override
    public String authorize(String state) {
        return SocialUrlBuilder.ofBaseUrl(this.getSource().authorize())
            .queryParam("response_type", "code")
            .queryParam("client_id", this.getConfig().getClientId())
            .queryParam("redirect_uri", this.getConfig().getRedirectUri())
            .queryParam("prompt", "consent")
            .queryParam("state", state)
            .build();
    }

    /// 钉钉使用 POST JSON 换取 access_token, 响应字段为驼峰命名
    @Override
    public AuthToken getAccessToken(AuthCallback callback) {
        JSONObject param = new JSONObject();
        param.set("grantType", "authorization_code");
        param.set("clientId", this.getConfig().getClientId());
        param.set("clientSecret", this.getConfig().getClientSecret());
        param.set("code", callback.getCode());
        JSONObject object = this.parseObj(this.doPost(
            this.getSource().accessToken(), param.toString(), this.headers("Content-Type", "application/json")
        ));
        if (!object.containsKey("accessToken")) {
            throw new SocialException(object.getStr("message"));
        }
        return new AuthToken()
            .setAccessToken(object.getStr("accessToken"))
            .setRefreshToken(object.getStr("refreshToken"))
            .setExpireIn(object.getInt("expireIn", 0))
            .setUnionId(object.getStr("corpId"));
    }

    /// 钉钉用户信息通过 x-acs-dingtalk-access-token 头携带
    @Override
    public AuthUser getUserInfo(AuthToken token) {
        String response = this.doGet(
            this.getSource().userInfo(),
            this.headers("x-acs-dingtalk-access-token", token.getAccessToken())
        );
        JSONObject object = this.parseObj(response);
        token.setOpenId(object.getStr("openId"));
        if (object.containsKey("unionId")) {
            token.setUnionId(object.getStr("unionId"));
        }
        return new AuthUser()
            .setUuid(object.getStr("unionId"))
            .setUsername(object.getStr("nick"))
            .setNickname(object.getStr("nick"))
            .setAvatar(object.getStr("avatarUrl"))
            .setSource(this.getSourceName())
            .setToken(token);
    }
}
