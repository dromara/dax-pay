package cn.daxpay.open.channel.hkrt.client.resp;

import lombok.Data;

import java.time.OffsetDateTime;

/// # 海科融通通道退款响应(主应用侧, 与子应用镜像)
@Data
public class HkrtRefundResp {

    /// 商户退款单号(透传请求 outRefundNo)
    private String outRefundNo;

    /// 海科退款订单号(refund_no)
    private String tradeNo;

    /// 交易状态(抽象态 SUCCESS / FAIL / PROCESSING, 已屏蔽海科 trade_status 数字码)
    private String tradeStatus;

    /// 是否已终态完成
    private Boolean complete;

    /// 完成时间
    private OffsetDateTime finishTime;
}
