package cn.daxpay.open.channel.yeepay.client.credential;

import lombok.Data;

/// # 易宝支付 SDK 凭证(主应用侧)
///
/// 主应用从直连密钥配置([cn.daxpay.open.channel.yeepay.entity.direct.YeepayDirectKeyConfig])提取
/// 商户身份与密钥后组装, 经声明式 HTTP 客户端下发给子应用 dax-pay-channel-two 发起易宝 YOP API 调用。
/// 字段与子应用 cn.daxpay.open.channel.yeepay.config.YeepaySdkCredential 保持一致(序列化互通)。
@Data
public class YeepaySdkCredential {

    /// 易宝商户号(merchantNo)
    private String merchantNo;

    /// 易宝服务商商编(parentMerchantNo / yopIsvNo)
    private String yopIsvNo;

    /// 通道应用 AppKey(YOP 应用标识)
    private String appKey;

    /// 商户 RSA 私钥(PEM 格式 PKCS#8 字符串, SDK 签名用)
    private String privateKey;

    /// 易宝平台 RSA 公钥(PEM 格式, SDK 验签用)
    private String yopPublicKey;

    /// 微信 AppId(微信 H5/JSAPI 场景用, 可空)
    private String wxAppId;

    /// 微信 AppSecret(微信场景用, 可空)
    private String wxAppSecret;

    /// 是否沙箱环境
    private Boolean sandbox;
}
