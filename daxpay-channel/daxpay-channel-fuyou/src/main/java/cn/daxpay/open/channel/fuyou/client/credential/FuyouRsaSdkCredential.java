package cn.daxpay.open.channel.fuyou.client.credential;

import lombok.Data;

/// # 富友 RSA SDK 凭证(主应用侧)
///
/// 主应用从 RSA 密钥配置(FuyouRsaKeyConfig)提取商户号 + 商户私钥 + 富友平台公钥后组装,
/// 经声明式 HTTP 客户端下发给子应用 dax-pay-channel-two, 发起「互联网直连」/「H5 聚合收银台」
/// (JSON + RSA 加密信封) API 调用。
///
/// 与子应用 [cn.daxpay.open.channel.fuyou.config.FuyouRsaCredential] 字段对称, 但属于主应用 client 包,
/// 独立序列化以避免主子应用包路径耦合。
@Data
public class FuyouRsaSdkCredential {

    /// 富友商户编号(mchnt_cd)
    private String merchantNo;

    /// 商户RSA私钥(Base64, PKCS1/PKCS8 均可, 响应/回调解密用)
    private String privateKey;

    /// 富友平台RSA公钥(Base64, X509, 请求加密用)
    private String publicKey;

    /// 是否沙箱环境
    private Boolean sandbox;
}
