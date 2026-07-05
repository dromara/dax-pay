package cn.daxpay.open.channel.douyin.client.credential;

import lombok.Data;

/// # 抖音 SDK 凭证
///
/// 与子应用 dax-pay-channel-one 的 `DouyinSdkCredential` 镜像, 跨 HTTP 传输时字段对齐。
/// 主应用从进件实体(douyin_direct_app / douyin_direct_key_config / douyin_direct_channel_merchant)
/// 提取 appId / mchId / 私钥 / 证书序列号 / 加密密钥后组装, 下发给子应用构建抖音 SDK Client。
@Data
public class DouyinSdkCredential {
    /// 抖音应用 AppId(APPID)
    private String douyinAppId;
    /// 抖音商户号(MCHID, 来自通道商户绑定表 dyMchId)
    private String mchId;
    /// 商家公钥证书序列号(MERCHANT_SERIAL_NO)
    private String merchantSerialNumber;
    /// 商户私钥(MERCHANT_PRIVATE_KEY, PKCS8)
    private String merchantPrivateKey;
    /// 接口加密密钥(ENCRYPT_KEY, 用于回调密文 AES 解密)
    private String encryptKey;
}
