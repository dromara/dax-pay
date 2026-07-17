package cn.daxpay.open.platform.system.result.config.security;

import cn.daxpay.open.platform.core.result.BaseResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # IAM域防重放配置结果
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "IAM域防重放配置结果")
public class PlatformIamReplayProtectConfigResult extends BaseResult {

    @Schema(description = "是否启用防重放校验")
    private Boolean enabled;

    @Schema(description = "Nonce有效期（秒）")
    private Integer nonceTimeoutSeconds;

    @Schema(description = "时间戳允许偏差（秒）")
    private Integer timestampToleranceSeconds;
}
