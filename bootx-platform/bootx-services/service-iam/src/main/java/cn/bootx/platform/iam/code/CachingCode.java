package cn.bootx.platform.iam.code;

/**
 * 缓存名称
 *
 * @author xxm
 * @since 2021/6/16
 */
public interface CachingCode {

    /** 直接放行的请求权限 */
    String IGNORE_PATH = "iam:ignore:path";

    /** 用户请求权限关系缓存 */
    String USER_PATH = "iam:user:path";

    /** 用户权限码关系缓存 */
    String USER_PERM_CODE = "iam:user:perm:code";

    /** 用户数据权限关系缓存 */
    String USER_DATA_SCOPE = "iam:user:data:scope";

}
