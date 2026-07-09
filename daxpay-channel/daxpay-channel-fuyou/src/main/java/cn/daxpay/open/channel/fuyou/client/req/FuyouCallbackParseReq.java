package cn.daxpay.open.channel.fuyou.client.req;

import cn.daxpay.open.channel.fuyou.client.credential.FuyouSdkCredential;
import lombok.Data;

/// # 富友回调验签解析请求(主应用 → 子应用)
///
/// 与子应用 dax-pay-channel-two 的 `FuyouCallbackParseReq` 镜像。
/// 主应用组装凭证 + 原始 `req` 表单参数值(URL 编码 XML, GBK) 下发, 子应用验签并解析。
@Data
public class FuyouCallbackParseReq {

    /// 通道调用凭证(用于获取富友公钥验签)
    private FuyouSdkCredential credential;

    /// 回调原始 `req` 表单参数值(URL 编码的 XML 字符串, GBK)
    private String reqParam;
}
