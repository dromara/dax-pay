package cn.bootx.platform.core.annotation;

import java.lang.annotation.*;

/**
 * 操作日志注解(支持重复注解)
 *
 * @author xxm
 * @since 2021/8/13
 */
@Target({ ElementType.PARAMETER, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Repeatable(value = OperateLogs.class)
@Inherited
public @interface OperateLog {

    /**
     * 模块
     */
    String title() default "";

    /**
     * 业务操作类型
     */
    BusinessType businessType() default BusinessType.OTHER;

    /**
     * 是否保存请求参数
     */
    boolean saveParam() default false;

    /**
     * 是否保存返回参数
     */
    boolean saverReturn() default false;

    /**
     * 业务操作类型
     * 字典: log_business_type
     */
    enum BusinessType {

        /**
         * 其它
         */
        OTHER,

        /**
         * 新增
         */
        ADD,

        /**
         * 修改
         */
        UPDATE,

        /**
         * 删除
         */
        DELETE,

        /**
         * 授权
         */
        GRANT,

        /**
         * 导出
         */
        EXPORT,

        /**
         * 导入
         */
        IMPORT,

        /**
         * 强退
         */
        FORCE,

        /**
         * 清空数据
         */
        CLEAN,

    }

}
