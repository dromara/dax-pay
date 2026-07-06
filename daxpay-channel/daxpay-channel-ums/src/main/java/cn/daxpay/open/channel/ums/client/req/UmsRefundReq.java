package cn.daxpay.open.channel.ums.client.req;

import cn.daxpay.open.channel.ums.client.credential.UmsSdkCredential;
import cn.daxpay.open.channel.ums.client.enums.UmsPayMethod;
import lombok.Data;

import java.time.OffsetDateTime;

/// # 银联商务通道退款请求
@Data
public class UmsRefundReq {

    /// 原商户订单号
    private String outTradeNo;

    /// 原订单创建时间(UTC), 由子应用按通道时区转换为银联商务 billDate(yyyy-MM-dd)
    private OffsetDateTime billDate;

    /// 退款单号
    private String outRefundNo;

    /// 退款金额(单位: 分)
    private Long refundAmount;

    /// 退款原因
    private String reason;

    /// 退款异步通知地址
    private String notifyUrl;

    /// 支付方式(区分扫码/H5 退款)
    private UmsPayMethod method;

    /// 通道调用凭证
    private UmsSdkCredential credential;
}
