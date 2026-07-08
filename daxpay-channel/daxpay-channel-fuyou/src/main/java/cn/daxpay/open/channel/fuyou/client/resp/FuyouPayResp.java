package cn.daxpay.open.channel.fuyou.client.resp;

import cn.daxpay.open.channel.fuyou.client.enums.FuyouPayBodyType;
import lombok.Data;

import java.time.OffsetDateTime;

/// # 富友通道支付响应(主应用侧)
@Data
public class FuyouPayResp {

    private String outTradeNo;
    private String outOrderNo;
    private String relationOrderNo;
    private String payBody;
    private FuyouPayBodyType payBodyType;
    private Boolean complete;
    private Long totalAmount;
    private Long realAmount;
    private OffsetDateTime finishTime;
    private String buyerId;
    private String tradeProduct;
}
