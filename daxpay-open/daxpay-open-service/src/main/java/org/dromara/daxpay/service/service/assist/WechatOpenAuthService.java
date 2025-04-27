package org.dromara.daxpay.service.service.assist;

import org.dromara.daxpay.core.context.MchAppLocal;
import org.dromara.daxpay.core.exception.OperationFailException;
import org.dromara.daxpay.core.param.assist.GenerateAuthUrlParam;
import org.dromara.daxpay.core.result.assist.AuthResult;
import org.dromara.daxpay.core.result.assist.AuthUrlResult;
import org.dromara.daxpay.service.common.local.PaymentContextLocal;
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

/**
 * 微信认证
 * @author xxm
 * @since 2025/3/31
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WechatOpenAuthService {

    /**
     * 构建微信oauth2授权的url连接
     */
    @Deprecated
    public AuthUrlResult generateAuthUrl(GenerateAuthUrlParam param, String channelAuthPath,String appId, String appSecret) {
        MchAppLocal mchAppInfo = PaymentContextLocal.get().getMchAppInfo();
        WxMpService wxMpService = this.getWxMpService(appId,appSecret);
        // 手段传输回调地址, 直接拼接不做处理
            String queryCode = RandomUtil.randomString(10);
            // 如果配置中有地址配置则使用, 没有的话使用平台地址进行拼接
            String serverUrl =channelAuthPath;
            if (StrUtil.isBlank(serverUrl)){
                serverUrl = mchAppInfo.getGatewayMobileUrl();
            }
            String redirectUrl = StrUtil.format("{}/auth/wechat/{}/{}/{}", param.getAppId(), param.getChannel(),queryCode);
            String authUrl = wxMpService.getOAuth2Service().buildAuthorizationUrl(redirectUrl, WxConsts.OAuth2Scope.SNSAPI_BASE, "");
            return new AuthUrlResult().setAuthUrl(authUrl).setQueryCode(queryCode);
    }

    /**
     * 生成内部使用的授权链接, 返回授权码给调用者, 由调用者自己再去获取授权信息
     * @param authPath 回调的认证路径, 需要为h5项目中的可访问路由路径
     * @param serverUrl 用于微信的重定向url地址, 需要转发或重定向到h5项目的地址上
     */
    public AuthUrlResult generateInnerAuthUrl(String authPath, String serverUrl, String channel, String appId, String wxAppId, String appSecret) {
        WxMpService wxMpService = this.getWxMpService(wxAppId,appSecret);
        // 如果配置中有地址配置则使用, 没有的话使用平台地址进行拼接
        if (StrUtil.isBlank(serverUrl)){
            MchAppLocal mchAppInfo = PaymentContextLocal.get().getMchAppInfo();
            serverUrl = mchAppInfo.getGatewayMobileUrl();
        }
        String queryCode = RandomUtil.randomString(10);
        // 如果授权地址为空
        if (StrUtil.isBlank(authPath)){
            authPath = StrUtil.format("/auth/wechat/{}/{}/{}",appId, channel,queryCode);
        }

        String redirectUrl = StrUtil.format("{}{}", serverUrl, authPath);
        String authUrl =  wxMpService.getOAuth2Service().buildAuthorizationUrl(redirectUrl, WxConsts.OAuth2Scope.SNSAPI_BASE, "");
        return new AuthUrlResult().setAuthUrl(authUrl).setQueryCode(queryCode);
    }

    /**
     * 获取微信AccessToken数据
     */
    public AuthResult getTokenAndOpenId(String authCode, String appId, String appSecret){
        WxMpService wxMpService = this.getWxMpService(appId,appSecret);
        WxOAuth2AccessToken accessToken;
        try {
            accessToken = wxMpService.getOAuth2Service().getAccessToken(authCode);
        } catch (WxErrorException e) {
            throw new OperationFailException("微信认证失败: "+e.getMessage());
        }
        return new AuthResult()
                .setAccessToken(accessToken.getAccessToken())
                .setOpenId(accessToken.getOpenId());
    }

    /**
     * 获取微信公众号API的Service
     */
    private WxMpService getWxMpService(String appId, String appSecret) {
        WxMpService wxMpService = new WxMpServiceImpl();
        WxMpDefaultConfigImpl wxMpConfig = new WxMpDefaultConfigImpl();
        // 设置微信公众号的appid
        wxMpConfig.setAppId(appId);
        // 设置微信公众号的app corpSecret
        wxMpConfig.setSecret(appSecret);
        wxMpService.setWxMpConfigStorage(wxMpConfig);
        return wxMpService;
    }
}
