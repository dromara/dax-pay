package cn.bootx.platform.starter.wecom.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 企业微信配置
 *
 * @author xxm
 * @since 2022/7/22
 */
@Getter
@Setter
@ConfigurationProperties("bootx.starter.third.wecom")
public class WeComProperties {

    /** 应用id */
    private Integer agentId;

    /** 企业id */
    private String corpId;

    /** 企业Secret */
    private String corpSecret;

    /** token */
    private String token;

    /** 消息加解密密钥 */
    private String encodingAesKey;

}
