package cn.daxpay.open.channel.lakala.client.credential;

import lombok.Data;

/// # 拉卡拉 MOSS SDK 凭证(主应用侧, 与子应用镜像)
///
/// 跨 HTTP 传输时字段对齐子应用 `cn.daxpay.open.channel.lakala.moss.config.MossSdkCredential`。
/// 主应用从服务商密钥配置(lakala_isv_key_config, product=lakala_moss)
/// + 通道商户绑定(lakala_isv_channel_merchant) 组装, 下发给子应用发起 MOSS API 调用。
@Data
public class MossSdkCredential {

    /// MOSS 平台应用编号(businessChannel)
    private String appId;

    /// 商户 RSA 私钥(PEM 格式, 兼容 PKCS#1/PKCS#8, 签名 + 解密用)
    private String mchPrivateKey;

    /// MOSS 平台公钥(Base64 或 PEM, 验签用; 为空时子应用回退内置默认值)
    private String platformPublicKey;

    /// MOSS 商户编号(mer_no)
    private String merNo;
}
