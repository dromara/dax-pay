package cn.bootx.platform.common.cache.configuration;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * spring cache 配置
 *
 * @author xxm
 * @since 2021/6/11
 */
@Data
@Accessors(chain = true)
@ConfigurationProperties(prefix = "bootx-platform.common.cache")
public class CachingProperties {

    /** 默认超时时间 30分钟 */
    private Integer defaultTtl = 60 * 30;

    /** 自定义过期时间 秒 */
    private Map<String, Integer> keysTtl = new HashMap<>();

}
