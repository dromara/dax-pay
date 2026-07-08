package cn.daxpay.open.channel.fuyou.client.resp;

import lombok.Data;

/// # 富友退款响应(主应用侧)
@Data
public class FuyouRefundResp {

    private String outRefundNo;
    private String outRefundId;
    private String refundStatus;
}
