package cn.daxpay.open.platform.system.entity.config.platform;

import lombok.Data;
import lombok.experimental.Accessors;

/// # 平台支付宝开放平台认证配置
///
/// 存储支付宝 OAuth 所需凭据, 用于授权登录(iam 模块)与通道认证(payment 模块)共用。
/// 通过 [cn.daxpay.open.platform.system.enums.EncryptPlatformConfigTypeEnum#ALIPAY_AUTH]
/// 以 AES-256-GCM 加密 JSON 存储。
///
@Data
@Accessors(chain = true)
public class PlatformAlipayAuthConfig {

    /// 支付宝开放平台应用 appId
    private String appId;

    /// 鉴权方式: public_key(公钥模式) / cert(证书模式)
    /// @see cn.daxpay.open.platform.capability.alipay.auth.config.AlipayAuthTypeEnum
    private String authType;

    /// 应用私钥(公钥模式与证书模式都需要)
    private String privateKey;

    /// 支付宝公钥(公钥模式使用)
    private String alipayPublicKey;

    /// 应用公钥证书内容(证书模式使用)
    private String appCert;

    /// 支付宝公钥证书内容(证书模式使用)
    private String alipayCert;

    /// 支付宝根证书内容(证书模式使用)
    private String alipayRootCert;

    /// 是否沙箱环境
    private Boolean sandbox;
}
