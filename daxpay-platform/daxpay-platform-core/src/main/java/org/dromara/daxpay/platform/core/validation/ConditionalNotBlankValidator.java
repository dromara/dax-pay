package org.dromara.daxpay.platform.core.validation;

import cn.hutool.core.util.StrUtil;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;

/// # 条件非空校验器：当指定字段的值等于期望值时，目标字段必须非空
///
public class ConditionalNotBlankValidator implements ConstraintValidator<ConditionalNotBlank, Object> {

    private String target;
    private String field;
    private String fieldValue;
    private String message;

    @Override
    public void initialize(ConditionalNotBlank annotation) {
        this.target = annotation.target();
        this.field = annotation.field();
        this.fieldValue = annotation.fieldValue();
        this.message = annotation.message();
    }

    @Override
    public boolean isValid(Object bean, ConstraintValidatorContext context) {
        if (bean == null) {
            return true;
        }
        try {
            Field conditionField = bean.getClass().getDeclaredField(field);
            conditionField.setAccessible(true);
            Object conditionValue = conditionField.get(bean);
            if (!fieldValue.equals(conditionValue)) {
                return true;
            }
            Field targetField = bean.getClass().getDeclaredField(target);
            targetField.setAccessible(true);
            Object targetValue = targetField.get(bean);
            if (targetValue == null || (targetValue instanceof String && StrUtil.isBlank((String) targetValue))) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(message)
                        .addPropertyNode(target)
                        .addConstraintViolation();
                return false;
            }
            return true;
        } catch (NoSuchFieldException | IllegalAccessException e) {
            return true;
        }
    }
}