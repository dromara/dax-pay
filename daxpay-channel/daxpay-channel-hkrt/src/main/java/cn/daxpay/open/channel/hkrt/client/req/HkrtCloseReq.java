package cn.daxpay.open.channel.hkrt.client.req;

import cn.daxpay.open.channel.hkrt.client.credential.HkrtSdkCredential;
import lombok.Data;

/// # 海科融通通道关单请求(主应用侧, 与子应用镜像)
@Data
public class HkrtCloseReq {

    private HkrtSdkCredential credential;

    /// 原商户订单号(与 originTradeNo 二选一)
    private String originOutTradeNo;

    /// 原海科融通交易号(与 originOutTradeNo 二选一)
    private String originTradeNo;

    /// 客户端IP
    private String clientIp;
}
