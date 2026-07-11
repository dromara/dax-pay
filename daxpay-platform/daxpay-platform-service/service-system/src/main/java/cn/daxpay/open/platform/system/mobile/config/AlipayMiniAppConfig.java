package cn.daxpay.open.platform.system.mobile.config;

import lombok.Data;
import lombok.experimental.Accessors;

/// # 支付宝小程序应用配置(落库 JSON 形状)
///
/// 序列化后写入 [cn.daxpay.open.platform.system.entity.mobile.MobileApp#appConfig] 并加密存储。
/// authType 与平台支付宝认证一致: public_key / cert。
@Data
@Accessors(chain = true)
public class AlipayMiniAppConfig {

    /// 小程序 AppId
    private String appId;

    /// 鉴权方式: public_key(公钥) / cert(证书)
    private String authType;

    /// 应用私钥
    private String privateKey;

    /// 支付宝公钥(公钥模式)
    private String alipayPublicKey;

    /// 应用公钥证书(证书模式)
    private String appCert;

    /// 支付宝公钥证书(证书模式)
    private String alipayCert;

    /// 支付宝根证书(证书模式)
    private String alipayRootCert;
}
