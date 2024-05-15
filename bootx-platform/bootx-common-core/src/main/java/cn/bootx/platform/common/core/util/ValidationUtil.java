package cn.bootx.platform.common.core.util;

import cn.bootx.platform.common.core.exception.ValidationFailedException;
import lombok.experimental.UtilityClass;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

/**
 * BeanValidation 工具类
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
     * 提取校验失败的消息
     */
    public String extractMessages(Set<ConstraintViolation<Object>> violations) {
        StringBuilder message = new StringBuilder();
        for (ConstraintViolation<Object> violation : violations) {
            message.append(violation.getMessage()).append(System.lineSeparator());
        }
        return message.toString();
    }

    /**
     * 验证参数对象，如果验证失败则返回所有失败信息
     */
    public String validate(Object paramObject, Class<?>... groups) {
        Validator validator = validatorFactory.getValidator();
        Set<ConstraintViolation<Object>> violations = validator.validate(paramObject, groups);

        StringBuilder message = new StringBuilder();
        for (ConstraintViolation<Object> violation : violations) {
            message.append(violation.getMessage()).append("\n");
        }

        return message.toString();
    }

}
