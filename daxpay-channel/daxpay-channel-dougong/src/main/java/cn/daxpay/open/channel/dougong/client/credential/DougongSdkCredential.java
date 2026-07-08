package cn.daxpay.open.channel.dougong.client.credential;

import lombok.Data;

/// # 斗拱 SDK 凭证(主应用侧)
///
/// 与子应用 dax-pay-channel-two 的 `DougongSdkCredential` 镜像, 跨 HTTP 传输时字段对齐。
/// 主应用从服务商密钥配置(DougongIsvKeyConfig) + 通道商户绑定(DougongIsvChannelMerchant) 组装,
/// 下发给子应用发起斗拱(汇付) API 调用。
@Data
public class DougongSdkCredential {

    /// 汇付商户号(huifuId, 每笔交易必填)
    private String merchantNo;

    /// 商户 appId(汇付 SDK BasePay.putMerConfigs 的 key, 也是 BasePayClient.request 的入参)
    private String appId;

    /// 服务商系统ID(sysId)
    private String sysId;

    /// 产品号(productId)
    private String productId;

    /// 商户 RSA 私钥(PEM 格式 PKCS#8 字符串, 签名用)
    private String privateKey;

    /// 斗拱 RSA 公钥(PEM 格式, 回调/响应验签用)
    private String dgPublicKey;

    /// 是否沙箱环境
    private Boolean sandbox;
}
