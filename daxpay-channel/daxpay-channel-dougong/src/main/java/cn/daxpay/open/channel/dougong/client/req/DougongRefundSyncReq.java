package cn.daxpay.open.channel.dougong.client.req;

import cn.daxpay.open.channel.dougong.client.credential.DougongSdkCredential;
import lombok.Data;

/// # 斗拱通道退款同步请求(主应用侧)
///
/// 与子应用 dax-pay-channel-two 的 `DougongRefundSyncReq` 镜像, 字段对齐。
@Data
public class DougongRefundSyncReq {

    /// 通道调用凭证
    private DougongSdkCredential credential;

    /// 原汇付退款流水号(hf_seq_id)
    private String tradeNo;
}
