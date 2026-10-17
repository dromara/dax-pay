package cn.daxpay.open.channel.ysep.client.credential;

import lombok.Data;

/// # 银盛 SDK 凭证(主应用侧)
///
/// 与子应用 dax-pay-channel-two 的 `YsepSdkCredential` 镜像, 跨 HTTP 传输时字段对齐。
/// 从 YsepKeyConfig(按通道商户号定位)组装, 下发给子应用发起银盛 API 调用。
///
/// 银盛一通道两产品共用本凭证结构(字段按产品取用):
/// - ysep_pay(开放平台): sponsorMchNo(partner_id) + payeeMchNo(seller_id) + businessCode(business_code)
/// - yseqt_pay(e企通): sponsorMchNo(srcMerchantNo) + payeeMchNo(payeeMerchantNo)
///
/// 银盛不提供集测沙箱网关, 凭证不携带沙箱标记。
@Data
public class YsepSdkCredential {

    /// 发起方商户号(ysep_pay=partner_id / yseqt_pay=srcMerchantNo)
    private String sponsorMchNo;

    /// 收款商户号(ysep_pay=seller_id / yseqt_pay=payeeMerchantNo)
    private String payeeMchNo;

    /// 业务代码(ysep_pay 专用 business_code; yseqt_pay 不使用)
    private String businessCode;

    /// 商户私钥证书 pfx 内容(Base64, PKCS12 格式, SHA1withRSA 请求签名)
    private String merchantCertPfx;

    /// 商户私钥证书密码
    private String merchantCertPassword;

    /// 银盛平台公钥证书 cer 内容(Base64/PEM 兼容, 响应/回调验签)
    private String ysepayPublicCert;
}
