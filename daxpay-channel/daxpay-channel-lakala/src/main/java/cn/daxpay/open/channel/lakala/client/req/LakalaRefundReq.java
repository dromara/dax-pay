package cn.daxpay.open.channel.lakala.client.req;

import cn.daxpay.open.channel.lakala.client.credential.LakalaSdkCredential;
import lombok.Data;

/// # 拉卡拉通道退款请求(主应用侧, 与子应用镜像)
@Data
public class LakalaRefundReq {

    private LakalaSdkCredential credential;

    /// 商户退款单号
    private String outRefundNo;

    /// 原商户订单号
    private String originOutTradeNo;

    /// 原拉卡拉交易号
    private String originTradeNo;

    /// 退款金额(单位: 分)
    private Long amount;

    /// 退款原因
    private String reason;

    /// 客户端IP
    private String clientIp;
}
