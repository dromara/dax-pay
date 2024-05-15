package cn.bootx.platform.starter.wecom.configuration;

import lombok.RequiredArgsConstructor;
import me.chanjar.weixin.cp.api.WxCpService;
import me.chanjar.weixin.cp.api.impl.WxCpServiceImpl;
import me.chanjar.weixin.cp.config.WxCpConfigStorage;
import me.chanjar.weixin.cp.config.impl.WxCpDefaultConfigImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author xxm
 * @since 2022/7/23
 */
@Configuration
@RequiredArgsConstructor
public class WeComConfiguration {

    private final WeComProperties weComProperties;

    /**
     * 微信公众号APIService
     */
    @Bean
    public WxCpService wxCpService(WxCpConfigStorage wxMpConfigStorage) {
        WxCpService wxMpService = new WxCpServiceImpl();
        wxMpService.setWxCpConfigStorage(wxMpConfigStorage);
        return wxMpService;
    }

    /**
     * 微信配置
     */
    @Bean
    public WxCpConfigStorage wxCpConfigStorage() {
        WxCpDefaultConfigImpl config = new WxCpDefaultConfigImpl();
        config.setAgentId(weComProperties.getAgentId());
        config.setCorpId(weComProperties.getCorpId());
        config.setCorpSecret(weComProperties.getCorpSecret());
        config.setToken(weComProperties.getToken()); // 设置微信公众号的Token
        config.setAesKey(weComProperties.getEncodingAesKey()); // 消息加解密密钥
        return config;
    }

}
