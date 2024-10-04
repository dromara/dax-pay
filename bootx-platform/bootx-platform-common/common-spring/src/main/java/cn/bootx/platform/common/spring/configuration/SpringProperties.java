package cn.bootx.platform.common.spring.configuration;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author xxm
 * @since 2020/4/9 13:50
 */
@ConfigurationProperties(prefix = "bootx-platform.common.spring")
@Data
@Accessors(chain = true)
public class SpringProperties {

    /** cors跨域配置 */
    private Cors cors = new Cors();

    @Data
    public static class Cors {

        /** 允许跨域发送身份凭证 */
        private boolean enable = false;

        /** 预检请求有效期(秒) */
        private Integer maxAge = 3600;

        /** 允许的请求头 */
        private String allowedHeaders = "*";

        /** 允许的请求方法 */
        private String allowedMethods = "*";

        /** 允许跨域的源为，注意与origin:进行区分 */
        private String allowedOriginPatterns = "*";

    }

}
