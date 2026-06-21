package cn.daxpay.open.platform.system.param.config.security;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 登录安全配置参数
///
@Data
@Accessors(chain = true)
@Schema(title = "登录安全配置参数")
public class PlatformLoginSecurityConfigParam {

    @Schema(description = "是否启用登录失败锁定")
    private Boolean lockoutEnabled;

    @Schema(description = "最大登录失败次数")
    private Integer maxFailedAttempts;

    @Schema(description = "锁定时长（分钟）")
    private Integer lockoutDurationMinutes;

    @Schema(description = "失败计数重置时长（分钟）")
    private Integer failureResetMinutes;

    @Schema(description = "是否启用验证码触发")
    private Boolean captchaEnabled;

    @Schema(description = "触发验证码的失败次数")
    private Integer captchaTriggerAttempts;

    @AssertTrue(message = "{validation.field.captchaFailCountNotGreaterThanMax.assertTrue}")
    @Schema(hidden = true)
    public boolean isCaptchaTriggerValid() {
        if (captchaTriggerAttempts == null || maxFailedAttempts == null) {
            return true;
        }
        return captchaTriggerAttempts <= maxFailedAttempts;
    }
}
