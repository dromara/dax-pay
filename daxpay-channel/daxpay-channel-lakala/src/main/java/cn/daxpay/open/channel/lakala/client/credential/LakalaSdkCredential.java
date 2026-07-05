package cn.daxpay.open.channel.lakala.client.credential;

import lombok.Data;

/// # 拉卡拉 SDK 凭证(主应用侧)
///
/// 与子应用 dax-pay-channel-one 的 `LakalaSdkCredential` 镜像, 跨 HTTP 传输时字段对齐。
/// 主应用从服务商密钥配置(LakalaIsvKeyConfig) + 通道商户绑定(LakalaIsvChannelMerchant) 组装,
/// 下发给子应用发起拉卡拉 API 调用。
@Data
public class LakalaSdkCredential {
    /// 拉卡拉应用编号(lkl_app_id)
    private String lklAppId;
    /// 商户证书序列号
    private String mchSerialNo;
    /// 商户RSA私钥(PEM 格式 PKCS#8 字符串)
    private String privateKey;
    /// 拉卡拉RSA公钥证书(PEM 格式, 用于响应验签)
    private String publicKey;
    /// 拉卡拉商户编号(merchantNo)
    private String lakalaMchNo;
    /// 终端号(termNo)
    private String termNo;
    /// 是否沙箱环境
    private Boolean sandbox;
}
