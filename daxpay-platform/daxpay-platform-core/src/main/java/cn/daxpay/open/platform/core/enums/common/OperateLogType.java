package cn.daxpay.open.platform.core.enums.common;

import cn.daxpay.open.platform.core.i18n.I18nSupport;

import java.util.Locale;

/// # 操作日志业务类型枚举
///
/// 实现 [I18nSupport], 翻译 key 前缀 `enum.operate_log_type`, 完整 key = 前缀 + "." + code
public enum OperateLogType implements I18nSupport {

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

    /// 枚举编码, 与操作日志入库的 businessType 值一致（name 小写）
    @Override
    public String getCode() {
        return name().toLowerCase(Locale.ROOT);
    }

    /// 翻译 key 前缀, 对应资源文件 enum/operate_log_type.json
    @Override
    public String getI18nPrefix() {
        return "enum.operate_log_type";
    }
}
