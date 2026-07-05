package cn.daxpay.open.channel.ums.client.req;

import cn.daxpay.open.channel.ums.client.credential.UmsSdkCredential;
import cn.daxpay.open.channel.ums.client.enums.UmsPayMethod;
import lombok.Data;

/// # 银联商务通道支付同步请求
@Data
public class UmsSyncReq {

    /// 商户订单号
    private String outTradeNo;

    /// 订单日期(yyyy-MM-dd, 扫码查询必填)
    private String billDate;

    /// 支付方式(区分扫码/H5 查询)
    private UmsPayMethod method;

    /// 通道调用凭证
    private UmsSdkCredential credential;
}
