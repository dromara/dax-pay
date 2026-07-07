package cn.daxpay.open.channel.vbill.client.resp;

import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class VbillSyncResp {

    /// 商户订单号
    private String outTradeNo;

    /// 随行付网关订单号(uuid)
    private String outOrderNo;

    /// 交易状态(SUCCESS / PAYING / FAIL / CLOSED)
    private String tradeState;

    /// 订单总金额(单位: 分)
    private Long totalAmount;

    /// 实付金额(单位: 分)
    private Long realAmount;

    /// 完成时间
    private OffsetDateTime finishTime;

    /// 支付厂商
    private String tradeProduct;

    /// 支付方式
    private String tradeWay;

    /// 买家标识
    private String buyerId;

    /// 渠道交易单号
    private String transOrderNo;

    /// 同步原始数据
    private String syncData;

    /// 同步错误信息
    private String syncErrorMsg;
}
