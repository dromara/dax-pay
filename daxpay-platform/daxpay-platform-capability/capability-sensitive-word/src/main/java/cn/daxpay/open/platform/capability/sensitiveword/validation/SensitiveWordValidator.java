package cn.daxpay.open.platform.capability.sensitiveword.validation;

import cn.daxpay.open.platform.capability.sensitiveword.enums.SensitiveWordSceneEnum;
import cn.daxpay.open.platform.capability.sensitiveword.service.SensitiveWordCheckService;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.hutool.core.util.StrUtil;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/// # 敏感词 Bean Validation
///
@Component
@RequiredArgsConstructor
public class SensitiveWordValidator implements ConstraintValidator<SensitiveWord, String> {

    private final SensitiveWordCheckService sensitiveWordCheckService;

    private boolean html;

    @Override
    public void initialize(SensitiveWord annotation) {
        this.html = annotation.html();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (StrUtil.isBlank(value)) {
            return true;
        }
        if (!sensitiveWordCheckService.isEnabled()) {
            return true;
        }
        // 走 assertClean 写命中审计；失败用注解默认 i18n message
        try {
            sensitiveWordCheckService.assertClean(value, SensitiveWordSceneEnum.GENERAL, html);
            return true;
        } catch (BizInfoException e) {
            // assertClean 以 BizInfoException 表达"命中敏感词";
            // 其余异常(DB 故障/NPE 等)不在此吞掉, 自然上抛为系统错误, 避免基础设施故障被误报成敏感词命中
            return false;
        }
    }
}

