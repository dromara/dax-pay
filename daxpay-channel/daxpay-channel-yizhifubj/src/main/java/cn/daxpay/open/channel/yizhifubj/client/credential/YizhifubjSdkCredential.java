package cn.daxpay.open.channel.yizhifubj.client.credential;

import lombok.Data;

/// # 一直付(北京)支付 SDK 凭证(主应用侧)
///
/// 主应用从直连密钥配置([cn.daxpay.open.channel.yizhifubj.entity.direct.YizhifubjDirectKeyConfig])提取
/// 商户编号、服务端ID、报备序列号、商户 pfx 证书与 5upay 平台证书(Base64)后组装,
/// 经声明式 HTTP 客户端下发给子应用 dax-pay-channel-two 发起 5upay 网关调用。
/// 字段与子应用 cn.daxpay.open.channel.yizhifubj.config.YizhifubjSdkCredential 保持一致(序列化互通)。
@Data
public class YizhifubjSdkCredential {

    /// 商户编号(merchantId, 5upay 开通后下发)
    private String merchantId;

    /// 服务端ID(partnerId, 可空; 机构服务端形态下下发)
    private String partnerId;

    /// 报备序列号(reportSerialNo, 可空; 下单上送通道侧报备用)
    private String reportSerialNo;

    /// 商户 pfx 证书内容(Base64, PKCS12 格式, 请求签名与 encryptKey 解密用)
    private String certPfx;

    /// 商户证书密码
    private String certPassword;

    /// 5upay 平台证书内容(Base64/DER, 可空; AES 密钥加密与回验签用, 空时子应用用内置默认平台证书)
    private String platformCert;

    /// 是否沙箱环境(5upay 无独立沙箱网关, 该标识仅透传记录)
    private Boolean sandbox;
}
