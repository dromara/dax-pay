package cn.daxpay.open.payment.unipay.result.assist;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 获取OpenId授权链接和查询标识返回类
///
@Data
@Accessors(chain = true)
@Schema(title = "授权链接和查询标识返回类")
public class AuthUrlResult {

    /// 授权访问链接
    @Schema(description = "授权访问链接")
    private String authUrl;

    /// 查询标识码, 用于查询是否获取到了OpenId
    @Schema(description = "查询标识码")
    private String queryCode;

    /// 认证会话码(OPEN 场景需据此更新 session 中的 scene/redirect_url)
    ///
    /// 由 MerchantChannelAuthService / PlatformAuthProvider 在创建 session 后回填,
    /// 供 OPEN 场景(OpenAuthService)加载并更新会话上下文。
    @Schema(description = "认证会话码")
    private String authToken;

}
