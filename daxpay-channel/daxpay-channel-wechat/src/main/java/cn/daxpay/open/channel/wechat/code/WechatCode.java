package cn.daxpay.open.channel.wechat.code;

/// # 微信支付常量
///
/// 定义微信支付相关的常量，包括认证方式(仅API V3证书模式)和用户标识类型等。
///
public interface WechatCode {

    /// # 认证方式
    ///
    interface AuthType {
        /// API V3 证书模式
        String AUTH_TYPE_CERT = "cert";
    }

    /// # 用户标识类型
    ///
    interface UserIdType {
        /// 用户标识(OpenId)
        String OPENID = "openid";
    }
}
