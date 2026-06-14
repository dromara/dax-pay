package org.dromara.daxpay.channel.alipay.code;

/// # 支付宝支付参数常量
///
/// 定义支付宝支付相关的常量，包括认证类型(公钥/证书)和用户标识类型(openid/userid)等枚举值。
///
public interface AlipayCode {

    /// # 认证类型
    ///
    interface AuthType {
        /// 公钥模式
        String AUTH_TYPE_KEY = "public_key";

        /// 证书模式
        String AUTH_TYPE_CERT = "cert";
    }

    /// # 用户标识类型
    ///
    interface UserIdType {
        /// OpenId
        String OPENID = "openid";

        /// UserId
        String USERID = "userid";

        /// 同时支持 OpenId 和 UserId
        String OPENID_USERID = "openid_userid";
    }
}
