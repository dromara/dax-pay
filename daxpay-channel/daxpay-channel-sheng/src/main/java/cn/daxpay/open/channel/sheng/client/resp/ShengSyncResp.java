package cn.daxpay.open.channel.sheng.client.resp;

import lombok.Data;

import java.time.OffsetDateTime;

/// # 盛付通通道订单查询响应(主应用侧, 与子应用镜像)
@Data
public class ShengSyncResp {

    private String outTradeNo;
    private String tradeNo;
    /// 交易状态(盛付通原始状态: PAY_SUCCESS/PAY_INIT/PAY_ING/PAY_FAIL/CLOSED/REFUND 族)
    private String tradeState;
    private Long totalAmount;
    private String buyerId;
    private OffsetDateTime finishTime;
    private String syncData;
}
