package cn.daxpay.open.channel.alipay.client.resp;

import lombok.Data;

/// # 支付宝应用授权令牌换取响应
///
/// 与子应用 dax-pay-channel-one 的 `AlipayAppAuthTokenResp` 镜像, 字段对齐。
@Data
public class AlipayAppAuthTokenResp {

    /// 网关返回码(code, 10000=成功)
    private String code;

    /// 业务返回码(sub_code)
    private String subCode;

    /// 业务返回消息(sub_msg)
    private String subMsg;

    /// 应用授权令牌
    private String appAuthToken;

    /// 刷新令牌
    private String appRefreshToken;

    /// 授权方应用ID
    private String authAppId;

    /// 授权方支付宝用户ID(2088 开头)
    private String userId;

    /// 授权方 openId
    private String openId;

    /// 令牌有效期(秒)
    private String expiresIn;

    /// 刷新令牌有效期(秒)
    private String reExpiresIn;
}
