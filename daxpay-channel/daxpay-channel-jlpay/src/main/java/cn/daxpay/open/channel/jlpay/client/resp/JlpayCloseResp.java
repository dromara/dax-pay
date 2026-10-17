package cn.daxpay.open.channel.jlpay.client.resp;

import lombok.Data;
import lombok.experimental.Accessors;

/// # 嘉联通道关单(撤销)响应
///
/// 子应用调用嘉联 /open/trans/cancel 撤销后回传给主应用。
@Data
@Accessors(chain = true)
public class JlpayCloseResp {

    /// 撤销受理的嘉联交易号(ori_transaction_id, 透传 JlpayCloseReq.outOrderNo)
    private String outOrderNo;

    /// 原始响应明文(用于对账/排障)
    private String syncData;
}
