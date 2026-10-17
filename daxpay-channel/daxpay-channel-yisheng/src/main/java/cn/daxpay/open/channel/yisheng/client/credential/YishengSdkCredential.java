package cn.daxpay.open.channel.yisheng.client.credential;

import lombok.Data;

/// # 易生 SDK 凭证
///
/// 主应用从直连密钥配置(YishengDirectKeyConfig)提取易生合作伙伴ID(partner/reqId)、
/// 商户编号(merchant_id)与 RSA 密钥对(商户私钥签名用 / 易生平台公钥验签用)后组装,
/// 下发给子应用发起易生统一网关调用。
///
/// 仅承载通道调用所需的身份与密钥信息, 不含任何业务字段。
@Data
public class YishengSdkCredential {

    /// 合作伙伴ID(reqId, 网关公共参数 partner)
    private String partner;

    /// 易生商户编号(merchant_id, 业务参数)
    private String merchantId;

    /// 商户 RSA 私钥(Base64/PEM, PKCS8 与 PKCS1 均兼容, 请求签名用)
    private String merchantPrivateKey;

    /// 易生平台 RSA 公钥(Base64/PEM, X509, 响应/回调验签用)
    private String platformPublicKey;

    /// 是否沙箱环境(易生网关生产与沙箱同址, 该标识仅透传记录)
    private Boolean sandbox;
}
