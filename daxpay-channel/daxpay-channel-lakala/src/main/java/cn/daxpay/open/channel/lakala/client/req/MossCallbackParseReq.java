package cn.daxpay.open.channel.lakala.client.req;

import cn.daxpay.open.channel.lakala.client.credential.MossSdkCredential;
import lombok.Data;

/// # 拉卡拉 MOSS 回调验签解析请求(主应用侧, 与子应用镜像)
///
/// MOSS 回调签名要素在报文体 sign 字段(非请求头), 故只需转发 body。
@Data
public class MossCallbackParseReq {

    private MossSdkCredential credential;

    /// 回调原始 body(MOSS 异步通知 JSON: head + requestEncrypted + sign)
    private String body;
}
