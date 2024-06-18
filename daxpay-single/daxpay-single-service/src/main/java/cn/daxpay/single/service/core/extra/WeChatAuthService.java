package cn.daxpay.single.service.core.extra;

import cn.bootx.platform.common.redis.RedisClient;
import cn.daxpay.single.core.param.assist.WxAccessTokenParam;
import cn.daxpay.single.core.param.assist.WxAuthUrlParam;
import cn.daxpay.single.core.result.assist.WxAccessTokenResult;
import cn.daxpay.single.core.result.assist.WxAuthUrlResult;
import cn.daxpay.single.service.core.channel.wechat.entity.WeChatPayConfig;
import cn.daxpay.single.service.core.channel.wechat.service.WeChatPayConfigService;
import cn.daxpay.single.service.core.system.config.entity.PlatformConfig;
import cn.daxpay.single.service.core.system.config.service.PlatformConfigService;
import cn.daxpay.single.service.dto.extra.AuthUrlResult;
import cn.daxpay.single.service.dto.extra.OpenIdResult;
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

import java.util.Objects;

/**
 * 支付支撑服务类
 * @author xxm
 * @since 2024/2/10
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WeChatAuthService {

    public static final String OPEN_ID_KEY_PREFIX = "daxpay:wechat:openId:";

    private final WeChatPayConfigService weChatPayConfigService;

    private final PlatformConfigService platformsConfigService;

    private final RedisClient redisClient;

    /**
     * 构建微信oauth2授权的url连接
     */
    public WxAuthUrlResult getWxAuthUrl(WxAuthUrlParam param) {
        WxMpService wxMpService = this.getWxMpService();
        String url = wxMpService.getOAuth2Service()
                .buildAuthorizationUrl(param.getUrl(), WxConsts.OAuth2Scope.SNSAPI_BASE, param.getState());
        return new WxAuthUrlResult().setUrl(url);
    }

    /**
     * 获取微信AccessToken数据
     */
    @SneakyThrows
    public WxAccessTokenResult getWxAccessToken(WxAccessTokenParam wxAccessToken){
        WxMpService wxMpService = this.getWxMpService();
        WxOAuth2AccessToken accessToken = wxMpService.getOAuth2Service().getAccessToken(wxAccessToken.getCode());
        return new WxAccessTokenResult()
                .setAccessToken(accessToken.getAccessToken())
                .setOpenId(accessToken.getOpenId());
    }

    /**
     * 生成一个用于微信授权页面的链接和code标识
     */
    public AuthUrlResult generateAuthUrl() {
        PlatformConfig config = platformsConfigService.getConfig();
        String code = RandomUtil.randomString(10);
        // 构建出授权后重定向页面链接
        String redirectUrl = StrUtil.format("{}/callback/pay/wechat/auth/{}", config.getWebsiteUrl(), code);
        WxAuthUrlResult result = this.getWxAuthUrl(new WxAuthUrlParam().setUrl(redirectUrl));

        // 写入Redis, 五分钟有效期
        redisClient.setWithTimeout(OPEN_ID_KEY_PREFIX + code, "", 5*60*1000L);
        return new AuthUrlResult()
                .setCode(code)
                .setAuthUrl(result.getUrl());
    }

    /**
     * 微信授权回调页面, 通过获取到authCode获取到OpenId, 存到到Redis中对应code关联的键值对中
     * @param authCode 微信返回的授权码
     * @param code 标识码
     */
    public void authCallback(String authCode, String code) {
        // 获取OpenId
        WxAccessTokenParam param = new WxAccessTokenParam();
        param.setCode(authCode);
        WxAccessTokenResult wxAccessToken = this.getWxAccessToken(new WxAccessTokenParam().setCode(authCode));
        // 写入Redis
        redisClient.setWithTimeout(OPEN_ID_KEY_PREFIX + code, wxAccessToken.getOpenId(), 60*1000L);
    }

    /**
     * 通过标识码轮训获取OpenId
     */
    public OpenIdResult queryOpenId(String code) {
        // 从redis中获取
        String openId = redisClient.get(OPEN_ID_KEY_PREFIX + code);
        // 不为空存在
        if (StrUtil.isNotBlank(openId)){
            return new OpenIdResult().setOpenId(openId).setStatus("success");
        }
        // 为空获取中
        if (Objects.equals(openId, "")){
            return new OpenIdResult().setStatus("pending");
        }
        // null不存在
        return new OpenIdResult().setStatus("fail");
    }


    /**
     * 获取微信公众号API的Service
     */
    private WxMpService getWxMpService() {
        WeChatPayConfig config = weChatPayConfigService.getConfig();
        WxMpService wxMpService = new WxMpServiceImpl();
        WxMpDefaultConfigImpl wxMpConfig = new WxMpDefaultConfigImpl();
        wxMpConfig.setAppId(config.getWxAppId()); // 设置微信公众号的appid
        wxMpConfig.setSecret(config.getAppSecret()); // 设置微信公众号的app corpSecret
        wxMpService.setWxMpConfigStorage(wxMpConfig);
        return wxMpService;
    }
}
