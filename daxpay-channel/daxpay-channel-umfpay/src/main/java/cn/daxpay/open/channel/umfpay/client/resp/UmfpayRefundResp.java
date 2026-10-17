package cn.daxpay.open.channel.umfpay.client.resp;

import lombok.Data;
import lombok.experimental.Accessors;

/// # 联动优势通道退款响应(主应用侧)
///
/// mer_refund 同步终态: 受理成功(ret_code=0000)即退款成功(complete=true)。
@Data
@Accessors(chain = true)
public class UmfpayRefundResp {

    /// 商户退款单号(透传 UmfpayRefundReq.outRefundNo)
    private String outRefundNo;

    /// 是否已终态完成(mer_refund 同步终态, 受理成功恒 true)
    private Boolean complete;

    /// 联动优势退款金额(单位: 分, 响应 refund_amt)
    private Long refundAmount;

    /// 原始响应参数串(用于对账/排障)
    private String syncData;
}
