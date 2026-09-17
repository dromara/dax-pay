package cn.daxpay.open.channel.sheng.client.credential;

import lombok.Data;

/// # 盛付通 SDK 凭证(主应用侧)
///
/// 与子应用 dax-pay-channel-two 的 `ShengSdkCredential` 镜像, 跨 HTTP 传输时字段对齐。
/// 商户模式(sheng_pay)从 ShengKeyConfig 组装, 服务商模式(sheng_isv)由全局服务商密钥+行级子商户绑定合并组装,
/// 下发给子应用发起盛付通 API 调用。子应用按字段非空上送, 无模式概念。
///
/// 盛付通不提供集测接口(2026-09-17 官方二次确认), 全部交易走生产网关,
/// 凭证不携带沙箱标记。
@Data
public class ShengSdkCredential {
    /// 盛付通商户号(mchId, 商户模式=商户自有商户号 / 服务商模式=服务商商户号)
    private String mchId;
    /// 子商户号(subMchId, 服务商模式代子商户发起时上送, 商户模式为空不上送)
    private String subMchId;
    /// 盛付通分配的 AppId(sdpAppId, 直连场景必填)
    private String sdpAppId;
    /// 商户RSA私钥(PKCS8, SHA1withRSA 签名)
    private String merchantPrivateKey;
    /// 盛付通验签公钥(响应/回调验签)
    private String shengpayPublicKey;
}
