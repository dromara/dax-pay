package cn.daxpay.open.platform.core.enums.common;

/// # 操作日志业务类型枚举
///
public enum OperateLogType {

    /// 其它
    OTHER("其它"),

    /// 新增
    ADD("新增"),

    /// 修改
    UPDATE("修改"),

    /// 删除
    DELETE("删除"),

    /// 授权
    GRANT("授权"),

    /// 同步
    SYNC("同步"),

    /// 导出
    EXPORT("导出"),

    /// 导入
    IMPORT("导入"),

    /// 强退
    FORCE("强退"),

    /// 清空数据
    CLEAN("清空");

    /// 描述
    private final String description;

    OperateLogType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
