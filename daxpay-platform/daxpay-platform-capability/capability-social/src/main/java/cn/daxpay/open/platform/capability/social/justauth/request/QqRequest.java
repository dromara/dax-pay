package cn.daxpay.open.platform.capability.social.justauth.request;

import cn.daxpay.open.platform.capability.social.justauth.SocialAuthConfig;
import cn.daxpay.open.platform.capability.social.justauth.SocialSourceEnum;
import cn.daxpay.open.platform.capability.social.justauth.exception.SocialException;
import cn.daxpay.open.platform.capability.social.justauth.model.AuthCallback;
import cn.daxpay.open.platform.capability.social.justauth.model.AuthToken;
import cn.daxpay.open.platform.capability.social.justauth.model.AuthUser;
import cn.daxpay.open.platform.capability.social.justauth.util.SocialUrlBuilder;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;

import java.util.Map;

/// # QQ 授权登录
///
public class QqRequest extends AbstractSocialAuthRequest {

    public QqRequest(SocialAuthConfig config) {
        super(config, SocialSourceEnum.QQ);
    }

    /// QQ 的 token 响应为 urlencoded 格式
    @Override
    public AuthToken getAccessToken(AuthCallback callback) {
        String response = this.doGet(this.accessTokenUrl(callback.getCode()));
        Map<String, String> res = this.parseForm(response);
        if (!res.containsKey("access_token") || res.containsKey("code")) {
            throw new SocialException(res.get("msg"));
        }
        return new AuthToken()
            .setAccessToken(res.get("access_token"))
            .setRefreshToken(res.get("refresh_token"))
            .setExpireIn(this.parseInt(res.get("expires_in")));
    }

    /// QQ 需先获取 openId, 再查询用户信息
    @Override
    public AuthUser getUserInfo(AuthToken token) {
        String openId = this.getOpenId(token);
        String url = SocialUrlBuilder.ofBaseUrl(this.getSource().userInfo())
            .queryParam("access_token", token.getAccessToken())
            .queryParam("oauth_consumer_key", this.getConfig().getClientId())
            .queryParam("openid", openId)
            .build();
        JSONObject object = this.parseObj(this.doGet(url));
        if (object.getInt("ret", -1) != 0) {
            throw new SocialException(object.getStr("msg"));
        }
        String avatar = object.getStr("figureurl_qq_2");
        if (StrUtil.isBlank(avatar)) {
            avatar = object.getStr("figureurl_qq_1");
        }
        return new AuthUser()
            .setUuid(StrUtil.isBlank(token.getUnionId()) ? openId : token.getUnionId())
            .setUsername(object.getStr("nickname"))
            .setNickname(object.getStr("nickname"))
            .setAvatar(avatar)
            .setSource(this.getSourceName())
            .setToken(token);
    }

    /// 获取 QQ 用户的 openId(可选返回 unionId)
    private String getOpenId(AuthToken token) {
        String url = SocialUrlBuilder.ofBaseUrl("https://graph.qq.com/oauth2.0/me")
            .queryParam("access_token", token.getAccessToken())
            .queryParam("unionid", this.getConfig().isUnionId() ? 1 : 0)
            .build();
        // QQ 返回格式为 callback({...}); 需截取
        String response = this.doGet(url).replace("callback(", "").replace(");", "").trim();
        JSONObject object = this.parseObj(response);
        if (object.containsKey("error")) {
            throw new SocialException(object.getStr("error_description"));
        }
        token.setOpenId(object.getStr("openid"));
        if (object.containsKey("unionid")) {
            token.setUnionId(object.getStr("unionid"));
        }
        return token.getOpenId();
    }

    private int parseInt(String value) {
        if (StrUtil.isBlank(value)) {
            return 0;
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
