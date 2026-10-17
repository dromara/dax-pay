package cn.daxpay.open.channel.ysep.client.resp;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;

/// # 银盛通道订单查询响应(主应用侧, 与子应用镜像)
@Data
@Accessors(chain = true)
public class YsepSyncResp {

    /// 商户订单号
    private String outTradeNo;

    /// 银盛交易单号
    private String tradeNo;

    /// 交易状态(银盛原始状态: ysep_pay=TRADE_SUCCESS/TRADE_CLOSED 等 / yseqt_pay=SUCCESS/FAIL/PROCESSING 等)
    private String tradeState;

    /// 订单总金额(单位: 分)
    private Long totalAmount;

    /// 买家标识
    private String buyerId;

    /// 完成时间
    private OffsetDateTime finishTime;

    /// 原始响应数据
    private String syncData;
}
