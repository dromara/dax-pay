package cn.daxpay.open.channel.leshua.client.req;

import cn.daxpay.open.channel.leshua.client.credential.LeshuaSdkCredential;
import lombok.Data;

/// # 乐刷通道关单请求(主应用侧镜像)
@Data
public class LeshuaCloseReq {
    private LeshuaSdkCredential credential;
    private String leshuaOrderId;
    private String outTradeNo;
    private String clientIp;
}
