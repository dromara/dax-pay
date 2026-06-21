package cn.daxpay.open.platform.capability.social.config.result;

import java.util.Map;

import cn.daxpay.open.platform.common.json.sensitive.SensitiveInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 第三方平台登录配置返回结果
///
@Data
@Accessors(chain = true)
@Schema(title = "第三方平台登录配置结果")
public class SocialConfigResult {

    @Schema(description = "主键")
    private Long id;

    @Schema(description = "平台编码")
    private String source;

    @Schema(description = "客户端ID")
    private String clientId;

    /// 客户端密钥(脱敏返回, 保留前后各4位)
    @SensitiveInfo(front = 4, end = 4)
    @Schema(description = "客户端密钥(脱敏)")
    private String clientSecret;

    @Schema(description = "回调地址")
    private String redirectUri;

    /// 平台特有配置(如企业微信 agentId)
    @Schema(description = "平台特有配置(如企业微信 agentId)")
    private Map<String, String> extra;

    @Schema(description = "是否已配置")
    private boolean configured;

    @Schema(description = "是否启用")
    private Boolean enabled;
}
