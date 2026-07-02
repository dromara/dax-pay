package cn.daxpay.open.payment.old.pay.service.assist;

import cn.daxpay.open.platform.core.exception.operation.OperationFailException;
import cn.daxpay.open.platform.core.code.CommonCode;
import cn.daxpay.open.payment.unipay.result.assist.AuthResult;
import cn.daxpay.open.payment.unipay.result.assist.AuthUrlResult;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.bean.oauth2.WxOAuth2AccessToken;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.config.impl.WxMpDefaultConfigImpl;
import org.springframework.stereotype.Service;

/// # 微信认证
///
@Slf4j
@Service
@RequiredArgsConstructor
public class WechatOpenAuthService {

    /// 生成内部使用的授权链接, 返回授权码给调用者, 由调用者自己再去获取授权信息
    /// @param authPath 回调的认证路径, 需要为h5项目中的可访问路由路径
    /// @param serverUrl 用于微信的重定向url地址, 需要转发或重定向到h5项目的地址上
    public AuthUrlResult generateInnerAuthUrl(String authPath, String serverUrl, String channel, String appId, String wxAppId, String appSecret) {
        WxMpService wxMpService = this.getWxMpService(wxAppId,appSecret);
        // 如果配置中有地址配置则使用, 没有的话使用平台地址进行拼接
        if (StrUtil.isBlank(serverUrl)){
            serverUrl = "";
        }
        String queryCode = RandomUtil.randomString(10);
        // 如果授权地址为空
        if (StrUtil.isBlank(authPath)){
            // 拼接授权地址 t 说明使用道通认证 f 说明不使用通道认证
            authPath = StrUtil.format("/auth/wechat/{}/{}/f/{}",appId, channel,queryCode);
        }

        String redirectUrl = StrUtil.format("{}{}", serverUrl, authPath);
        String authUrl =  wxMpService.getOAuth2Service().buildAuthorizationUrl(redirectUrl, WxConsts.OAuth2Scope.SNSAPI_BASE, "");
        return new AuthUrlResult().setAuthUrl(authUrl).setQueryCode(queryCode);
    }

    /// 获取微信AccessToken数据
    public AuthResult getTokenAndOpenId(String authCode, String wxAppId, String appSecret){
        WxMpService wxMpService = this.getWxMpService(wxAppId,appSecret);
        WxOAuth2AccessToken accessToken;
        try {
            accessToken = wxMpService.getOAuth2Service().getAccessToken(authCode);
        } catch (WxErrorException e) {
            // 微信认证失败
            throw new OperationFailException(CommonCode.FAIL_CODE, "pay.error.assist.wechatAuthFail", e.getMessage());
        }
        return new AuthResult()
                .setAccessToken(accessToken.getAccessToken())
                .setOpenId(accessToken.getOpenId());
    }

    /// 获取微信公众号API的Service
    private WxMpService getWxMpService(String wxAppId, String appSecret) {
        WxMpService wxMpService = new WxMpServiceImpl();
        WxMpDefaultConfigImpl wxMpConfig = new WxMpDefaultConfigImpl();
        // 设置微信公众号的appid
        wxMpConfig.setAppId(wxAppId);
        // 设置微信公众号的app corpSecret
        wxMpConfig.setSecret(appSecret);
        try {
            wxMpService.setWxMpConfigStorage(wxMpConfig);
        } catch (Exception e) {
           // 微信认证配置错误
            throw new OperationFailException(CommonCode.FAIL_CODE, "pay.error.assist.wechatAuthConfigError", e.getMessage());
        }
        return wxMpService;
    }
}

