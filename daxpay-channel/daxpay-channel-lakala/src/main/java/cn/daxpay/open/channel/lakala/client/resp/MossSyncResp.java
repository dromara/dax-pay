package cn.daxpay.open.channel.lakala.client.resp;

import lombok.Data;

import java.time.OffsetDateTime;

/// # 拉卡拉 MOSS 订单查询响应(主应用侧, 与子应用镜像)
@Data
public class MossSyncResp {

    /// 商户订单号
    private String outTradeNo;

    /// MOSS 通道流水号(pay_serial)
    private String tradeNo;

    /// 交易状态(trade_state)
    private String tradeState;

    /// 订单总金额(单位: 分)
    private Long totalAmount;

    /// 买家标识(user_id2)
    private String buyerId;

    /// 完成时间
    private OffsetDateTime finishTime;

    /// 原始响应数据(解密后业务报文, 用于对账/排障)
    private String syncData;
}
