package cn.daxpay.open.channel.alipay.client.req;

import cn.daxpay.open.channel.alipay.client.credential.AlipaySdkCredential;
import lombok.Data;

import java.util.Map;

/// # 支付宝回调验签解析请求(主应用 → 子应用)
///
/// 与子应用 dax-pay-channel-one 的 `AlipayCallbackParseReq` 镜像, 跨 HTTP 传输时字段对齐。
/// 主应用组装凭证 + 原始表单参数后下发, 子应用用公钥/证书验签并解析。
@Data
public class AlipayCallbackParseReq {

    /// 通道调用凭证(用于获取支付宝公钥/证书验签)
    private AlipaySdkCredential credential;

    /// 回调原始表单参数(支付宝异步通知全部 form 参数, 含 sign / sign_type)
    private Map<String, String> params;
}
