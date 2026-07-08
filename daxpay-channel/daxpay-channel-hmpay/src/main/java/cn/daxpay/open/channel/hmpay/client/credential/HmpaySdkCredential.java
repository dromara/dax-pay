package cn.daxpay.open.channel.hmpay.client.credential;

import lombok.Data;

/// # 河马付 SDK 凭证(主应用侧)
///
/// 与子应用 dax-pay-channel-two 的 `HmpaySdkCredential` 镜像, 跨 HTTP 传输时字段对齐。
/// 主应用从服务商配置(HmpayIsvKeyConfig) + 通道商户绑定(HmpayIsvChannelMerchant) 组装,
/// 下发给子应用发起河马付(杉德) API 调用。
@Data
public class HmpaySdkCredential {

    /// 杉德代理号(app_id, 服务商身份)
    private String sandAppId;

    /// 杉德商户编号(sub_app_id, 交易类请求必填)
    private String merchantNo;

    /// 门店号
    private String storeId;

    /// 商户 RSA 私钥(PKCS#8 Base64, 签名用)
    private String privateKey;

    /// 杉德 RSA 公钥(X509 Base64, 回调/响应验签用)
    private String publicKey;

    /// 是否沙箱环境
    private Boolean sandbox;

    /// 异步通知地址
    private String notifyUrl;
}
