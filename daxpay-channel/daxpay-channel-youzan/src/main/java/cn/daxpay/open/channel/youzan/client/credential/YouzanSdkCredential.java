package cn.daxpay.open.channel.youzan.client.credential;

import lombok.Data;

/// # 有赞 SDK 凭证
///
/// 主应用从直连密钥配置(YouzanDirectKeyConfig)提取高汇通商户号、商户 RSA 私钥、
/// 有赞支付平台公钥与公钥唯一标识后组装, 下发给子应用发起有赞开放网关调用。
///
/// 仅承载通道调用所需的身份与密钥信息, 不含任何业务字段。
@Data
public class YouzanSdkCredential {

    /// 高汇通商户号(mch_id, 请求头 mchid 与 body mch_id 同值)
    private String merchantId;

    /// 商户 RSA 私钥(Base64/PEM, PKCS8 与 PKCS1 均兼容, 请求签名用)
    private String merchantPrivateKey;

    /// 有赞支付平台 RSA 公钥(Base64/PEM, X509, 响应/回调验签用)
    private String platformPublicKey;

    /// 公钥唯一标识(请求头 publickeyid, 与平台公钥成对下发)
    private String publicKeyId;

    /// 是否沙箱环境(高汇通无独立沙箱网关, 该标识仅透传记录)
    private Boolean sandbox;
}
