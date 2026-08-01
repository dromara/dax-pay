package cn.daxpay.open.channel.union.client.req;

import cn.daxpay.open.channel.union.client.credential.UnionSdkCredential;
import lombok.Data;

import java.util.Map;

/// # 云闪付回调验签解析请求
///
/// 与子应用镜像, 主应用转发回调参数到子应用完成证书验签。
@Data
public class UnionCallbackParseReq {

    /// 通道调用凭证(用于获取中级证书做回调验签)
    private UnionSdkCredential credential;

    /// 回调原始参数(银联异步通知全部字段, 含 signature/signPubKeyCert)
    private Map<String, String> params;
}
