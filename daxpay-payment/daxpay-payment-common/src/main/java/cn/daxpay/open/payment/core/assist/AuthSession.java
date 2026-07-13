package cn.daxpay.open.payment.core.assist;

import lombok.Data;
import lombok.experimental.Accessors;

/// # 认证会话上下文
///
/// H5授权重定向场景下, 生成授权链接时将认证所需上下文序列化保存到Redis(以 authToken 为key),
/// 授权回调后凭 authToken 恢复, 供认证策略定位通道应用并完成 code 换 openId/userId。
/// 与 [ChannelAuthService] 的 queryCode 机制(付款码/道通场景)解耦, 独立 key 前缀管理。
@Data
@Accessors(chain = true)
public class AuthSession {

    /// 认证来源: 平台级支付宝配置(系统支付宝配置调试场景)
    ///
    /// 该标记表示本次认证使用平台级 `PlatformAlipayAuthConfig` 用授权码换 openId,
    /// 而非商户级支付产品策略。由 [PlatformAuthService] 在 generateAlipayAuthUrl 时写入,
    /// 认证分发层据此走平台级支付宝授权回调分支([PlatformAuthServiceauthAlipay])。
    public static final String SOURCE_PLATFORM_ALIPAY = "platform_alipay";

    /// 认证来源: 平台级微信公众号配置(系统公众号配置调试场景)
    ///
    /// 该标记表示本次认证使用平台级 `PlatformWechatMpAuthConfig`(appId/appSecret) 用授权码换 openId,
    /// 而非商户级支付产品策略。由 [PlatformAuthService] 在 generateWechatMpAuthUrl 时写入,
    /// 认证分发层据此走平台级微信授权回调分支([PlatformAuthService.authWechatMp])。
    public static final String SOURCE_PLATFORM_MP = "platform_mp";

    /// 认证来源: 平台级抖音 H5 应用配置(抖音支付调试场景)
    ///
    /// 该标记表示本次认证使用平台级 `PlatformDouyinH5AuthConfig`(clientKey/clientSecret) 用授权码换 openId,
    /// 通过抖音开放平台 silent_auth 静默授权获取 openId。由 [PlatformAuthService] 在
    /// generateDouyinAuthUrl 时写入, 认证分发层据此走平台级抖音授权回调分支([PlatformAuthService.authDouyin])。
    public static final String SOURCE_PLATFORM_DOUYIN = "platform_douyin";

    /// 认证来源
    ///
    /// 标识本次认证的配置来源:
    /// - [#SOURCE_PLATFORM_ALIPAY]: 平台级支付宝配置(调试场景)
    /// - [#SOURCE_PLATFORM_MP]: 平台级微信公众号配置(调试场景)
    /// - [#SOURCE_PLATFORM_DOUYIN]: 平台级抖音 H5 应用配置(调试场景)
    /// - null/空: 走商户级支付产品策略(直连/服务商)
    private String source;

    /// 支付产品编码
    /// @see cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum
    private String product;

    /// 通道商户号
    private String channelMchNo;

    /// 支付能力编码(用于解析具体应用)
    /// @see cn.daxpay.open.platform.core.enums.pay.channel.PayCapabilityEnum
    private String capability;

    /// 指定认证应用AppId(可选, 优先级高于配置自动解析)
    private String opAppId;

    /// 来源回跳路径(授权完成后前端回跳的目标路径)
    private String returnPath;

    /// 查询码(调试轮询用)
    ///
    /// OAuth 重定向场景下, 回调 URL 仅含 authToken(第三方不透传 queryCode),
    /// 故将 queryCode 随会话保存, auth 时从会话恢复并据此把结果写回轮询 redis。
    private String queryCode;
}
