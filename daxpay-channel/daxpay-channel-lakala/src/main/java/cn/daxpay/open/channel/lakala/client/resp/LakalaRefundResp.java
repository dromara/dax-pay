package cn.daxpay.open.channel.lakala.client.resp;

import lombok.Data;

import java.time.OffsetDateTime;

/// # 拉卡拉通道退款响应(主应用侧, 与子应用镜像)
@Data
public class LakalaRefundResp {

    private String outRefundNo;
    private String tradeNo;
    private Boolean complete;
    private OffsetDateTime finishTime;
}
