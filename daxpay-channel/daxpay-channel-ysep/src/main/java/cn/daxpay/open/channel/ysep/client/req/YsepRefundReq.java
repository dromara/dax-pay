package cn.daxpay.open.channel.ysep.client.req;

import cn.daxpay.open.channel.ysep.client.credential.YsepSdkCredential;
import lombok.Data;

/// # 银盛通道退款请求(主应用侧, 与子应用镜像)
@Data
public class YsepRefundReq {

    private YsepSdkCredential credential;

    /// 产品编码(ysep_pay / yseqt_pay)
    private String product;

    /// 商户退款单号(平台退款关联单号)
    private String outRefundNo;

    /// 原商户订单号(平台支付交易号)
    private String originOutTradeNo;

    /// 原银盛交易单号(首次退款时可能为空)
    private String originTradeNo;

    /// 退款金额(单位: 分)
    private Long amount;

    /// 退款原因
    private String reason;
}
