package cn.daxpay.open.platform.capability.social.justauth.request;

import cn.daxpay.open.platform.capability.social.justauth.SocialAuthConfig;
import cn.daxpay.open.platform.capability.social.justauth.SocialSourceEnum;
import cn.daxpay.open.platform.capability.social.justauth.exception.SocialException;
import cn.daxpay.open.platform.capability.social.justauth.model.AuthCallback;
import cn.daxpay.open.platform.capability.social.justauth.model.AuthToken;
import cn.daxpay.open.platform.capability.social.justauth.model.AuthUser;
import cn.daxpay.open.platform.capability.social.justauth.util.SocialUrlBuilder;
import cn.hutool.json.JSONObject;

/// # 企业微信(企业自建应用扫码)授权登录
///
public class WeComRequest extends AbstractSocialAuthRequest {

    public WeComRequest(SocialAuthConfig config) {
        super(config, SocialSourceEnum.WE_COM);
    }

    /// 企业微信授权地址使用 appid(corpid) 和 agentid
    @Override
    public String authorize(String state) {
        return SocialUrlBuilder.ofBaseUrl(this.getSource().authorize())
            .queryParam("appid", this.getConfig().getClientId())
            .queryParam("agentid", this.getConfig().getAgentId())
            .queryParam("redirect_uri", this.buildRedirectUri())
            .queryParam("state", state)
            .build();
    }

    /// 企业微信 access_token 由 corpid+corpsecret 换取(不依赖授权码)
    @Override
    public AuthToken getAccessToken(AuthCallback callback) {
        String url = SocialUrlBuilder.ofBaseUrl(this.getSource().accessToken())
            .queryParam("corpid", this.getConfig().getClientId())
            .queryParam("corpsecret", this.getConfig().getClientSecret())
            .build();
        JSONObject object = this.checkResponse(this.doGet(url));
        return new AuthToken()
            .setAccessToken(object.getStr("access_token"))
            .setExpireIn(object.getInt("expires_in", 0))
            .setCode(callback.getCode());
    }

    /// 先用 access_token+code 换取 UserId, 再查询用户详情
    @Override
    public AuthUser getUserInfo(AuthToken token) {
        String url = SocialUrlBuilder.ofBaseUrl(this.getSource().userInfo())
            .queryParam("access_token", token.getAccessToken())
            .queryParam("code", token.getCode())
            .build();
        JSONObject object = this.checkResponse(this.doGet(url));
        // 未返回 UserId 表示非当前企业成员
        if (!object.containsKey("UserId")) {
            // 企业微信: 非本企业成员, 不支持登录
            throw new SocialException("error.social.weCom.notCorpMember");
        }
        String userId = object.getStr("UserId");
        JSONObject detail = this.getUserDetail(token.getAccessToken(), userId, object.getStr("user_ticket"));
        return new AuthUser()
            .setUuid(userId)
            .setUsername(detail.getStr("name"))
            .setNickname(detail.getStr("alias"))
            .setAvatar(detail.getStr("avatar"))
            .setEmail(detail.getStr("email"))
            .setSource(this.getSourceName())
            .setToken(token);
    }

    /// 查询企业微信成员详情(基础信息 + 敏感信息)
    private JSONObject getUserDetail(String accessToken, String userId, String userTicket) {
        // 基础信息
        String url = SocialUrlBuilder.ofBaseUrl("https://qyapi.weixin.qq.com/cgi-bin/user/get")
            .queryParam("access_token", accessToken)
            .queryParam("userid", userId)
            .build();
        JSONObject detail = this.checkResponse(this.doGet(url));
        // 敏感信息(需 user_ticket)
        if (userTicket != null && !userTicket.isBlank()) {
            String detailUrl = SocialUrlBuilder.ofBaseUrl("https://qyapi.weixin.qq.com/cgi-bin/auth/getuserdetail")
                .queryParam("access_token", accessToken)
                .build();
            JSONObject param = new JSONObject();
            param.set("user_ticket", userTicket);
            JSONObject sensitive = this.checkResponse(this.doPost(detailUrl, param.toString()));
            detail.putAll(sensitive);
        }
        return detail;
    }

    /// 校验企业微信响应(errcode 不为 0 表示错误)
    private JSONObject checkResponse(String response) {
        JSONObject object = this.parseObj(response);
        if (object.containsKey("errcode") && object.getInt("errcode", 0) != 0) {
            throw new SocialException(object.getStr("errmsg"));
        }
        return object;
    }
}
