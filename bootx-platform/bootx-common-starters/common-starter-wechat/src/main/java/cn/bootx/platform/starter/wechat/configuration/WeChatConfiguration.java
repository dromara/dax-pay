package cn.bootx.platform.starter.wechat.configuration;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.impl.WxMaServiceImpl;
import cn.binarywang.wx.miniapp.config.WxMaConfig;
import cn.binarywang.wx.miniapp.config.impl.WxMaDefaultConfigImpl;
import lombok.RequiredArgsConstructor;
import me.chanjar.weixin.mp.api.*;
import me.chanjar.weixin.mp.api.impl.*;
import me.chanjar.weixin.mp.config.WxMpConfigStorage;
import me.chanjar.weixin.mp.config.impl.WxMpDefaultConfigImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 微信配置
 *
 * @author xxm
 * @since 2022/7/15
 */
@Configuration
@RequiredArgsConstructor
public class WeChatConfiguration {

    private final WeChatProperties weChatProperties;

    private final WeChatAppletProperties weChatAppletProperties;

    /**
     * 微信公众号APIService
     */
    @Bean
    public WxMpService wxMpService(WxMpConfigStorage wxMpConfigStorage) {
        WxMpService wxMpService = new WxMpServiceImpl();
        wxMpService.setWxMpConfigStorage(wxMpConfigStorage);
        return wxMpService;
    }

    /**
     * 微信配置
     */
    @Bean
    public WxMpConfigStorage wxMpConfigStorage() {
        WxMpDefaultConfigImpl config = new WxMpDefaultConfigImpl();
        config.setAppId(weChatProperties.getAppId()); // 设置微信公众号的appid
        config.setSecret(weChatProperties.getAppSecret()); // 设置微信公众号的app corpSecret
        config.setToken(weChatProperties.getToken()); // 设置微信公众号的Token
        config.setAesKey(weChatProperties.getEncodingAesKey()); // 消息加解密密钥
        return config;
    }
    @Bean
    public WxMaService wxMaService(WxMaConfig wxMaConfigStorage) {
        WxMaService wxMpService = new WxMaServiceImpl();
        wxMpService.setWxMaConfig(wxMaConfigStorage);
        return wxMpService;
    }
    @Bean
    public WxMaConfig wxMaConfigStorage() {
        WxMaDefaultConfigImpl config = new WxMaDefaultConfigImpl();
        config.setAppid(weChatAppletProperties.getAppId()); // 设置微信公众号的appid
        config.setSecret(weChatAppletProperties.getAppSecret()); // 设置微信公众号的app corpSecret
        return config;
    }
}
