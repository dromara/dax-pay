package cn.daxpay.open.channel.lakala.client.req;

import cn.daxpay.open.channel.lakala.client.credential.LakalaSdkCredential;
import lombok.Data;

/// # 拉卡拉通道关单请求(主应用侧, 与子应用镜像)
@Data
public class LakalaCloseReq {

    private LakalaSdkCredential credential;

    /// 原商户订单号(与 originTradeNo 二选一)
    private String originOutTradeNo;

    /// 原拉卡拉交易号(与 originOutTradeNo 二选一)
    private String originTradeNo;

    /// 客户端IP
    private String clientIp;
}
