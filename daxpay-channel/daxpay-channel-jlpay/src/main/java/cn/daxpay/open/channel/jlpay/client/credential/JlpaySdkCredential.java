package cn.daxpay.open.channel.jlpay.client.credential;

import lombok.Data;

/// # 嘉联 SDK 凭证
///
/// 主应用从直连密钥配置(JlpayDirectKeyConfig)提取嘉联应用 appid、商户号、终端号与
/// SM2 密钥对(商户私钥 + 平台公钥, 均支持 hex / PEM 文本)后组装, 下发给子应用发起嘉联 OpenAPI 调用。
///
/// 仅承载通道调用所需的身份与密钥信息, 不含任何业务字段。
@Data
public class JlpaySdkCredential {

    /// 嘉联开放平台应用 appid(请求头 x-jlpay-appid)
    private String appId;

    /// 嘉联商户号(mch_id)
    private String mchId;

    /// 嘉联终端号(term_no)
    private String termNo;

    /// 商户 SM2 私钥(hex 或 PEM 文本, 请求签名用, 加密存储)
    private String merchantPrivateKey;

    /// 嘉联平台 SM2 公钥(hex 或 PEM 文本, 响应/通知验签用, 加密存储)
    private String platformPublicKey;

    /// 是否沙箱环境(true 走 UAT 网关 openapi-uat.jlpay.com)
    private Boolean sandbox;
}
