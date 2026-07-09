package cn.daxpay.open.channel.alipay.client.req;

import cn.daxpay.open.channel.alipay.client.credential.AlipaySdkCredential;
import lombok.Data;

/// # 支付宝应用授权令牌换取请求
///
/// 与子应用 dax-pay-channel-one 的 `AlipayAppAuthTokenReq` 镜像, 字段对齐。
@Data
public class AlipayAppAuthTokenReq {

    /// 应用授权码(支付宝回调回传的 app_auth_code)
    private String authCode;

    /// 通道调用凭证(服务商应用密钥/证书, 不带 appAuthToken)
    private AlipaySdkCredential credential;
}
