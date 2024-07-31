package cn.bootx.platform.starter.audit.log.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 审计日志配置
 *
 * @author xxm
 * @since 2021/12/2
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "bootx-platform.starter.audit-log")
public class AuditLogProperties {

    /**
     * ip地址库配置
     */
    private Ip2region ip2region = new Ip2region();

    /**
     * 存储方式, 默认为数据库
     */
    private Store store = Store.JDBC;

    /**
     * Ip地址库配置
     */
    @Getter
    @Setter
    public static class Ip2region{
        /** ip2region.xdb 数据文件所在路径 */
        private String filePath;
        /** 查询模式, 默认为缓存 VectorIndex 索引 */
        private Ip2regionSearch searchType = Ip2regionSearch.VECTOR_INDEX;
    }

    /**
     * 存储类型
     */
    public enum Store {

        /** 数据库 */
        JDBC,
        /** MongoDB */
        MONGO

    }
    /**
     * Ip2region查询类型
     */
    public enum Ip2regionSearch {
        /** 完全基于文件的查询(不推荐) */
        FILE,
        /** 缓存 VectorIndex 索引 */
        VECTOR_INDEX,
        /** 缓存整个 xdb 数据 */
        CACHE
    }

}
