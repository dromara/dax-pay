package cn.daxpay.open.channel.leshua.client.req;

import cn.daxpay.open.channel.leshua.client.credential.LeshuaSdkCredential;
import lombok.Data;

/// # 乐刷通道退款查询请求(主应用侧镜像)
@Data
public class LeshuaRefundSyncReq {
    private LeshuaSdkCredential credential;
    private String leshuaOrderId;
    private String leshuaRefundId;
    private String outRefundNo;
}
