package cn.daxpay.open.platform.system.param.config;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;
import lombok.Data;
import lombok.experimental.Accessors;

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
    private Integer rotationDays;

    @Schema(description = "密码历史记录数量")
    private Integer historyCount;

    @AssertTrue(message = "{validation.field.minLengthNotGreaterThanMax.assertTrue}")
    @Schema(hidden = true)
    public boolean isLengthRangeValid() {
        if (minLength == null || maxLength == null) {
            return true;
        }
        return minLength <= maxLength;
    }
}
