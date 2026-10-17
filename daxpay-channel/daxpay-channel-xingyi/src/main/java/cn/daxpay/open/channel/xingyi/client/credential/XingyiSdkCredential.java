package cn.daxpay.open.channel.xingyi.client.credential;

import lombok.Data;

/// # 星驿付支付 SDK 凭证(主应用侧)
///
/// 主应用从直连密钥配置([cn.daxpay.open.channel.xingyi.entity.direct.XingyiDirectKeyConfig])提取
/// 机构号/商户号/商户RSA公钥后组装, 经声明式 HTTP 客户端下发给子应用 dax-pay-channel-two
/// 发起星驿付驿云付(YYF)网关调用。
/// 字段与子应用 cn.daxpay.open.channel.xingyi.config.XingyiSdkCredential 保持一致(序列化互通)。
@Data
public class XingyiSdkCredential {

    /// 机构号(agetId, 星驿付分配的机构标识)
    private String agetId;

    /// 星驿付商户号(custId)
    private String custId;

    /// 商户RSA公钥(Base64 字符串, 兼容 PEM 头尾; 请求签名与回调验签共用)
    private String merchantPublicKey;

    /// 是否沙箱环境(true 走测试网关)
    private Boolean sandbox;
}
