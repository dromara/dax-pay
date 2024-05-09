package cn.bootx.platform.starter.dingtalk.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 钉钉应用配置
 *
 * @author xxm
 * @since 2022/7/15
 */
@Getter
@Setter
@ConfigurationProperties("bootx.starter.third.ding-talk")
public class DingTalkProperties {

    /** AppKey */
    private String appKey;

    /** AppSecret */
    private String appSecret;

    /** 应用id */
    private Long agentId;

}
