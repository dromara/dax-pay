package cn.daxpay.open.channel.sand.client.credential;

import lombok.Data;

/// # 杉德支付 SDK 凭证(主应用侧)
///
/// 主应用从直连密钥配置([cn.daxpay.open.channel.sand.entity.direct.SandDirectKeyConfig])提取
/// 杉德商户编号(mid)、市场产品编码、商户 pfx 私钥证书与杉德公钥证书(Base64)后组装,
/// 经声明式 HTTP 客户端下发给子应用 dax-pay-channel-two 发起杉德 v4 开放平台调用。
/// 字段与子应用 cn.daxpay.open.channel.sand.config.SandSdkCredential 保持一致(序列化互通)。
@Data
public class SandSdkCredential {

    /// 杉德商户编号(mid, 直连商户身份)
    private String mid;

    /// 市场产品编码(QZF 标准线上收款 / CSDB 企业杉德宝)
    private String marketProduct;

    /// 商户私钥证书 pfx 内容(Base64, PKCS12 格式, 签名与响应 encryptKey 解密用)
    private String merchantCertPfx;

    /// 商户私钥证书密码
    private String merchantCertPassword;

    /// 杉德平台公钥证书 cer 内容(Base64 或 PEM, 请求 encryptKey 加密与响应/回调验签用)
    private String sandCert;

    /// 是否沙箱环境(true 走 UAT 网关)
    private Boolean sandbox;
}
