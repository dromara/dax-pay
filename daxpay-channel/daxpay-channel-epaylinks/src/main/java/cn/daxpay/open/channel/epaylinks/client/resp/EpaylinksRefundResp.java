package cn.daxpay.open.channel.epaylinks.client.resp;

import lombok.Data;
import lombok.experimental.Accessors;

/// # 易票联通道退款响应(主应用侧)
@Data
@Accessors(chain = true)
public class EpaylinksRefundResp {

    /// 商户退款单号(透传 EpaylinksRefundReq.outRefundNo)
    private String outRefundNo;

    /// 退款金额(单位: 分, 响应 refundAmount 回显)
    private Long refundAmount;

    /// 是否已终态完成(易票联退款为同步成功语义, 受理成功恒 true)
    private Boolean complete;

    /// 原始响应明文(用于对账/排障)
    private String syncData;
}
