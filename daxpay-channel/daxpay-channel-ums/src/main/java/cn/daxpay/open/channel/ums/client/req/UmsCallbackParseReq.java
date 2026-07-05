package cn.daxpay.open.channel.ums.client.req;

import cn.daxpay.open.channel.ums.client.credential.UmsSdkCredential;
import lombok.Data;

import java.util.Map;

/// # 银联商务回调验签解析请求
///
/// 银联商务回调为 form 参数(Map), 与抖音的 header+body 方式不同。
@Data
public class UmsCallbackParseReq {

    /// 通道调用凭证(用于获取 secretKey 做回调验签)
    private UmsSdkCredential credential;

    /// 回调原始参数(含 sign/signType)
    private Map<String, String> params;
}
