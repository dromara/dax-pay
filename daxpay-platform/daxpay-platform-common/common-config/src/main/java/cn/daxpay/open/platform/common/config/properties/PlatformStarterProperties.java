package cn.daxpay.open.platform.common.config.properties;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import java.util.ArrayList;
import java.util.List;
/// # Platform Starter 统一配置类
///
/// 整合了审计日志和认证相关的配置
@Getter
@Setter
@ConfigurationProperties(prefix = "daxpay.platform.starter")
public class PlatformStarterProperties {
    /// 审计日志配置
    private AuditLog auditLog = new AuditLog();
    /// 认证配置
    private Auth auth = new Auth();
    /// # 审计日志配置
    ///
    @Getter
    @Setter
    public static class AuditLog {
        /// ip地址库配置
        private Ip2region ip2region = new Ip2region();
        /// 存储方式, 默认为数据库
        private Store store = Store.JDBC;
        /// # Ip地址库配置
        ///
        @Getter
        @Setter
        public static class Ip2region {
            /// IPv4 xdb 数据文件所在路径
            private String filePath;
            /// IPv6 xdb 数据文件路径（可选, 未配置时 IPv6 地址查询返回 null）
            private String ipv6FilePath;
            /// IPv4 数据版本档次（开源版/商业基础版/高级版/专业版），默认开源版
            private Ip2regionDataVersion dataVersion = Ip2regionDataVersion.OPEN_SOURCE;
            /// IPv6 数据版本档次（可与 v4 不同），默认开源版
            private Ip2regionDataVersion ipv6DataVersion = Ip2regionDataVersion.OPEN_SOURCE;
            /// 查询模式, 默认为缓存 VectorIndex 索引
            private Ip2regionSearch searchType = Ip2regionSearch.VECTOR_INDEX;
        }
        /// # 存储类型
        ///
        public enum Store {
            /// 数据库
            JDBC
        }
        /// # Ip2region查询类型
        ///
        public enum Ip2regionSearch {
            /// 缓存 VectorIndex 索引
            VECTOR_INDEX,
            /// 缓存整个 xdb 数据
            CACHE
        }
        /// # ip2region 数据版本档次
        ///
        /// 决定 xdb 定位信息的字段下标映射。开源版与商业版字段顺序不同,
        /// 切换数据文件时必须同步配置此项, 否则解析结果错位。
        ///
        /// 各版本字段格式（| 分隔, 下标从 0 起）:
        /// - OPEN_SOURCE: 国家|省份|城市|ISP|国家码（5段）
        /// - BASE/HIGH/PRO: 大洲|国家|省份|城市|区县|ISP|...|国家码（16-18段, 国家码恒在末段）
        ///
        /// 商业三档的 country/province/city/isp 下标完全一致（1/2/3/5）, 差异仅在中间段数。
        public enum Ip2regionDataVersion {
            /// 开源版：国家|省份|城市|ISP|国家码（5段）
            OPEN_SOURCE,
            /// 商业基础版：大洲|国家|省份|城市|区县|ISP|...|国家码（16段）
            BASE,
            /// 商业高级版：基础版+ASN（17段）
            HIGH,
            /// 商业专业版：高级版+应用场景（18段）
            PRO
        }
    }
    /// # 认证配置
    ///
    @Getter
    @Setter
    public static class Auth {
        /// 不进行鉴权的路径
        private List<String> ignoreUrls = new ArrayList<>();
        /// 开启超级管理员(生产模式请关闭)
        private boolean enableAdmin = true;
        /// 用户管理列表中是否显示超级管理员用户
        private boolean adminInList = true;
    }
}


