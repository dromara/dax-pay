package cn.daxpay.open.channel.union.client.credential;

import lombok.Data;

/// # 云闪付 SDK 凭证
///
/// 与子应用 dax-pay-channel-one 的 `UnionSdkCredential` 镜像, 跨 HTTP 传输时字段对齐。
/// 主应用从密钥配置(union_key_config)提取商户号、签名类型与三证书后组装, 下发给子应用。
///
/// 银联 ACP 采用 RSA2 证书签名(私钥证书 PKCS12 + 中级/根证书), 三证书均为 Base64 字符串。
@Data
public class UnionSdkCredential {

    /// 银联商户号(merId)
    private String merId;

    /// 签名类型(银联 ACP 固定 RSA2)
    private String signType;

    /// 是否证书签名(银联 ACP 固定 true)
    private boolean certSign;

    /// 应用私钥证书(Base64 编码的 PKCS12 字符串)
    private String keyPrivateCert;

    /// 私钥证书密码
    private String keyPrivateCertPwd;

    /// 中级证书(Base64 编码的 X.509 DER)
    private String acpMiddleCert;

    /// 根证书(Base64 编码的 X.509 DER)
    private String acpRootCert;

    /// 是否沙箱环境
    private boolean sandbox;
}
