package cn.daxpay.open.channel.lakala.client.req;

import cn.daxpay.open.channel.lakala.client.credential.LakalaSdkCredential;
import lombok.Data;

/// # 拉卡拉通道退款查询请求(主应用侧, 与子应用镜像)
@Data
public class LakalaRefundSyncReq {

    private LakalaSdkCredential credential;

    /// 商户退款单号(与 originTradeNo 二选一)
    private String outRefundNo;

    /// 原拉卡拉退款交易号(与 outRefundNo 二选一)
    private String originTradeNo;
}
