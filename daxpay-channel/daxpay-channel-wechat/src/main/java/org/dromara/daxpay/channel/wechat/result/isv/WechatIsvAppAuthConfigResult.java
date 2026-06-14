package org.dromara.daxpay.channel.wechat.result.isv;

import org.dromara.daxpay.platform.common.json.sensitive.SensitiveInfo;
import org.dromara.daxpay.platform.core.result.BaseResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 微信服务商应用授权认证配置
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "微信服务商应用授权认证配置")
public class WechatIsvAppAuthConfigResult extends BaseResult {

    @Schema(description = "微信服务商应用ID")
    private Long appId;

    @SensitiveInfo(front = 12, end = 12)
    @Schema(description = "应用密钥(加密存储)")
    private String appSecret;

    @Schema(description = "是否已配置应用密钥")
    private Boolean appSecretConfigured;

    @Schema(description = "授权回调地址（仅公众号）")
    private String authCallbackUrl;
}
