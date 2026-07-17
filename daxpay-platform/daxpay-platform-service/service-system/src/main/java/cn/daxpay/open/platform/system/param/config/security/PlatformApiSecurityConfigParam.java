package cn.daxpay.open.platform.system.param.config.security;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

/// # API安全配置参数
///
@Data
@Accessors(chain = true)
@Schema(title = "API安全配置参数")
public class PlatformApiSecurityConfigParam {

    @Schema(description = "是否启用Nonce防重放校验")
    @NotNull(message = "{validation.field.nonceVerifyEnabled.notNull}")
    private Boolean nonceVerifyEnabled;

    @Schema(description = "是否启用请求时间窗口校验")
    @NotNull(message = "{validation.field.reqTimeoutEnabled.notNull}")
    private Boolean reqTimeoutEnabled;

    @Schema(description = "请求时间窗口容差（秒）")
    @Min(value = 1, message = "{validation.field.reqTimeoutSeconds.min}")
    private Integer reqTimeoutSeconds;

    @Schema(description = "Nonce有效期（秒）")
    @Min(value = 1, message = "{validation.field.nonceTtlSeconds.min}")
    private Integer nonceTtlSeconds;
}
