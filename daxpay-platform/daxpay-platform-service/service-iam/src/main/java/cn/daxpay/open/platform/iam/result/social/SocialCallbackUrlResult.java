package cn.daxpay.open.platform.iam.result.social;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 社交 OAuth 应登记的回调地址
///
/// 供运营在第三方开放平台配置 redirect_uri 白名单时复制.
///
@Data
@Accessors(chain = true)
@Schema(title = "社交回调地址")
public class SocialCallbackUrlResult {

    /// 身份域终端 admin / merchant
    @Schema(description = "终端编码")
    private String clientCode;

    /// 平台编码(source)
    @Schema(description = "平台编码")
    private String source;

    /// 场景: LOGIN / BIND
    @Schema(description = "场景 LOGIN/BIND")
    private String mode;

    /// 完整回调 URL(含 /{source})
    @Schema(description = "完整回调地址")
    private String url;

    /// 对应端 baseUrl 是否已配置
    @Schema(description = "端点 baseUrl 是否已配置")
    private boolean baseUrlConfigured;
}
