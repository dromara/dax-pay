package cn.daxpay.open.platform.system.param.config.security;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

/// # IAM域防重放配置参数
///
@Data
@Accessors(chain = true)
@Schema(title = "IAM域防重放配置参数")
public class PlatformIamReplayProtectConfigParam {

    @Schema(description = "是否启用防重放校验")
    @NotNull(message = "{validation.field.replayProtectEnabled.notNull}")
    private Boolean enabled;

    @Schema(description = "Nonce有效期（秒）")
    @Min(value = 1, message = "{validation.field.nonceTimeoutSeconds.min}")
    private Integer nonceTimeoutSeconds;

    @Schema(description = "时间戳允许偏差（秒）")
    @Min(value = 1, message = "{validation.field.timestampToleranceSeconds.min}")
    private Integer timestampToleranceSeconds;
}
