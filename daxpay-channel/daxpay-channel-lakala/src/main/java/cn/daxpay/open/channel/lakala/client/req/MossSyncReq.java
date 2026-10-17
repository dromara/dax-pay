package cn.daxpay.open.channel.lakala.client.req;

import cn.daxpay.open.channel.lakala.client.credential.MossSdkCredential;
import lombok.Data;

/// # 拉卡拉 MOSS 订单查询请求(主应用侧, 与子应用镜像)
@Data
public class MossSyncReq {

    private MossSdkCredential credential;

    /// 商户订单号(主应用支付交易号)
    private String outTradeNo;
}
