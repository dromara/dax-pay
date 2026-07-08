package cn.daxpay.open.channel.dougong.client.req;

import cn.daxpay.open.channel.dougong.client.credential.DougongSdkCredential;
import lombok.Data;

/// # 斗拱通道退款请求(主应用侧)
///
/// 与子应用 dax-pay-channel-two 的 `DougongRefundReq` 镜像, 字段对齐。
@Data
public class DougongRefundReq {

    /// 通道调用凭证
    private DougongSdkCredential credential;

    /// 商户退款单号
    private String outRefundNo;

    /// 原汇付支付流水号(hf_seq_id)
    private String tradeNo;

    /// 原支付请求日期(yyyyMMdd)
    private String orgReqDate;

    /// 退款金额(单位: 分)
    private Long amount;

    /// 退款原因
    private String reason;

    /// 异步通知地址
    private String notifyUrl;
}
