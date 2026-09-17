package cn.daxpay.open.channel.sheng.client.req;

import cn.daxpay.open.channel.sheng.client.credential.ShengSdkCredential;
import lombok.Data;

/// # 盛付通通道退款请求(主应用侧, 与子应用镜像)
@Data
public class ShengRefundReq {

    private ShengSdkCredential credential;

    /// 商户退款单号(平台退款关联单号)
    private String outRefundNo;

    /// 原商户订单号(平台支付交易号)
    private String originOutTradeNo;

    /// 原盛付通交易号(首次退款时可能为空)
    private String originTradeNo;

    /// 退款金额(单位: 分)
    private Long amount;

    /// 退款原因
    private String reason;

    /// 退款异步通知地址(平台退款回调端点, 由子应用透传给盛付通)
    private String notifyUrl;

    /// 客户端IP
    private String clientIp;
}
