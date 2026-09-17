package cn.daxpay.open.channel.sheng.client.req;

import cn.daxpay.open.channel.sheng.client.credential.ShengSdkCredential;
import lombok.Data;

/// # 盛付通通道退款查询请求(主应用侧, 与子应用镜像)
@Data
public class ShengRefundSyncReq {

    private ShengSdkCredential credential;

    /// 商户退款单号(与 originTradeNo 二选一)
    private String outRefundNo;

    /// 原盛付通退款交易号(与 outRefundNo 二选一)
    private String originTradeNo;
}
