package cn.daxpay.open.platform.capability.wechat.auth.service;

import cn.daxpay.open.platform.core.exception.operation.OperationFailException;
import cn.daxpay.open.platform.capability.wechat.auth.result.WechatAuthResult;
import cn.daxpay.open.platform.capability.wechat.auth.result.WechatAuthUrlResult;
import cn.daxpay.open.platform.capability.wechat.auth.result.WechatUserInfoResult;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.bean.WxOAuth2UserInfo;
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
    /// @param redirectUrl 回调地址
    /// @param appId 微信公众号AppId
    /// @param appSecret 微信公众号AppSecret
    public WechatAuthUrlResult generateAuthUrl(String redirectUrl, String appId, String appSecret) {
        WxMpService wxMpService = this.getWxMpService(appId, appSecret);
        String queryCode = RandomUtil.randomString(10);
        String authUrl = wxMpService.getOAuth2Service().buildAuthorizationUrl(redirectUrl, WxConsts.OAuth2Scope.SNSAPI_BASE, "");
        return new WechatAuthUrlResult().setAuthUrl(authUrl).setQueryCode(queryCode);
    }

    /// 生成用户信息授权链接
    /// @param redirectUrl 回调地址
    /// @param appId 公众号AppId
    /// @param appSecret 公众号AppSecret
    /// @return 授权链接结果
    public WechatAuthUrlResult generateUserInfoAuthUrl(String redirectUrl, String appId, String appSecret) {
        WxMpService wxMpService = this.getWxMpService(appId, appSecret);
        String queryCode = RandomUtil.randomString(10);
        // 使用SNSAPI_USERINFO获取用户信息
        String authUrl = wxMpService.getOAuth2Service().buildAuthorizationUrl(redirectUrl, WxConsts.OAuth2Scope.SNSAPI_USERINFO, "");
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

    /// 获取用户信息（包含OpenId和用户详情）
    /// @param authCode 授权码
    /// @param appId 公众号AppId
    /// @param appSecret 公众号AppSecret
    /// @return 用户信息结果
    public WechatUserInfoResult getUserInfoByAuthCode(String authCode, String appId, String appSecret) {
        WxMpService wxMpService = this.getWxMpService(appId, appSecret);
        
        try {
            // 获取AccessToken
            WxOAuth2AccessToken accessToken = wxMpService.getOAuth2Service().getAccessToken(authCode);
            
            // 获取用户信息
            WxOAuth2UserInfo userInfo = wxMpService.getOAuth2Service().getUserInfo(accessToken, null);
            
            // 转换结果
            var result = new WechatUserInfoResult();
            result.setOpenId(userInfo.getOpenid());
            result.setNickname(userInfo.getNickname());
            result.setHeadImgUrl(userInfo.getHeadImgUrl());
            result.setSex(userInfo.getSex());
            result.setCountry(userInfo.getCountry());
            result.setProvince(userInfo.getProvince());
            result.setCity(userInfo.getCity());
            // 注意：weixin-java-common 4.8.1.B版本的WxOAuth2UserInfo不再提供getLanguage()方法
            result.setUnionId(userInfo.getUnionId());
            
            log.info("获取用户信息成功，openId: {}", userInfo.getOpenid());
            return result;
            
        } catch (WxErrorException e) {
            log.error("获取用户信息失败，错误: {}", e.getMessage());
            // 微信: 获取用户信息失败: {0}
            throw new OperationFailException("error.channel.wechat.userInfoFetchFailed", e.getMessage());
        }
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

