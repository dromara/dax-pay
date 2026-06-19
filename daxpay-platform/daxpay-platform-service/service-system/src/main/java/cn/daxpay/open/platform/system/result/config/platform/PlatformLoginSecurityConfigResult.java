package cn.daxpay.open.platform.system.result.config.platform;

import cn.daxpay.open.platform.core.result.BaseResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 登录安全配置结果
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "登录安全配置结果")
public class PlatformLoginSecurityConfigResult extends BaseResult {

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
}
