package cn.daxpay.open.channel.yeepay.client.resp;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;

/// # 易宝通道退款响应(子应用 → 主应用)
@Data
@Accessors(chain = true)
public class YeepayRefundResp {

    /// 商户退款单号(回显)
    private String outRefundNo;

    /// 原商户订单号(回显)
    private String outTradeNo;

    /// 易宝退款单号(uniqueRefundNo)
    private String tradeNo;

    /// 退款是否同步完成
    private boolean complete;

    /// 退款金额(单位: 分)
    private Long amount;

    /// 退款完成时间(同步成功时返回)
    private OffsetDateTime finishTime;
}
