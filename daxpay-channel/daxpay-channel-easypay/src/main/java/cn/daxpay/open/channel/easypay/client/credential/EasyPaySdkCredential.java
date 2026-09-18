package cn.daxpay.open.channel.easypay.client.credential;

import lombok.Data;

/// # 易支付 SDK 凭证(主应用侧)
///
/// 从易支付密钥配置(easy_pay_key_config)提取平台地址/商户ID/密钥后组装, 随请求下发给子应用 dax-pay-channel-two。
/// 与子应用侧 `cn.daxpay.open.channel.easypay.config.EasyPaySdkCredential` 字段镜像, 经 JSON 传输。
///
/// 易支付为三方聚合平台, 网关地址随通道商户配置(无统一官方网关), 故 serverUrl 也随凭证下发;
/// 不提供集测环境, 凭证无沙箱标记。
@Data
public class EasyPaySdkCredential {

    /// 易支付平台网关地址(如 https://pay.xxx.com)
    private String serverUrl;

    /// 易支付商户ID(pid, 上游平台分配)
    private String partnerId;

    /// 商户 RSA 私钥(PKCS8 格式 Base64 字符串, 用于请求签名)
    private String merchantPrivateKey;

    /// 易支付平台 RSA 公钥(Base64 字符串, 用于响应/回调验签)
    private String platformPublicKey;
}
