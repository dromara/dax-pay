package cn.daxpay.open.channel.leshua.client.resp;

import cn.daxpay.open.channel.leshua.client.enums.LeshuaPayBodyType;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;

/// # 乐刷通道支付响应(主应用侧镜像)
@Data
@Accessors(chain = true)
public class LeshuaPayResp {
    private String outTradeNo;
    private String leshuaOrderId;
    private String payBody;
    private LeshuaPayBodyType payBodyType;
    private Boolean complete;
    private Long totalAmount;
    private Long realAmount;
    private OffsetDateTime finishTime;
    private String buyerId;
    private String outTransNo;
}
