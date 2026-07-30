package cn.daxpay.open.platform.system.result.config.auth;

import cn.daxpay.open.platform.common.json.sensitive.SensitiveInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 平台抖音开放平台 H5 应用认证配置
///
/// 敏感字段使用 [@SensitiveInfo] 注解, 返回时由 Jackson 序列化器脱敏。
///
@Data
@Accessors(chain = true)
@Schema(title = "平台抖音开放平台 H5 应用认证配置")
public class PlatformDouyinH5AuthConfigResult {

    /// 抖音开放平台 Client Key
    @Schema(description = "抖音开放平台 Client Key")
    private String clientKey;

    /// 抖音开放平台 Client Secret(脱敏返回)
    @SensitiveInfo(front = 6, end = 6)
    @Schema(description = "抖音开放平台 Client Secret")
    private String clientSecret;
}
