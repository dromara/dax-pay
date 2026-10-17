package cn.daxpay.open.channel.kuaiqian.client.credential;

import lombok.Data;

/// # 快钱支付 SDK 凭证(主应用侧)
///
/// 主应用从直连密钥配置([cn.daxpay.open.channel.kuaiqian.entity.direct.KuaiqianDirectKeyConfig])提取
/// 快钱会员号、当面付商户/终端号与三份证书(Base64)后组装,
/// 经声明式 HTTP 客户端下发给子应用 dax-pay-channel-two 发起快钱 MAS 网关调用。
/// 字段与子应用 cn.daxpay.open.channel.kuaiqian.config.KuaiqianSdkCredential 保持一致(序列化互通)。
@Data
public class KuaiqianSdkCredential {

    /// 快钱会员号/账户号(memberCode, 人民币网关商户账号 = memberCode + "01")
    private String memberCode;

    /// 服务商快钱子账户号(可空; 设置后 head.memberCode=子账户号, head.vendorMemberCode=会员号)
    private String vendorMemberCode;

    /// 当面付商户号(merchantId)
    private String merchantId;

    /// 当面付终端号(terminalId)
    private String terminalId;

    /// 商户证书 pfx 内容(Base64, PKCS12 格式, 签名与响应解密用)
    private String merchantCertPfx;

    /// 商户证书密码
    private String merchantCertPassword;

    /// 快钱平台证书 cer 内容(Base64, 报文加密与验签用)
    private String platformCert;

    /// SSL 客户端证书 p12 内容(Base64, MAS 网关双向认证用)
    private String sslCertP12;

    /// SSL 客户端证书密码
    private String sslCertPassword;

    /// 是否沙箱环境(快钱 MAS 无独立沙箱网关, 该标识仅透传记录)
    private Boolean sandbox;
}
