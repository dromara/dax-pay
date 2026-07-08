package cn.daxpay.open.channel.yeepay.client.req;

import cn.daxpay.open.channel.yeepay.client.credential.YeepaySdkCredential;
import lombok.Data;

/// # 易宝回调验签解析请求(主应用 → 子应用)
///
/// 易宝异步通知为 form 表单格式, 含 response(数字信封密文) 与 customerIdentification(appKey)。
@Data
public class YeepayCallbackParseReq {

    /// 通道调用凭证(含 appKey/私钥, 用于数字信封解密)
    private YeepaySdkCredential credential;

    /// 易宝通知 response 字段(RSA2048 数字信封密文)
    private String response;

    /// 易宝通知 customerIdentification 字段(appKey 标识)
    private String customerIdentification;
}
