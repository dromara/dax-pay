package cn.daxpay.multi.channel.wechat.service.assist;

import cn.daxpay.multi.channel.wechat.entity.config.WechatPayConfig;
import cn.daxpay.multi.channel.wechat.result.assist.WxTokenAndOpenIdResult;
import cn.daxpay.multi.channel.wechat.service.config.WechatPayConfigService;
import cn.daxpay.multi.core.param.assist.GenerateAuthUrlParam;
import cn.daxpay.multi.core.result.assist.AuthUrlResult;
import cn.daxpay.multi.core.result.assist.OpenIdResult;
import cn.daxpay.multi.service.entity.config.PlatformConfig;
import cn.daxpay.multi.service.service.config.PlatformConfigService;
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
import org.springframework.data.redis.core.StringRedisTemplate;
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
public class WechatAuthService {

    public static final String OPEN_ID_KEY_PREFIX = "daxpay:wechat:openId:";

    private final WechatPayConfigService weChatPayConfigService;

    private final PlatformConfigService platformsConfigService;

    private final StringRedisTemplate redisTemplate;

    /**
     * 构建微信oauth2授权的url连接
     */
    public AuthUrlResult generateAuthUrl(GenerateAuthUrlParam param) {
        WxMpService wxMpService = this.getWxMpService();
        if (StrUtil.isNotBlank(param.getAuthRedirectUrl())){
            String url = wxMpService.getOAuth2Service()
                    .buildAuthorizationUrl(param.getAuthRedirectUrl(), WxConsts.OAuth2Scope.SNSAPI_BASE, "");
            return new AuthUrlResult().setAuthUrl(url);
        } else {
            PlatformConfig platformConfig = platformsConfigService.getConfig();
            String code = RandomUtil.randomString(10);
            // 读取平台配置
            String serverUrl = platformConfig.getGatewayServiceUrl();
            // 构建出授权后重定向页面链接
            String redirectUrl = StrUtil.format("{}/unipay/callback/wechat/auth/{}", serverUrl, code);
            // 写入Redis, 五分钟有效期
            redisTemplate.opsForValue().set(OPEN_ID_KEY_PREFIX + code, "", 5*60*1000L);
            String url = wxMpService.getOAuth2Service()
                    .buildAuthorizationUrl(redirectUrl, WxConsts.OAuth2Scope.SNSAPI_BASE, "");
            return new AuthUrlResult().setAuthUrl(url).setCode(code);
        }
    }

    /**
     * 获取微信AccessToken数据
     */
    @SneakyThrows
    public WxTokenAndOpenIdResult getTokenAndOpenId(String authCode){
        WxMpService wxMpService = this.getWxMpService();
        WxOAuth2AccessToken accessToken = wxMpService.getOAuth2Service().getAccessToken(authCode);
        return new WxTokenAndOpenIdResult()
                .setAccessToken(accessToken.getAccessToken())
                .setOpenId(accessToken.getOpenId());
    }

    /**
     * 微信授权回调页面, 通过获取到authCode获取到OpenId, 存到到Redis中对应code关联的键值对中
     * @param authCode 微信返回的授权码
     * @param code 标识码
     */
    public void authCallback(String authCode, String code) {
        // 获取OpenId
        WxTokenAndOpenIdResult wxAccessToken = this.getTokenAndOpenId(authCode);
        // 写入Redis
        redisTemplate.opsForValue().set(OPEN_ID_KEY_PREFIX + code, wxAccessToken.getOpenId(), 60*1000L);
    }

    /**
     * 通过标识码轮训获取OpenId
     */
    public OpenIdResult queryOpenId(String code) {
        // 从redis中获取
        String openId = redisTemplate.opsForValue().get(OPEN_ID_KEY_PREFIX + code);
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
        WechatPayConfig config = weChatPayConfigService.getWechatPayConfig();
        WxMpService wxMpService = new WxMpServiceImpl();
        WxMpDefaultConfigImpl wxMpConfig = new WxMpDefaultConfigImpl();
        wxMpConfig.setAppId(config.getWxAppId()); // 设置微信公众号的appid
        wxMpConfig.setSecret(config.getAppSecret()); // 设置微信公众号的app corpSecret
        wxMpService.setWxMpConfigStorage(wxMpConfig);
        return wxMpService;
    }
}
