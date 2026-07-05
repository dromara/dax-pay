package cn.daxpay.open.channel.lakala.client.resp;

import cn.daxpay.open.channel.lakala.client.enums.LakalaPayBodyType;
import lombok.Data;

import java.time.OffsetDateTime;

/// # 拉卡拉通道支付响应(主应用侧, 与子应用镜像)
@Data
public class LakalaPayResp {

    private String outTradeNo;
    private String tradeNo;
    private String payBody;
    private LakalaPayBodyType payBodyType;
    private Boolean complete;
    private Long totalAmount;
    private Long payerAmount;
    private OffsetDateTime finishTime;
    private String buyerId;
}
