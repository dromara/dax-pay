package cn.daxpay.open.channel.wechat.client.credential;

import lombok.Data;

/// # 微信 SDK 凭证
///
/// 与子应用 dax-pay-channel-one 的 `WechatSdkCredential` 镜像, 跨 HTTP 传输时字段对齐。
/// 主应用从进件实体(WechatDirectApp + WechatDirectKeyConfig)提取商户号 / 应用ID / 私钥 / 证书后组装,
/// 下发给子应用构建 WxJava [com.github.binarywang.wxpay.service.WxPayService]。
@Data
public class WechatSdkCredential {
    /// 微信商户号(服务商模式下为服务商商户号 sp_mchid)
    private String wxMchId;
    /// 微信应用ID(公众号 / 小程序 / APP 的 appId; 服务商模式下为服务商 AppId sp_appid)
    private String wxAppId;
    /// 微信特约商户号(服务商模式 sub_mchid; 直连模式留空)
    private String subMchId;
    /// 微信子应用 AppId(服务商模式 sub_appid; 直连模式留空)
    private String subAppId;
    /// API V3 密钥
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
}
