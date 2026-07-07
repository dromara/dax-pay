package cn.daxpay.open.channel.hkrt.client.req;

import cn.daxpay.open.channel.hkrt.client.credential.HkrtSdkCredential;
import lombok.Data;

/// # 海科融通通道订单查询请求(主应用侧, 与子应用镜像)
@Data
public class HkrtSyncReq {

    private HkrtSdkCredential credential;

    /// 商户订单号(与 tradeNo 二选一)
    private String outTradeNo;

    /// 海科融通交易号(与 outTradeNo 二选一)
    private String tradeNo;
}
