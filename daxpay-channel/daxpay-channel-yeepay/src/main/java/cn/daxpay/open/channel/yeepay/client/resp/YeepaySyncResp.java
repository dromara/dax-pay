package cn.daxpay.open.channel.yeepay.client.resp;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;

/// # 易宝通道订单查询响应(子应用 → 主应用)
@Data
@Accessors(chain = true)
public class YeepaySyncResp {

    /// 商户订单号(回显)
    private String outTradeNo;

    /// 易宝交易号(uniqueOrderNo)
    private String tradeNo;

    /// 统一交易状态(SUCCESS / FAIL / CLOSED / PROGRESS)
    private String tradeStatus;

    /// 订单总金额(单位: 分)
    private Long totalAmount;

    /// 实付金额(单位: 分)
    private Long realAmount;

    /// 买家标识
    private String buyerId;

    /// 完成时间
    private OffsetDateTime finishTime;

    /// 原始响应数据
    private String syncData;
}
