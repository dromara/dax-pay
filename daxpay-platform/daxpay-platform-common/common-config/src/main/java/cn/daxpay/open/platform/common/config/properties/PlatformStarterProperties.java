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
            /// ip2region.xdb 数据文件所在路径
            private String filePath;
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


