package cn.daxpay.open.channel.hmpay.client.resp;

import lombok.Data;

import java.time.OffsetDateTime;

/// # 河马付通道订单查询响应(主应用侧)
@Data
public class HmpaySyncResp {

    /// 交易状态(SUCCESS / FAIL / CLOSE / 其他)
    private String tradeState;

    /// 商户订单号
    private String outTradeNo;

    /// 杉德流水号(plat_trx_no)
    private String tradeNo;

    /// 订单总金额(单位: 分)
    private Long totalAmount;

    /// 实付金额(单位: 分)
    private Long realAmount;

    /// 买家标识
    private String buyerId;

    /// 支付方式(pay_way_code)
    private String tradeWay;

    /// 通道外部交易号(bank_trx_no)
    private String outTransNo;

    /// 完成时间
    private OffsetDateTime finishTime;

    /// 原始响应数据
    private String syncData;
}
