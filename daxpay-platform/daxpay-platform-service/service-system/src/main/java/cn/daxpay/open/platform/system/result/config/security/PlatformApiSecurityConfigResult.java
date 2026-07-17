package cn.daxpay.open.platform.system.result.config.security;

import cn.daxpay.open.platform.core.result.BaseResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # API安全配置结果
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "API安全配置结果")
public class PlatformApiSecurityConfigResult extends BaseResult {

    @Schema(description = "是否启用Nonce防重放校验")
    private Boolean nonceVerifyEnabled;

    @Schema(description = "是否启用请求时间窗口校验")
    private Boolean reqTimeoutEnabled;

    @Schema(description = "请求时间窗口容差（秒）")
    private Integer reqTimeoutSeconds;

    @Schema(description = "Nonce有效期（秒）")
    private Integer nonceTtlSeconds;
}
