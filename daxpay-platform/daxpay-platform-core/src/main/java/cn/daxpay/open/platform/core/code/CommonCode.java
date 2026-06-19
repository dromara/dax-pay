package cn.daxpay.open.platform.core.code;

/// # 公共常量
///
public interface CommonCode {

    /// 响应成功码
    int SUCCESS_CODE = 0;

    /// 响应成功
    String SUCCESS_MSG = "success";

    /// 响应失败码
    int FAIL_CODE = 1;

    /// 追踪Id
    String TRACE_ID = "traceId";

    /// 用户
    String USER = "user";

    /// 创建人字段
    String CREATOR = "creator";

    /// 创建时间字段
    String CREATE_TIME = "createTime";

    /// 最后更新人字段
    String LAST_MODIFIER = "lastModifier";

    /// 最后更新时间字段
    String LAST_MODIFIED_TIME = "lastModifiedTime";

    /// 数据版本号字段
    String VERSION = "version";

    /// 数据软删除标识字段
    String DELETED = "deleted";

    /// 危险SQL异常
    int DANGER_SQL = 10512;

}
