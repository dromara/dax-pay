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

/// # 微信公众号授权登录
///
public class WechatMpRequest extends AbstractSocialAuthRequest {

    public WechatMpRequest(SocialAuthConfig config) {
        super(config, SocialSourceEnum.WECHAT_MP);
    }

    /// 微信授权地址使用 appid 参数, silent 时使用 snsapi_base(静默仅 openId), 否则 snsapi_userinfo
    /// #wechat_redirect 是微信文档要求的 URL 末尾锚点, 必须拼在完整地址之后;
    /// 拼进 state 值会被编码为 %23 并随授权回显原样带回, 污染 state 导致
    /// 扫码票据的 state 一次性映射查找失败
    @Override
    public String authorize(String state) {
        String scope = this.getConfig().isSilent() ? "snsapi_base" : "snsapi_userinfo";
        return SocialUrlBuilder.ofBaseUrl(this.getSource().authorize())
            .queryParam("appid", this.getConfig().getClientId())
            .queryParam("redirect_uri", this.encode(this.buildRedirectUri()))
            .queryParam("response_type", "code")
            .queryParam("scope", scope)
            .queryParam("state", state)
            .build() + "#wechat_redirect";
    }

    /// 微信 accessToken 同时返回 openid
    @Override
    public AuthToken getAccessToken(AuthCallback callback) {
        String url = SocialUrlBuilder.ofBaseUrl(this.getSource().accessToken())
            .queryParam("appid", this.getConfig().getClientId())
            .queryParam("secret", this.getConfig().getClientSecret())
            .queryParam("code", callback.getCode())
            .queryParam("grant_type", "authorization_code")
            .build();
        JSONObject object = this.parseObj(this.doGet(url));
        this.checkResponse(object);
        return new AuthToken()
            .setAccessToken(object.getStr("access_token"))
            .setRefreshToken(object.getStr("refresh_token"))
            .setExpireIn(object.getInt("expires_in", 0))
            .setOpenId(object.getStr("openid"))
            .setScope(object.getStr("scope"));
    }

    @Override
    public AuthUser getUserInfo(AuthToken token) {
        // 静默授权(scope 不含 snsapi_userinfo)时无详细信息
        if (StrUtil.isNotBlank(token.getScope()) && !token.getScope().contains("snsapi_userinfo")) {
            return new AuthUser()
                .setUuid(token.getOpenId())
                .setSource(this.getSourceName())
                .setToken(token);
        }
        String url = SocialUrlBuilder.ofBaseUrl(this.getSource().userInfo())
            .queryParam("access_token", token.getAccessToken())
            .queryParam("openid", token.getOpenId())
            .queryParam("lang", "zh_CN")
            .build();
        JSONObject object = this.parseObj(this.doGet(url));
        this.checkResponse(object);
        if (object.containsKey("unionid")) {
            token.setUnionId(object.getStr("unionid"));
        }
        return new AuthUser()
            .setUuid(token.getOpenId())
            .setUsername(object.getStr("nickname"))
            .setNickname(object.getStr("nickname"))
            .setAvatar(object.getStr("headimgurl"))
            .setSource(this.getSourceName())
            .setToken(token);
    }

    /// 校验微信响应(errcode 不为 0 表示错误)
    private void checkResponse(JSONObject object) {
        if (object.containsKey("errcode") && object.getInt("errcode", 0) != 0) {
            throw new SocialException(object.getInt("errcode"), object.getStr("errmsg"));
        }
    }
}
