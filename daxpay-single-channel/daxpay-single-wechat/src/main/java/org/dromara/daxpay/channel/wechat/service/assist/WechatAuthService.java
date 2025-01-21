package org.dromara.daxpay.channel.wechat.service.assist;

import org.dromara.daxpay.channel.wechat.entity.config.WechatPayConfig;
import org.dromara.daxpay.channel.wechat.service.config.WechatPayConfigService;
import org.dromara.daxpay.core.param.assist.GenerateAuthUrlParam;
import org.dromara.daxpay.core.result.assist.AuthResult;
import org.dromara.daxpay.core.result.assist.AuthUrlResult;
import org.dromara.daxpay.service.entity.config.PlatformConfig;
import org.dromara.daxpay.service.service.config.PlatformConfigService;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.bean.oauth2.WxOAuth2AccessToken;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.config.impl.WxMpDefaultConfigImpl;
import org.springframework.stereotype.Service;

/**
 * 支付支撑服务类
 * @author xxm
 * @since 2024/2/10
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WechatAuthService {

    private final WechatPayConfigService weChatPayConfigService;

    private final PlatformConfigService platformsConfigService;
    private final WechatPayConfigService wechatPayConfigService;
    private final PlatformConfigService platformConfigService;

    /**
     * 构建微信oauth2授权的url连接
     */
    public AuthUrlResult generateAuthUrl(GenerateAuthUrlParam param) {
        WxMpService wxMpService = this.getWxMpService();
        // 手段传输回调地址, 直接拼接不做处理
        if (StrUtil.isNotBlank(param.getAuthRedirectUrl())){
            String url = wxMpService.getOAuth2Service()
                    .buildAuthorizationUrl(param.getAuthRedirectUrl(), WxConsts.OAuth2Scope.SNSAPI_BASE, "");
            return new AuthUrlResult().setAuthUrl(url);
        } else {
            PlatformConfig platformConfig = platformsConfigService.getConfig();
            String queryCode = RandomUtil.randomString(10);
            // 判断是否独立部署前端
            String serverUrl = platformConfig.getGatewayMobileUrl();
            String redirectUrl = StrUtil.format("{}/wechat/auth/{}/{}/{}", serverUrl, param.getAppId(), param.getChannel(),queryCode);
            String authUrl = wxMpService.getOAuth2Service().buildAuthorizationUrl(redirectUrl, WxConsts.OAuth2Scope.SNSAPI_BASE, "");
            return new AuthUrlResult().setAuthUrl(authUrl).setQueryCode(queryCode);
        }
    }

    /**
     * 生成内部使用的授权链接, 返回授权码给调用者, 由调用者自己再去获取授权信息
     * @param authPath 授权链接, 需要为h5项目中的可访问路由路径
     */
    public String generateInnerAuthUrl(String authPath) {
        WechatPayConfig config = wechatPayConfigService.getWechatPayConfig(false);
        WxMpService wxMpService = this.getWxMpService(config);

        // 如果配置中有地址配置则使用, 没有的话使用平台地址进行拼接
        String serverUrl = config.getAuthUrl();
        if (StrUtil.isBlank(serverUrl)){
            PlatformConfig platformConfig = platformConfigService.getConfig();
            serverUrl = platformConfig.getGatewayMobileUrl();
        }
        String redirectUrl = StrUtil.format("{}{}", serverUrl, authPath);
        return wxMpService.getOAuth2Service().buildAuthorizationUrl(redirectUrl, WxConsts.OAuth2Scope.SNSAPI_BASE, "");
    }

    /**
     * 获取微信AccessToken数据
     */
    @SneakyThrows
    public AuthResult getTokenAndOpenId(String authCode){
        WxMpService wxMpService = this.getWxMpService();
        WxOAuth2AccessToken accessToken = wxMpService.getOAuth2Service().getAccessToken(authCode);
        return new AuthResult()
                .setAccessToken(accessToken.getAccessToken())
                .setOpenId(accessToken.getOpenId());
    }

    /**
     * 获取微信公众号API的Service
     */
    private WxMpService getWxMpService() {
        WechatPayConfig config = wechatPayConfigService.getAndCheckConfig(false);
        return getWxMpService(config);
    }
    /**
     * 获取微信公众号API的Service
     */
    private WxMpService getWxMpService(WechatPayConfig config) {
        WxMpService wxMpService = new WxMpServiceImpl();
        WxMpDefaultConfigImpl wxMpConfig = new WxMpDefaultConfigImpl();
        wxMpConfig.setAppId(config.getWxAppId()); // 设置微信公众号的appid
        wxMpConfig.setSecret(config.getAppSecret()); // 设置微信公众号的app corpSecret
        wxMpService.setWxMpConfigStorage(wxMpConfig);
        return wxMpService;
    }
}
