package cn.daxpay.open.channel.hkrt.client.credential;

import lombok.Data;

/// # 海科融通 SDK 凭证(主应用侧)
///
/// 与子应用 dax-pay-channel-two 的 `HkrtSdkCredential` 镜像, 跨 HTTP 传输时字段对齐。
/// 主应用从服务商密钥配置(HkrtIsvKeyConfig) + 通道商户绑定(HkrtIsvChannelMerchant) 组装,
/// 下发给子应用发起海科融通 API 调用。
///
/// 海科融通签名机制: 仅用 accessKey 做 MD5 大写签名(参数字母升序 + 末尾拼 accessKey), 无需证书。
@Data
public class HkrtSdkCredential {
    /// 服务商编号(agent_no)
    private String agentNo;
    /// 海科商户编号(merch_no)
    private String merchNo;
    /// 接入机构标识(access_id)
    private String accessId;
    /// SAAS 终端号(pn)
    private String pn;
    /// 签名密钥(access_key, MD5 大写签名)
    private String accessKey;
    /// 是否沙箱环境
    private Boolean sandbox;
}
