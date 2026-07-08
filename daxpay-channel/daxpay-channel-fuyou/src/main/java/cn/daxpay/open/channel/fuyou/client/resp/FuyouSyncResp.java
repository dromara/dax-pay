package cn.daxpay.open.channel.fuyou.client.resp;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;

/// # 富友订单同步响应(主应用侧)
@Data
@Accessors(chain = true)
public class FuyouSyncResp {

    private String syncStatus;
    private Boolean syncSuccess;
    private String syncErrorMsg;
    private String outTradeNo;
    private String outOrderNo;
    private Long amount;
    private OffsetDateTime finishTime;
    private String buyerId;
    private String tradeProduct;
}
