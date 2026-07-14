package cn.daxpay.open.platform.capability.wechat.auth.service;

import cn.daxpay.open.platform.core.exception.operation.OperationFailException;
import cn.daxpay.open.platform.capability.wechat.auth.result.WechatAuthResult;
import cn.daxpay.open.platform.capability.wechat.auth.result.WechatAuthUrlResult;
import cn.hutool.core.util.RandomUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.bean.oauth2.WxOAuth2AccessToken;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.config.impl.WxMpDefaultConfigImpl;
import org.springframework.stereotype.Service;

/// # 微信公众号认证服务
///
@Slf4j
@Service
@RequiredArgsConstructor
public class WechatMpAuthService {

    /// 生成授权链接（静默授权）
    /// @param redirectUrl 回调地址(固定路径, 不含动态段)
    /// @param appId 微信公众号AppId
    /// @param appSecret 微信公众号AppSecret
    /// @param state OAuth state 参数(透传会话标识 authToken, 回调后从 state 恢复)
    public WechatAuthUrlResult generateAuthUrl(String redirectUrl, String appId, String appSecret, String state) {
        WxMpService wxMpService = this.getWxMpService(appId, appSecret);
        String queryCode = RandomUtil.randomString(10);
        String authUrl = wxMpService.getOAuth2Service().buildAuthorizationUrl(redirectUrl, WxConsts.OAuth2Scope.SNSAPI_BASE, state);
        return new WechatAuthUrlResult().setAuthUrl(authUrl).setQueryCode(queryCode);
    }

    /// 获取微信AccessToken和OpenId
    /// @param authCode 授权码
    /// @param appId 微信公众号AppId
    /// @param appSecret 微信公众号AppSecret
    public WechatAuthResult getTokenAndOpenId(String authCode, String appId, String appSecret) {
        WxMpService wxMpService = this.getWxMpService(appId, appSecret);
        WxOAuth2AccessToken accessToken;
        try {
            accessToken = wxMpService.getOAuth2Service().getAccessToken(authCode);
        } catch (WxErrorException e) {
            // 微信: 微信公众号认证失败: {0}
            throw new OperationFailException("error.channel.wechat.mpAuthFailed", e.getMessage());
        }
        return new WechatAuthResult()
                .setAccessToken(accessToken.getAccessToken())
                .setOpenId(accessToken.getOpenId());
    }

    /// 获取微信公众号API的Service
    public WxMpService getWxMpService(String appId, String appSecret) {
        WxMpService wxMpService = new WxMpServiceImpl();
        var wxMpConfig = new WxMpDefaultConfigImpl();
        wxMpConfig.setAppId(appId);
        wxMpConfig.setSecret(appSecret);
        try {
            wxMpService.setWxMpConfigStorage(wxMpConfig);
        } catch (Exception e) {
            // 微信: 微信公众号认证配置错误: {0}
            throw new OperationFailException("error.channel.wechat.mpAuthConfigError", e.getMessage());
        }
        return wxMpService;
    }
}

