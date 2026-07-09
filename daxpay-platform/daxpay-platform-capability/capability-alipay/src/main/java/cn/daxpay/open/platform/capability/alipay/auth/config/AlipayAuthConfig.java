package cn.daxpay.open.platform.capability.alipay.auth.config;

import lombok.Data;
import lombok.experimental.Accessors;

/// # 支付宝开放平台认证配置
///
/// 纯 POJO, 不耦合配置存储。由调用方(iam 模块的授权登录端点 / payment 模块的通道认证策略)
/// 从各自的配置来源(平台级加密配置 `EncryptPlatformConfigTypeEnum.ALIPAY_AUTH`)装载后传入。
/// 字段与 `PlatformAlipayAuthConfig`(service-system) 一一对应, 调用方做一次拷贝即可。
///
@Data
@Accessors(chain = true)
public class AlipayAuthConfig {

    /// 支付宝开放平台应用 appId
    private String appId;

    /// 鉴权方式: public_key(公钥) / cert(证书)
    /// @see AlipayAuthTypeEnum
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
}
