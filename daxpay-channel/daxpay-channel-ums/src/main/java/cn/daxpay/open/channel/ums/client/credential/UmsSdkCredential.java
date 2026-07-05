package cn.daxpay.open.channel.ums.client.credential;

import lombok.Data;

/// # 银联商务 SDK 凭证
///
/// 与子应用 dax-pay-channel-one 的 `UmsSdkCredential` 镜像, 跨 HTTP 传输时字段对齐。
/// 主应用从进件实体(ums_direct_channel_merchant / ums_direct_key_config)
/// 提取 appId / appKey / 商户号 / 终端号 / 通讯密钥后组装, 下发给子应用。
///
/// 银联商务签名无需证书, 仅依赖 appKey(HmacSHA256) 与 secretKey(回调验签 MD5/SHA256)。
@Data
public class UmsSdkCredential {

    /// 银联商务应用 AppId
    private String umsAppId;

    /// 应用密钥(HmacSHA256 签名密钥)
    private String appKey;

    /// 商户号(mid)
    private String merchantNo;

    /// 终端号(tid)
    private String terminalNo;

    /// 通讯密钥(回调验签 MD5/SHA256 拼接密钥)
    private String secretKey;

    /// 是否沙箱环境
    private boolean sandbox;
}
