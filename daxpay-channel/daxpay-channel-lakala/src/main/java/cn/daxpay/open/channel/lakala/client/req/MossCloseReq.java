package cn.daxpay.open.channel.lakala.client.req;

import cn.daxpay.open.channel.lakala.client.credential.MossSdkCredential;
import lombok.Data;

/// # 拉卡拉 MOSS 关单请求(主应用侧, 与子应用镜像)
///
/// MOSS 无远端关单接口, 子应用按「查单确认非成功即视为关闭」处理。
@Data
public class MossCloseReq {

    private MossSdkCredential credential;

    /// 商户订单号(主应用支付交易号)
    private String outTradeNo;
}
