package cn.daxpay.open.platform.common.config.properties;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/// # 平台通用配置属性
///
/// 整合了缓存、异常处理、Spring 和 Swagger 配置
@Data
@Accessors(chain = true)
@ConfigurationProperties(prefix = "daxpay.platform.common")
public class PlatformCommonProperties {
    /// 缓存配置
    private Cache cache = new Cache();
    /// 异常处理配置
    private Exception exception = new Exception();
    /// Spring 相关配置
    private Spring spring = new Spring();
    /// Swagger 配置
    private Swagger swagger = new Swagger();

    /// # 缓存配置
    ///
    @Data
    public static class Cache {
        /// 缓存总开关，关闭后 L1 本地缓存 + L2 Redis 缓存全部退化成 NoOp（直接穿透到方法）
        ///
        /// 设计要点：
        /// - L1 与 L2 是强绑定的整体（L1 加速 + L2 共享），不单独开关
        /// - true（默认）= 启用整套二级缓存；false = 彻底禁用，排查/诊断时使用
        private boolean enabled = true;
        /// L1 本地缓存配置
        private L1 l1 = new L1();
        /// L2 Redis 缓存配置
        private L2 l2 = new L2();
        /// 敏感缓存名前缀；匹配的 L2 value 整包 AES-GCM 加密（默认 secure:）
        private String securePrefix = "secure:";
        /// 额外视为敏感的 cacheName 精确列表（可选）
        private List<String> secureNames = new ArrayList<>();

        /// # L1 本地缓存配置
        ///
        @Data
        public static class L1 {
            /// L1 本地缓存单独开关，false 时关闭 L1 仅保留 L2 Redis（纯 Redis 模式）
            ///
            /// 设计要点：
            /// - 仅在 [Cache#isEnabled] 总开关开启时生效；总开关关闭时 L1/L2 一并 NoOp
            /// - L2 无单独开关：关 L2 等同于关 [Cache#isEnabled] 总开关（全关）
            /// - 关闭 L1 适用场景：强一致性要求 / 排查本地脏读 / 单机无本地加速诉求
            private boolean enabled = true;
            /// 默认超时时间 60秒
            private Long defaultTtl = 60L;
            /// 默认最大容量
            private Long maximumSize = 10000L;
        }

        /// # L2 Redis 缓存配置
        ///
        @Data
        public static class L2 {
            /// 默认超时时间 30分钟
            private Integer defaultTtl = 60 * 30;
        }
    }
    /// # 异常处理配置
    ///
    @Data
    public static class Exception {
        /// 是否显示详细异常信息
        private boolean showFullMessage;
    }
    /// # Spring 相关配置
    ///
    @Data
    public static class Spring {
        /// cors跨域配置
        private Cors cors = new Cors();
        /// http请求配置
        private Rest rest = new Rest();
        /// # CORS 跨域配置
        ///
        @Data
        public static class Cors {
            /// 是否启用 CORS
            private boolean enable = false;
            /// 允许跨域发送身份凭证
            private boolean allowCredentials = true;
            /// 预检请求有效期(秒)
            private Integer maxAge = 3600;
            /// 允许的请求头
            private String allowedHeaders = "*";
            /// 允许的请求方法
            private String allowedMethods = "*";
            /// 允许跨域的源为，注意与origin:进行区分
            private String allowedOriginPatterns = "*";
        }

        /// # RestClient 配置
        ///
        @Data
        public static class Rest {
            /// 连接池最大连接数
            private Integer maxTotal = 100;
            /// 每个目标主机最大连接数
            private Integer maxPerRoute = 20;
            /// 连接超时(秒)
            private Integer connectTimeout = 5;
            /// 套接字读取超时(秒)
            private Integer socketTimeout = 30;
            /// 响应总超时(秒)
            private Integer responseTimeout = 40;
            /// 连接池获取超时(秒)
            private Integer connectionRequestTimeout = 3;
        }
    }
    /// # Swagger 配置
    ///
    @Data
    public static class Swagger {
        /// 标题
        private String title = "DaxPay API";
        /// 作者
        private String author;
        /// 描述
        private String description = "DaxPay 接口文档";
        /// 服务条款网址
        private String termsOfServiceUrl;
        /// 版本号
        private String version = "4.0.0-beta4";
    }
}

