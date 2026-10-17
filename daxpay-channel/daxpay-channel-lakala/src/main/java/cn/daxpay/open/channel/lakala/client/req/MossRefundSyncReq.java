package cn.daxpay.open.channel.lakala.client.req;

import cn.daxpay.open.channel.lakala.client.credential.MossSdkCredential;
import lombok.Data;

/// # 拉卡拉 MOSS 退款查询请求(主应用侧, 与子应用镜像)
@Data
public class MossRefundSyncReq {

    private MossSdkCredential credential;

    /// 商户退款单号
    private String outRefundNo;
}
