package cn.daxpay.open.channel.lakala.client.resp;

import lombok.Data;

import java.time.OffsetDateTime;

/// # 拉卡拉 MOSS 退款响应(主应用侧, 与子应用镜像)
@Data
public class MossRefundResp {

    /// 商户退款单号(透传 MossRefundReq.outRefundNo)
    private String outRefundNo;

    /// MOSS 退款交易号(trade_no)
    private String tradeNo;

    /// 是否已完成
    private Boolean complete;

    /// 退款完成时间
    private OffsetDateTime finishTime;
}
