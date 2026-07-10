package cn.daxpay.open.platform.system.param.config.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 平台抖音开放平台 H5 应用认证配置参数
///
/// 敏感字段(clientSecret) 编辑时未修改由前端不传字段(undefined) + 默认 IGNORE 策略跳过更新,
/// 详见 [PlatformDouyinH5AuthConfigService#updateDouyinH5AuthConfig]。
///
@Data
@Accessors(chain = true)
@Schema(title = "平台抖音开放平台 H5 应用认证配置参数")
public class PlatformDouyinH5AuthConfigParam {

    /// 抖音开放平台 Client Key
    @NotBlank(message = "{validation.field.clientKey.notBlank}")
    @Schema(description = "抖音开放平台 Client Key")
    private String clientKey;

    /// 抖音开放平台 Client Secret
    @Schema(description = "抖音开放平台 Client Secret")
    private String clientSecret;
}
