package cn.daxpay.open.platform.capability.social.justauth.model;

import lombok.Data;
import lombok.experimental.Accessors;

/// # 授权令牌
///
/// 各平台 OAuth2 授权后获取的凭证信息, 当前场景仅需登录身份, 不含刷新/撤销等扩展字段
///
@Data
@Accessors(chain = true)
public class AuthToken {

    /// 访问令牌
    private String accessToken;

    /// 过期时间(秒)
    private int expireIn;

    /// 刷新令牌
    private String refreshToken;

    /// 平台用户唯一标识(如微信 openid)
    private String openId;

    /// 联合标识(如微信 unionId, 钉钉 unionId)
    private String unionId;

    /// 企业微信授权码(用于换取用户信息)
    private String code;

    /// 作用域
    private String scope;

    /// 令牌类型
    private String tokenType;
}
