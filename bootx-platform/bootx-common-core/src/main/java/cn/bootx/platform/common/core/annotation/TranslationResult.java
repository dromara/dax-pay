package cn.bootx.platform.common.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 字典翻译标识注解, 1. 标注在字段上, 在翻译的时候会对这个字段进行递归翻译 2. 标注在方法上, 会对返回值进行翻译转换处理, 推荐只在 Controller 层配合
 * ResResult 使用, 其他场合使用 FieldTranslationService 进行手动处理
 *
 * @author xxm
 * @since 2022/12/15
 */
@Target({ ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface TranslationResult {

    /**
     * 是否启用
     */
    boolean enable() default true;

    /**
     * 翻译类型, 只可以用在方法上, 在字段上标注不发生效果
     */
    ConvertType convertType() default ConvertType.OBJECT;

    /**
     * 翻译类型
     */
    enum ConvertType {

        /**
         * 将目标对象转换成MAP, 限定只能使用在类似 ResResult 容器情况下, 可以处理翻译值前后类型不一致问题, 但会导致字段元信息的丢失,
         * 导致后续的处理出问题
         */
        MAP,
        /**
         * 不对目标对象的类型进行修改, 只对字典值进行翻译, 但遇到注解标注的字段出现字典项code与name类型不一致, 会进行忽略
         */
        OBJECT

    }

}
