package cn.daxpay.open.channel.vbill.client.credential;

import lombok.Data;

/// # 随行付 SDK 凭证(主应用侧)
///
/// 主应用从服务商密钥配置(VbillIsvKeyConfig) + 通道商户绑定(VbillIsvChannelMerchant) 提取
/// 机构号 / 私钥 / 公钥 / 商户号后组装, 经声明式 HTTP 客户端下发给子应用 dax-pay-channel-two。
///
/// 与子应用 [cn.daxpay.open.channel.vbill.config.VbillSdkCredential] 字段对称, 但属于主应用 client 包,
/// 独立序列化以避免主子应用包路径耦合。
@Data
public class VbillSdkCredential {

    /// 天阙合作机构ID(orgId)
    private String orgId;

    /// 商户RSA私钥(Base64, PKCS8, 去头尾)
    private String privateKey;

    /// 天阙RSA公钥(Base64, X509, 去头尾, 用于响应/回调验签)
    private String publicKey;

    /// 天阙商户号(mno, 子商户级)
    private String mno;

    /// 是否沙箱环境
    private Boolean sandbox;
}
