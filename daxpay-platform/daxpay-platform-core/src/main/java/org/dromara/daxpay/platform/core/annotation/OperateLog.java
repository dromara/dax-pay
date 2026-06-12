package org.dromara.daxpay.platform.core.annotation;

import org.dromara.daxpay.platform.core.enums.common.OperateLogType;
import java.lang.annotation.*;

/// # 操作日志注解(支持重复注解)
///
@Target({ ElementType.PARAMETER, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Repeatable(value = OperateLogs.class)
@Inherited
public @interface OperateLog {

    /// 模块
    String title() default "";

    /// 业务操作类型
    OperateLogType businessType() default OperateLogType.OTHER;

    /// 是否保存请求参数
    boolean saveParam() default false;

    /// 是否保存返回参数
    boolean saverReturn() default false;

    /// 是否对请求参数进行脱敏
    /// 当 saveParam=true 时生效
    boolean maskParam() default false;

    /// 是否对返回参数进行脱敏
    /// 当 saverReturn=true 时生效
    boolean maskReturn() default false;

    /// 全量脱敏键名单，大小写不敏感
    /// 命中的键值将被替换为 ******
    /// 如果未配置任何脱敏规则（fullMaskKeys 和 partialMaskRules 都为空），使用系统默认敏感键列表
    /// 如果配置了，使用配置的规则（不与默认列表合并）
    String[] fullMaskKeys() default {};

    /// 部分脱敏规则，命中后保留前后 N/M 位
    /// 优先级高于全量脱敏
    /// 如果未配置任何脱敏规则（fullMaskKeys 和 partialMaskRules 都为空），使用系统默认敏感键列表
    /// 如果配置了，使用配置的规则（不与默认列表合并）
    PartialMaskRule[] partialMaskRules() default {};

    /// 请求参数/返回值的最大长度
    /// 超过此长度将被截断
    /// 单位：字符数
    int payloadMaxLength() default 20000;

}

