package cn.daxpay.open.channel.wechat.client.credential;

import lombok.Data;

/// # 微信 SDK 凭证
///
/// 与子应用 dax-pay-channel-one 的 `WechatSdkCredential` 镜像, 跨 HTTP 传输时字段对齐。
/// 主应用从进件实体(WechatDirectApp + WechatDirectKeyConfig)提取商户号 / 应用ID / 私钥 / 证书后组装,
/// 下发给子应用构建 WxJava [com.github.binarywang.wxpay.service.WxPayService]。
@Data
public class WechatSdkCredential {
    /// 微信商户号
    private String wxMchId;
    /// 微信应用ID(公众号 / 小程序 / APP 的 appId)
    private String wxAppId;
    /// APIv3 密钥
    private String apiKeyV3;
    /// 商户私钥(PEM 格式 PKCS#8 字符串)
    private String privateKey;
    /// 商户证书内容(PEM 格式)
    private String privateCert;
    /// 商户证书序列号
    private String certSerialNo;
    /// 支付公钥(支付公钥新模式使用, 为空则走平台证书模式)
    private String publicKey;
    /// 支付公钥ID
    private String publicKeyId;
    /// APIv2 密钥(仅付款码 MICROPAY 走 V2 接口时需要)
    private String apiV2Key;
    /// 是否沙箱环境
    private Boolean sandbox;
}
