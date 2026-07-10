package cn.daxpay.open.platform.system.param.config.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 平台微信公众号 H5 认证配置参数
///
/// 敏感字段(appSecret) 编辑时未修改由前端不传字段(undefined) + 默认 IGNORE 策略跳过更新,
/// 详见 [PlatformWechatMpAuthConfigService#updateWechatMpAuthConfig]。
///
@Data
@Accessors(chain = true)
@Schema(title = "平台微信公众号 H5 认证配置参数")
public class PlatformWechatMpAuthConfigParam {

    /// 微信公众号 AppId
    @NotBlank(message = "{validation.field.appId.notBlank}")
    @Schema(description = "微信公众号 AppId")
    private String appId;

    /// 微信公众号 AppSecret
    @Schema(description = "微信公众号 AppSecret")
    private String appSecret;
}
