package org.dromara.daxpay.platform.system.result.config.platform;

import org.dromara.daxpay.platform.core.result.BaseResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 双因素认证配置结果
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "双因素认证配置结果")
public class PlatformTwoFactorAuthConfigResult extends BaseResult {

    @Schema(description = "是否启用双因素认证")
    private Boolean enabled;

    @Schema(description = "TOTP算法类型")
    private String algorithm;

    @Schema(description = "TOTP时间步长（秒）")
    private Integer timeStep;

    @Schema(description = "TOTP验证码长度")
    private Integer codeLength;

    @Schema(description = "允许的时间窗口偏移")
    private Integer timeWindowOffset;

    @Schema(description = "发行者名称")
    private String issuer;

    @Schema(description = "备用验证码数量")
    private Integer backupCodesCount;

    @Schema(description = "验证码有效期（分钟）")
    private Integer codeValidityMinutes;
}
