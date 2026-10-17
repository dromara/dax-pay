package cn.daxpay.open.channel.cpcn.client.credential;

import lombok.Data;

/// # 中金支付 SDK 凭证(主应用侧)
///
/// 主应用从直连密钥配置([cn.daxpay.open.channel.cpcn.entity.direct.CpcnDirectKeyConfig])提取
/// 机构编号、网关地址、商户证书与平台证书(Base64)后组装,
/// 经声明式 HTTP 客户端下发给子应用 dax-pay-channel-two 发起中金网关调用。
/// 字段与子应用 cn.daxpay.open.channel.cpcn.config.CpcnSdkCredential 保持一致(序列化互通)。
@Data
public class CpcnSdkCredential {

    /// 中金机构编号(InstitutionID, 接入机构唯一标识)
    private String institutionId;

    /// 中金网关地址(随凭证下发: 中金各业务线网关由开户开通后下发, 无统一公开网关)
    private String gatewayUrl;

    /// 商户证书 pfx 内容(Base64, PKCS12 格式, 国密 SM2 私钥, 请求签名用)
    private String merchantCertPfx;

    /// 商户证书密码
    private String merchantCertPassword;

    /// 中金平台证书 cer 内容(Base64 或 PEM 文本, 响应/通知验签用)
    private String platformCert;

    /// 指定分账子商户号(可空, 多个用英文逗号分隔, 上送 Extension.AppointMerchantInfos)
    private String appointMerchantIds;

    /// 是否沙箱环境(中金测试环境与生产环境网关/证书独立, 该标识仅透传记录)
    private Boolean sandbox;
}
