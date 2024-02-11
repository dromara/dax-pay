package cn.bootx.platform.daxpay.service.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 演示模块配置类
 * @author xxm
 * @since 2024/2/8
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "dax-pay")
public class DaxPayProperties {
    /** 服务地址 */
    private String serverUrl;

    /** 前端地址 */
    private String frontUrl;
}
