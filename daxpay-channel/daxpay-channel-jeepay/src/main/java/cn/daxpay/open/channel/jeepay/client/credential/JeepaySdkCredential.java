package cn.daxpay.open.channel.jeepay.client.credential;

import lombok.Data;

/// # Jeepay SDK 凭证(主应用侧)
///
/// 从 Jeepay 密钥配置(jee_pay_key_config)提取平台地址/商户号/应用ID/密钥后组装, 随请求下发给子应用 dax-pay-channel-two。
/// 与子应用侧 `cn.daxpay.open.channel.jeepay.config.JeepaySdkCredential` 字段镜像, 经 JSON 传输。
///
/// Jeepay 为三方聚合平台, 网关地址随通道商户配置(无统一官方网关), 故 serverUrl 也随凭证下发;
/// 普通商户模式 MD5 签名(appSecret), 不提供集测环境, 凭证无沙箱标记。
@Data
public class JeepaySdkCredential {

    /// Jeepay 平台网关地址(如 https://pay.jeepay.vip)
    private String serverUrl;

    /// Jeepay 商户号(mchNo, 上游平台分配)
    private String mchNo;

    /// 应用ID(appId, Jeepay 商户后台的应用标识)
    private String appId;

    /// 应用密钥(appSecret, MD5 签名钥)
    private String appSecret;
}
