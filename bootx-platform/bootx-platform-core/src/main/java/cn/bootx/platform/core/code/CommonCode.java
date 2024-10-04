package cn.bootx.platform.core.code;

/**
 * 公共常量
 *
 * @author xxm
 * @since 2020/4/8 10:58
 */
public interface CommonCode {

    /** 服务器地址 */
    String SERVER_URL = "ServerUrl";

    /** 开发环境 */
    String ENV_DEV = "dev";

    /** 测试环境 */
    String ENV_TEST = "test";

    /** 生产环境 */
    String ENV_PROD = "prod";

    /**
     * 实体类删除标记
     */
    String DELETE_FLAG = "1";

    /**
     * 实体类正常标记
     */
    String NORMAL_FLAG = "0";

    /**
     * 系统默认用户的 userId，便于定时任务和异步任务时使用
     */
    Long SYSTEM_DEFAULT_USERID = 1L;

    /**
     * 系统默认用户的 userId，便于定时任务和异步任务时使用
     */
    String SYSTEM_DEFAULT_USERNAME = "系统";

    /**
     * 响应成功码
     */
    int SUCCESS_CODE = 0;

    /**
     * 响应失败码
     */
    int FAIL_CODE = 1;

    /** 追踪Id */
    String TRACE_ID = "traceId";

    /** 用户 */
    String USER = "user";

    /** 用户id */
    String USER_ID = "userId";

    /** 主键字段 */
    String ID = "id";

    /** 创建人字段 */
    String CREATOR = "creator";

    /** 创建时间字段 */
    String CREATE_TIME = "createTime";

    /** 最后更新人字段 */
    String LAST_MODIFIER = "lastModifier";

    /** 最后更新时间字段 */
    String LAST_MODIFIED_TIME = "lastModifiedTime";

    /** 数据版本号字段 */
    String VERSION = "version";

    /** 数据软删除标识字段 */
    String DELETED = "deleted";

}
