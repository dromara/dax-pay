package cn.daxpay.open.channel.umfpay.client.credential;

import lombok.Data;

/// # 联动优势 SDK 凭证(主应用侧)
///
/// 主应用从直连密钥配置([cn.daxpay.open.channel.umfpay.entity.direct.UmfpayDirectKeyConfig])提取
/// 联动优势商户号、商户私钥与平台公钥(均为 PEM/Base64 文本)后组装,
/// 经声明式 HTTP 客户端下发给子应用 dax-pay-channel-two 发起老网关表单调用。
/// 字段与子应用 cn.daxpay.open.channel.umfpay.config.UmfpaySdkCredential 保持一致(序列化互通)。
@Data
public class UmfpaySdkCredential {

    /// 联动优势商户号(mer_id)
    private String merId;

    /// 商户私钥(PEM 或 Base64 裸串, 兼容 PKCS1/PKCS8, 请求/应答签名用)
    private String merchantPrivateKey;

    /// 联动优势平台公钥(PEM 或 Base64 裸串, X509 格式, 响应/回调验签用)
    private String platformPublicKey;

    /// 是否沙箱环境(联动优势老网关无独立沙箱地址, 该标识仅透传记录)
    private Boolean sandbox;
}
