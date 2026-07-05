package cn.daxpay.open.channel.douyin.client.req;

import cn.daxpay.open.channel.douyin.client.credential.DouyinSdkCredential;
import lombok.Data;

/// # 抖音回调验签解析请求
///
/// 主应用接收到抖音异步通知后, 将原始 header + body 连同通道凭证转发到子应用验签。
@Data
public class DouyinCallbackParseReq {
    /// 通道调用凭证
    private DouyinSdkCredential credential;
    /// 回调原始 body
    private String body;
    /// 回调头: Douyinpay-Serial
    private String serial;
    /// 回调头: Douyinpay-Nonce
    private String nonce;
    /// 回调头: Douyinpay-Signature
    private String signature;
    /// 回调头: Douyinpay-Timestamp
    private String timestamp;
}
