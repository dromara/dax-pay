package cn.bootx.platform.starter.auth.configuration;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * 认证配置参数
 *
 * @author xxm
 * @since 2021/7/30
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "bootx.starter.auth")
public class AuthProperties {

    /** 不进行鉴权的路径 */
    private List<String> ignoreUrls = new ArrayList<>();

    /** 盐值 */
    private String salt = "salt";

    /** 默认密码 */
    private String defaultPassword = "123456";

    /** 开启超级管理员(生产模式推荐关闭) */
    private boolean enableAdmin = true;

    /** 三方登录配置 */
    private ThirdLogin thirdLogin = new ThirdLogin();

    /**
     * 三方登录配置
     */
    @Data
    public static class ThirdLogin {

        /** 钉钉 */
        private DingTalk dingTalk = new DingTalk();

        /** 微信开放平台 */
        private WeChatOpen weChatOpen = new WeChatOpen();

        /** 企业微信 */
        private WeCom weCom = new WeCom();

        @Getter
        @Setter
        public static class DingTalk extends ThirdLoginConfig {

            /** 不是组织用户是否可以进行用户绑定 */
            private boolean checkBelongOrg;

        }

        @Getter
        @Setter
        public static class WeChatOpen extends ThirdLoginConfig {

        }

        @Getter
        @Setter
        public static class WeCom extends ThirdLoginConfig {

            /** 企业微信，授权方的网页应用ID */
            private String agentId;

        }

    }

    @Data
    public static class ThirdLoginConfig {

        /** 客户端id：对应各平台的appKey */
        private String clientId;

        /** 客户端Secret：对应各平台的appSecret */
        private String clientSecret;

        /** 登录成功后的回调地址 */
        private String redirectUri;

    }

}
