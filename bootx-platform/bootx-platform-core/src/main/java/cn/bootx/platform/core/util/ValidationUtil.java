package cn.bootx.platform.core.util;

import cn.bootx.platform.core.exception.ValidationFailedException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import lombok.experimental.UtilityClass;

import java.util.Set;

/**
 * BeanValidation 校验工具类
 *
 * @author xxm
 * @since 2020/5/26 18:14
 */
@UtilityClass
public class ValidationUtil {

    private static final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();

    /**
     * 验证参数对象，如果验证失败则抛出异常
     */
    public void validateParam(Object paramObject, Class<?>... groups) {
        Validator validator = validatorFactory.getValidator();
        Set<ConstraintViolation<Object>> violations = validator.validate(paramObject, groups);
        if (!violations.isEmpty()) {
            throw new ValidationFailedException(extractMessages(violations));
        }
    }

    /**
     * 验证参数对象，如果验证失败则返回所有失败信息
     */
    public String validate(Object paramObject, Class<?>... groups) {
        Validator validator = validatorFactory.getValidator();
        Set<ConstraintViolation<Object>> violations = validator.validate(paramObject, groups);
        return extractMessages(violations);
    }

    /**
     * 提取校验失败的消息
     */
    private String extractMessages(Set<ConstraintViolation<Object>> violations) {
        StringBuilder message = new StringBuilder();
        for (ConstraintViolation<Object> violation : violations) {
            message.append(violation.getMessage()).append(System.lineSeparator());
        }
        return message.toString();
    }
}
