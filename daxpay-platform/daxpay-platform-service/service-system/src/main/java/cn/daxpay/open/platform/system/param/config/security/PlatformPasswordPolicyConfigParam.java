package cn.daxpay.open.platform.system.param.config.security;

import cn.daxpay.open.platform.system.entity.config.platform.security.PlatformPasswordPolicyConfig;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.experimental.Accessors;
import java.util.Objects;

/// # 密码策略配置参数
///
@Data
@Accessors(chain = true)
@Schema(title = "密码策略配置参数")
public class PlatformPasswordPolicyConfigParam {

    @Schema(description = "是否启用密码强度验证")
    private Boolean enabled;

    @Schema(description = "最小长度")
    private Integer minLength;

    @Schema(description = "最大长度")
    private Integer maxLength;

    @Schema(description = "是否要求包含大写字母")
    private Boolean requireUppercase;

    @Schema(description = "是否要求包含小写字母")
    private Boolean requireLowercase;

    @Schema(description = "是否要求包含数字")
    private Boolean requireDigit;

    @Schema(description = "是否要求包含特殊字符")
    private Boolean requireSpecialChar;

    @Schema(description = "特殊字符集合")
    private String specialChars;

    @Schema(description = "密码轮换周期（天）")
    @Min(value = 0, message = "{validation.field.rotationDays.min}")
    @Max(value = PlatformPasswordPolicyConfig.MAX_ROTATION_DAYS, message = "{validation.field.rotationDays.max}")
    private Integer rotationDays;

    @Schema(description = "密码过期提醒天数（剩余天数不超过该值时提示即将过期）")
    @Min(value = 1, message = "{validation.field.expireWarnDays.min}")
    @Max(value = PlatformPasswordPolicyConfig.MAX_EXPIRE_WARN_DAYS, message = "{validation.field.expireWarnDays.max}")
    private Integer expireWarnDays;

    @Schema(description = "密码历史记录数量")
    private Integer historyCount;

    @AssertTrue(message = "{validation.field.minLengthNotGreaterThanMax.assertTrue}")
    @Schema(hidden = true)
    public boolean isLengthRangeValid() {
        if (Objects.isNull(minLength) || Objects.isNull(maxLength)) {
            return true;
        }
        return minLength <= maxLength;
    }
}
