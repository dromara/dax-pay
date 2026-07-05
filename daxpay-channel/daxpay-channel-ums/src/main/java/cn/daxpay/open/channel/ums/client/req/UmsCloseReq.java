package cn.daxpay.open.channel.ums.client.req;

import cn.daxpay.open.channel.ums.client.credential.UmsSdkCredential;
import cn.daxpay.open.channel.ums.client.enums.UmsPayMethod;
import lombok.Data;

/// # 银联商务通道关闭订单请求
@Data
public class UmsCloseReq {

    /// 商户订单号
    private String outTradeNo;

    /// 二维码 ID(扫码关单必填)
    private String qrCodeId;

    /// 支付方式(区分扫码/H5 关单)
    private UmsPayMethod method;

    /// 通道调用凭证
    private UmsSdkCredential credential;
}
