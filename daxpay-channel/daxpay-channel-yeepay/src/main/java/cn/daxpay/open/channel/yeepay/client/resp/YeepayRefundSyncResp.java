package cn.daxpay.open.channel.yeepay.client.resp;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;

/// # 易宝通道退款查询响应(子应用 → 主应用)
@Data
@Accessors(chain = true)
public class YeepayRefundSyncResp {

    /// 商户退款单号(回显)
    private String outRefundNo;

    /// 易宝退款单号(uniqueRefundNo)
    private String tradeNo;

    /// 统一退款状态(SUCCESS / FAIL / PROGRESS)
    private String tradeStatus;

    /// 退款金额(单位: 分)
    private Long amount;

    /// 退款完成时间
    private OffsetDateTime finishTime;

    /// 原始响应数据
    private String syncData;
}
