package cn.daxpay.open.channel.duolabao.client.resp;

import lombok.Data;
import lombok.experimental.Accessors;

/// # 哆啦宝通道退款响应(主应用侧)
///
/// 哆啦宝退款为同步受理即成功(无退款异步通知), 受理成功即置终态。
@Data
@Accessors(chain = true)
public class DuolabaoRefundResp {

    /// 商户退款单号(透传)
    private String outRefundNo;

    /// 哆啦宝退款订单号(orderNum)
    private String outRefundOrderNo;

    /// 是否已终态完成(受理成功恒 true)
    private Boolean complete;

    /// 实际退款金额(单位: 分)
    private Long refundAmount;

    /// 原始响应明文(用于对账/排障)
    private String syncData;
}
