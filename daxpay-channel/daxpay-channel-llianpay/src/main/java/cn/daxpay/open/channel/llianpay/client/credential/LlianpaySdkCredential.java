package cn.daxpay.open.channel.llianpay.client.credential;

import lombok.Data;

/// # 连连支付 SDK 凭证(主应用侧)
///
/// 主应用从直连密钥配置([cn.daxpay.open.channel.llianpay.entity.direct.LlianpayDirectKeyConfig])提取
/// 连连商户编号、商户私钥与连连平台公钥(Base64 裸串)后组装,
/// 经声明式 HTTP 客户端下发给子应用 dax-pay-channel-two 发起连连 openapi 调用。
/// 字段与子应用 cn.daxpay.open.channel.llianpay.config.LlianpaySdkCredential 保持一致(序列化互通)。
@Data
public class LlianpaySdkCredential {

    /// 连连商户编号(mch_id)
    private String mchId;

    /// 二级商户号(可空; 平台商模式上送 sub_mchid)
    private String subMchid;

    /// 收款方用户ID(可空; 留空为商户自身收款, 配置后用户收款)
    private String payeeUid;

    /// 业务属性(busi_type, 如 100001 虚拟商品购买)
    private String busiType;

    /// 商户私钥(Base64 裸串, 兼容 PKCS1/PKCS8, 请求签名用)
    private String merchantPrivateKey;

    /// 连连平台公钥(Base64 裸串, X509 格式, 响应/回调验签用)
    private String llianpayPublicKey;

    /// 是否沙箱环境(切换连连测试网关)
    private Boolean sandbox;
}
