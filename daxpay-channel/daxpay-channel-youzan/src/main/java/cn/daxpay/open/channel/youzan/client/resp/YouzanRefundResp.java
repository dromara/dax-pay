package cn.daxpay.open.channel.youzan.client.resp;

import lombok.Data;
import lombok.experimental.Accessors;

/// # 有赞通道退款响应
///
/// 子应用调用有赞开放网关统一退款后回传给主应用。
/// 受理成功仅代表有赞已接受退款请求(complete=false), 最终结果经 notify_url 异步通知。
@Data
@Accessors(chain = true)
public class YouzanRefundResp {

    /// 商户退款单号(透传 YouzanRefundReq.outRefundNo)
    private String outRefundNo;

    /// 有赞退款单号(响应 data.trade_no)
    private String refundOrderNo;

    /// 退款金额(单位: 分, 响应 data.refund_fee)
    private Long refundFee;

    /// 是否已终态完成(受理成功恒 false, 等待异步通知)
    private Boolean complete;

    /// 原始响应明文(用于对账/排障)
    private String syncData;
}
