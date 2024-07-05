package cn.bootx.platform.common.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * swagger配置
 *
 * @author xxm
 * @since 2020/4/9 13:36
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "bootx-platform.config")
public class BootxConfigProperties {

    /** 终端编码 */
    private String clientCode = "";

}
