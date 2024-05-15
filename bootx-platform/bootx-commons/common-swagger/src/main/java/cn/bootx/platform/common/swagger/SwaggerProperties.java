package cn.bootx.platform.common.swagger;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * swagger配置
 *
 * @author xxm
 * @since 2020/4/9 13:36
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "bootx.common.swagger")
public class SwaggerProperties {

    /**
     * 是否开启
     */
    private boolean enabled;

    /**
     * 业务模块模块
     */
    private Map<String, List<String>> basePackages = new LinkedHashMap<>();

    /**
     * Basic 认证控制
     */
    private Basic basic = new Basic();

    /**
     * 标题
     */
    private String title;

    /**
     * 作者
     */
    private String author;

    /**
     * 描述
     */
    private String description;

    /**
     * 服务条款网址
     */
    private String termsOfServiceUrl;

    /**
     * 版本号
     */
    private String version;

    /**
     * 开源协议
     */
    private String licenseName = "Apache 2.0";

    /**
     * 开源协议地址
     */
    private String licenseUrl = "https://www.apache.org/licenses/LICENSE-2.0";

    /**
     * Basic 认证控制
     */
    @Getter
    @Setter
    public static class Basic {

        /**
         * basic是否开启,默认为false
         */
        private boolean enable = false;

        /**
         * basic 用户名
         */
        private String username = "bootx";

        /**
         * basic 密码
         */
        private String password = "123456";

    }

}
