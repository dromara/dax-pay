package cn.daxpay.open.channel.ums.client.req;

import cn.daxpay.open.channel.ums.client.credential.UmsSdkCredential;
import cn.daxpay.open.channel.ums.client.enums.UmsPayMethod;
import lombok.Data;

/// # 银联商务通道退款同步请求
@Data
public class UmsRefundSyncReq {

    /// 退款单号
    private String outRefundNo;

    /// 原商户订单号
    private String outTradeNo;

    /// 原订单日期(yyyy-MM-dd, 扫码退款查询必填)
    private String billDate;

    /// 支付方式(区分扫码/H5 退款查询)
    private UmsPayMethod method;

    /// 通道调用凭证
    private UmsSdkCredential credential;
}
