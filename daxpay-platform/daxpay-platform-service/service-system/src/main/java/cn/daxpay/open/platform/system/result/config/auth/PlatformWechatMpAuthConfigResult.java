package cn.daxpay.open.platform.system.result.config.auth;

import cn.daxpay.open.platform.common.json.sensitive.SensitiveInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 平台微信公众号 H5 认证配置
///
/// 敏感字段使用 [@SensitiveInfo] 注解, 返回时由 Jackson 序列化器脱敏。
///
@Data
@Accessors(chain = true)
@Schema(title = "平台微信公众号 H5 认证配置")
public class PlatformWechatMpAuthConfigResult {

    /// 微信公众号 AppId
    @Schema(description = "微信公众号 AppId")
    private String appId;

    /// 微信公众号 AppSecret(脱敏返回)
    @SensitiveInfo
    @Schema(description = "微信公众号 AppSecret")
    private String appSecret;
}
