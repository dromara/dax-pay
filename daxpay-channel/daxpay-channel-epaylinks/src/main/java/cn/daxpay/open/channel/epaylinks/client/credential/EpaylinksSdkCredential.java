package cn.daxpay.open.channel.epaylinks.client.credential;

import lombok.Data;

/// # 易票联 SDK 凭证(主应用侧)
///
/// 从直连密钥配置(EpaylinksDirectKeyConfig)提取易票联商户编号、证书序列号、
/// 商户私钥与平台公钥证书(均为文本, 兼容 PEM/Base64)后组装, 下发给子应用发起 EFPS 网关调用。
///
/// 仅承载通道调用所需的身份与密钥信息, 不含任何业务字段。
@Data
public class EpaylinksSdkCredential {

    /// 易票联商户编号(customerCode)
    private String customerCode;

    /// 证书序列号(sign_no, 与商户私钥证书配套)
    private String signNo;

    /// 商户私钥(PEM/Base64, 兼容 PKCS1/PKCS8, 请求签名用)
    private String merchantPrivateKey;

    /// 易票联平台公钥证书(efps.cer, PEM/Base64/DER, 响应/回调验签用)
    private String platformCert;

    /// 上游商户号(channelMchtNo, 可空; 设置后下单上送指定受理商户)
    private String channelMchtNo;

    /// 是否沙箱环境(易票联无独立沙箱网关, 该标识仅透传记录)
    private Boolean sandbox;
}
