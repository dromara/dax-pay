package cn.daxpay.open.channel.fuyou.client.credential;

import lombok.Data;

/// # 富友 SDK 凭证(主应用侧)
///
/// 主应用从服务商密钥配置(FuyouIsvKeyConfig) + 通道商户绑定(FuyouIsvChannelMerchant) 提取
/// 机构号 / 私钥 / 公钥 / 商户号 / 终端号后组装, 经声明式 HTTP 客户端下发给子应用 dax-pay-channel-two。
///
/// 与子应用 [cn.daxpay.open.channel.fuyou.config.FuyouSdkCredential] 字段对称, 但属于主应用 client 包,
/// 独立序列化以避免主子应用包路径耦合。
@Data
public class FuyouSdkCredential {

    /// 富友应用编号(机构号 ins_cd)
    private String fyAppId;

    /// 富友商户编号(mchnt_cd, 子商户级)
    private String merchantNo;

    /// 终端号(term_id)
    private String termNo;

    /// 商户RSA私钥(Base64, PKCS8, MD5withRSA 签名)
    private String privateKey;

    /// 富友RSA公钥(Base64, X509, 响应/回调验签)
    private String publicKey;

    /// 订单前缀(关联订单号前缀, 富友回调凭 mchnt_order_no 反查平台订单)
    private String orderPrefix;

    /// 是否沙箱环境
    private Boolean sandbox;
}
