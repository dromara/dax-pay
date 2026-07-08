package cn.daxpay.open.channel.dougong.client.req;

import cn.daxpay.open.channel.dougong.client.credential.DougongSdkCredential;
import lombok.Data;

/// # 斗拱通道关单请求(主应用侧)
///
/// 与子应用 dax-pay-channel-two 的 `DougongCloseReq` 镜像, 字段对齐。
@Data
public class DougongCloseReq {

    /// 通道调用凭证
    private DougongSdkCredential credential;

    /// 原商户订单号
    private String outTradeNo;

    /// 原汇付流水号(hf_seq_id)
    private String tradeNo;

    /// 原支付请求日期(yyyyMMdd)
    private String orgReqDate;
}
