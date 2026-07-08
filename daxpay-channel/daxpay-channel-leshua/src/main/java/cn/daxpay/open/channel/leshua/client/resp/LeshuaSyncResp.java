package cn.daxpay.open.channel.leshua.client.resp;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;

/// # 乐刷通道订单查询响应(主应用侧镜像)
@Data
@Accessors(chain = true)
public class LeshuaSyncResp {
    private String outTradeNo;
    private String leshuaOrderId;
    private String tradeState;
    private Long totalAmount;
    private Long realAmount;
    private String buyerId;
    private String outTransNo;
    private OffsetDateTime finishTime;
    private String syncData;
}
