package cn.daxpay.open.channel.sheng.client.resp;

import cn.daxpay.open.channel.sheng.client.enums.ShengPayBodyType;
import lombok.Data;

import java.time.OffsetDateTime;

/// # 盛付通通道支付响应(主应用侧, 与子应用镜像)
@Data
public class ShengPayResp {

    private String outTradeNo;
    private String tradeNo;
    private String payBody;
    private ShengPayBodyType payBodyType;
    private Boolean complete;
    private Long totalAmount;
    private Long payerAmount;
    private OffsetDateTime finishTime;
    private String buyerId;
}
