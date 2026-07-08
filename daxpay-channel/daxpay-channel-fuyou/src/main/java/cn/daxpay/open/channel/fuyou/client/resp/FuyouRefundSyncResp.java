package cn.daxpay.open.channel.fuyou.client.resp;

import lombok.Data;
import lombok.experimental.Accessors;

/// # 富友退款同步响应(主应用侧)
@Data
@Accessors(chain = true)
public class FuyouRefundSyncResp {

    private String refundStatus;
    private Boolean syncSuccess;
    private String syncErrorMsg;
    private String outRefundNo;
    private Long amount;
}
