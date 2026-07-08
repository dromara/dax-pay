package cn.daxpay.open.channel.leshua.client.req;

import cn.daxpay.open.channel.leshua.client.credential.LeshuaSdkCredential;
import lombok.Data;

/// # 乐刷通道订单查询请求(主应用侧镜像)
@Data
public class LeshuaSyncReq {
    private LeshuaSdkCredential credential;
    private String leshuaOrderId;
    private String outTradeNo;
}
