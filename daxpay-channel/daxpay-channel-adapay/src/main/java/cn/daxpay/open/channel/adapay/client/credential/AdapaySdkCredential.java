package cn.daxpay.open.channel.adapay.client.credential;

import lombok.Data;

/// # 汇付天下 SDK 凭证(主应用侧)
///
/// 与子应用 dax-pay-channel-two 的 `cn.daxpay.open.channel.adapay.config.AdapaySdkCredential` 镜像,
/// 跨 HTTP 传输时字段对齐。
/// 主应用从直连配置实体(AdapayDirectKeyConfig)提取后组装, 下发给子应用发起汇付 API 调用。
@Data
public class AdapaySdkCredential {

    /// 汇付支付应用 ID(app_id)
    private String adapayAppId;

    /// 汇付 API Key(请求头 Authorization)
    private String apiKey;

    /// 商户 RSA 私钥(PKCS#8 Base64, 请求签名)
    private String privateKey;

    /// 汇付平台公钥(X509 Base64, 响应验签; 为空时子应用用全局默认公钥)
    private String publicKey;

    /// 是否沙箱环境
    private Boolean sandbox;
}
