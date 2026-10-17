package cn.daxpay.open.channel.ysep.client.req;

import cn.daxpay.open.channel.ysep.client.credential.YsepSdkCredential;
import lombok.Data;

import java.util.Map;

/// # 银盛回调验签解析请求(主应用 → 子应用)
///
/// 与子应用 dax-pay-channel-two 的 `YsepCallbackParseReq` 镜像。
/// 主应用组装凭证 + 回调原始表单参数下发, 子应用按报文形态分流两族协议做 SHA1withRSA 验签并解析。
@Data
public class YsepCallbackParseReq {

    /// 通道调用凭证(取银盛平台公钥证书用于回调验签)
    private YsepSdkCredential credential;

    /// 回调原始表单参数(form-urlencoded 解析后的键值对)
    private Map<String, String> formParams;
}
