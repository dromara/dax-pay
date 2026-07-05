package cn.daxpay.open.channel.lakala.client.resp;

import lombok.Data;

import java.time.OffsetDateTime;

/// # 拉卡拉通道订单查询响应(主应用侧, 与子应用镜像)
@Data
public class LakalaSyncResp {

    private String outTradeNo;
    private String tradeNo;
    /// 交易状态: SUCCESS / FAIL / CLOSED / 其他(处理中)
    private String tradeState;
    private Long totalAmount;
    private String buyerId;
    private OffsetDateTime finishTime;
    private String syncData;
}
