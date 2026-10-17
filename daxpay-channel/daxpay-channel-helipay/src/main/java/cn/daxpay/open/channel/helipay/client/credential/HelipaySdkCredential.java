package cn.daxpay.open.channel.helipay.client.credential;

import lombok.Data;

/// # 合利宝 SDK 凭证(主应用侧)
///
/// 从合利宝密钥配置(helipay_key_config)提取商户编号、扫码产品签名密钥与场景 AppId 后组装,
/// 随请求下发给子应用 dax-pay-channel-two。与子应用侧
/// `cn.daxpay.open.channel.helipay.config.HelipaySdkCredential` 字段镜像, 经 JSON 传输。
///
/// 合利宝扫码产品线为直连商户模式(无独立沙箱网关), 凭证无沙箱标记。
@Data
public class HelipaySdkCredential {

    /// 合利宝商户编号(customerNumber, 上送 P3_customerNumber)
    private String customerNumber;

    /// 子商户号(可空; 设置后覆盖 P3_customerNumber 发起交易)
    private String subCustomerNumber;

    /// 扫码产品签名密钥(MD5 签名与回调验签共用)
    private String signKey;

    /// 公众号/生活号支付 AppId(微信 JS 支付必填; 支付宝生活号固定传 1 由子应用处理)
    private String publicAppId;

    /// H5 支付 AppId(可空, 微信 H5 场景使用)
    private String h5AppId;

    /// 扫码支付报备ID(可空, 上送 P15_subMerchantId)
    private String reportId;
}
