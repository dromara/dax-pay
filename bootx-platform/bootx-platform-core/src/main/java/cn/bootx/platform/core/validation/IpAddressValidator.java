package cn.bootx.platform.core.validation;

import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.StrUtil;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * ip地址校验器
 * @author xxm
 * @since 2024/6/11
 */
public class IpAddressValidator implements ConstraintValidator<IpAddress, String> {

    /**
     * 校验是否为IP地址。成功返回true，失败返回false
     */
    @Override
    public boolean isValid(String str, ConstraintValidatorContext constraintValidatorContext) {
        if (StrUtil.isNotBlank(str)){
            return Validator.isIpv4(str) || Validator.isIpv6(str);
        }
        return true;
    }
}
