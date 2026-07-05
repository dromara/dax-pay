package cn.daxpay.open.channel.lakala.client.req;

import cn.daxpay.open.channel.lakala.client.credential.LakalaSdkCredential;
import lombok.Data;

/// # 拉卡拉通道订单查询请求(主应用侧, 与子应用镜像)
@Data
public class LakalaSyncReq {

    private LakalaSdkCredential credential;

    /// 商户订单号(与 tradeNo 二选一)
    private String outTradeNo;

    /// 拉卡拉交易号(与 outTradeNo 二选一)
    private String tradeNo;
}
