package cn.bootx.platform.starter.data.perm.configuration;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 数据权限配置
 *
 * @author xxm
 * @since 2021/12/3
 */
@Getter
@Setter
@ConfigurationProperties("bootx.starter.data-perm")
public class DataPermProperties {

    /** 开启字段加密 */
    private boolean enableFieldDecrypt = true;

    /** 字段加密key 需要符合AES秘钥的要求 */
    private String fieldDecryptKey = "UCrtxSCwYZNCIlav";

    /** 开启数据权限 */
    private boolean enableDataPerm = true;

    /** 部门关联类型数据权限关联表配置 */
    private DataPerm deptDataPerm = new DataPerm("iam_user_dept", "user_id", "dept_id");

    /** 角色关联类型数据权限关联表配置 */
    private DataPerm roleDataPerm = new DataPerm("iam_user_role", "user_id", "role _id");

    /** 开启查询字段权限 */
    private boolean enableSelectFieldPerm = true;

    /**
     * 部门相关数据权限
     */
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DataPerm {

        /** 关联表名 */
        private String table;

        /** 查询字段(输出字段名, 通常为用户字段, 用于筛选出子查询的结果) */
        private String queryField;

        /** 条件字段(筛选条件字段名) */
        private String whereField;
    }

}
