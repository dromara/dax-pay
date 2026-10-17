package cn.daxpay.open.channel.baofu.client.credential;

import lombok.Data;

/// # 宝付支付 SDK 凭证(主应用侧)
///
/// 主应用从直连密钥配置([cn.daxpay.open.channel.baofu.entity.direct.BaofuDirectKeyConfig])提取
/// 宝付商户号(merId)、终端号(terId)、聚合交易商户号(subMchId)与两份证书(Base64)后组装,
/// 经声明式 HTTP 客户端下发给子应用 dax-pay-channel-two 发起宝付聚合网关调用。
/// 字段与子应用 cn.daxpay.open.channel.baofu.config.BaofuSdkCredential 保持一致(序列化互通)。
@Data
public class BaofuSdkCredential {

    /// 宝付商户号(merId, 收款线)
    private String merId;

    /// 宝付终端号(terId, 收款线)
    private String terId;

    /// 聚合交易商户号(subMchId, 在微信/支付宝报备的商户号, 可空; 聚合收银台场景上送)
    private String subMchId;

    /// 商户证书 pfx 内容(Base64, PKCS12 格式, 请求签名用)
    private String merchantCertPfx;

    /// 商户证书密码
    private String merchantCertPassword;

    /// 宝付平台证书 cer 内容(Base64/PEM, 响应与回调验签用)
    private String platformCert;

    /// 是否沙箱环境(宝付聚合线无公开沙箱网关, 该标识仅透传记录)
    private Boolean sandbox;
}
