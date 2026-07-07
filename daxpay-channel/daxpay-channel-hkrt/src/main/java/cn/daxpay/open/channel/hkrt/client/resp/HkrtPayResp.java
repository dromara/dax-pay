package cn.daxpay.open.channel.hkrt.client.resp;

import cn.daxpay.open.channel.hkrt.client.enums.HkrtPayBodyType;
import lombok.Data;

import java.time.OffsetDateTime;

/// # 海科融通通道支付响应(主应用侧, 与子应用镜像)
@Data
public class HkrtPayResp {

    private String outTradeNo;
    private String tradeNo;
    private String payBody;
    private HkrtPayBodyType payBodyType;
    private Boolean complete;
    private Long totalAmount;
    private Long payerAmount;
    private OffsetDateTime finishTime;
    private String buyerId;
}
