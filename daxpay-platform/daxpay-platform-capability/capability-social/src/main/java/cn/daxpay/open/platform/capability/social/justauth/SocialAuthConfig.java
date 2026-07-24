package cn.daxpay.open.platform.capability.social.justauth;

import lombok.Data;
import lombok.experimental.Accessors;

/// # 社交登录授权配置
///
/// 对应单个第三方平台的配置, 由 iam_social_config 表加载得到
///
@Data
@Accessors(chain = true)
public class SocialAuthConfig {

    /// 客户端ID(appId / corpid)
    private String clientId;

    /// 客户端密钥(appSecret / corpsecret)
    private String clientSecret;

    /// 回调地址
    private String redirectUri;

    /// 企业微信: 网页应用ID(agentId)
    private String agentId;

    /// QQ: 是否申请 unionId
    private boolean unionId;

    /// 静默/应用内授权模式
    /// 企业微信: true 走网页授权(oauth2/authorize), false 走扫码(qrConnect)
    /// 微信公众号: true 使用 snsapi_base, false 使用 snsapi_userinfo
    private boolean silent;
}
