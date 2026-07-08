package cn.daxpay.open.channel.leshua.client.credential;

import lombok.Data;

/// # 乐刷 SDK 凭证(主应用侧)
///
/// 与子应用 dax-pay-channel-two 的 `LeshuaSdkCredential` 镜像, 跨 HTTP 传输时字段对齐。
/// 主应用从服务商密钥配置(LeshuaIsvKeyConfig) + 通道商户绑定(LeshuaIsvChannelMerchant) 组装,
/// 下发给子应用发起乐刷 API 调用。
@Data
public class LeshuaSdkCredential {
    /// 乐刷商户编号(merchant_id)
    private String lsMchNo;
    /// 交易密钥(tradeKey, 用于请求签名与回调验签)
    private String tradeKey;
    /// 签名类型(MD5 / SM3)
    private String signType;
    /// 是否沙箱环境
    private Boolean sandbox;
}
