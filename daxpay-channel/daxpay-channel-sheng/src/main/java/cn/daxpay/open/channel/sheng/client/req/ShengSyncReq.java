package cn.daxpay.open.channel.sheng.client.req;

import cn.daxpay.open.channel.sheng.client.credential.ShengSdkCredential;
import lombok.Data;

/// # 盛付通通道订单查询请求(主应用侧, 与子应用镜像)
@Data
public class ShengSyncReq {

    private ShengSdkCredential credential;

    /// 商户订单号(与 tradeNo 二选一)
    private String outTradeNo;

    /// 盛付通交易号(与 outTradeNo 二选一)
    private String tradeNo;
}
