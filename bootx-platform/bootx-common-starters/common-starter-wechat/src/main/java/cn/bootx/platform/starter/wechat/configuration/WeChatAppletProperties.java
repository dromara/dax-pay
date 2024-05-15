package cn.bootx.platform.starter.wechat.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 微信公众平台配置
 *
 * @author xxm
 * @since 2022/7/15
 */
@Getter
@Setter
@ConfigurationProperties("bootx.starter.third.wechat-applet")
public class WeChatAppletProperties {

    /** AppKey */
    private String appId = "?";

    /** AppSecret */
    private String appSecret = "?";

}
