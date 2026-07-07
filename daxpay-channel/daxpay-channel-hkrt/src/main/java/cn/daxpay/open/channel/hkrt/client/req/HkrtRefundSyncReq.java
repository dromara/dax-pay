package cn.daxpay.open.channel.hkrt.client.req;

import cn.daxpay.open.channel.hkrt.client.credential.HkrtSdkCredential;
import lombok.Data;

/// # 海科融通通道退款查询请求(主应用侧, 与子应用镜像)
@Data
public class HkrtRefundSyncReq {

    private HkrtSdkCredential credential;

    /// 商户退款单号(与 originTradeNo 二选一)
    private String outRefundNo;

    /// 原海科融通退款交易号(与 outRefundNo 二选一)
    private String originTradeNo;
}
