package cn.daxpay.open.channel.cpcn.client.resp;

import lombok.Data;
import lombok.experimental.Accessors;

/// # 中金通道退款响应(主应用侧)
///
/// 子应用调用中金 5021 退款后回传给主应用。
/// 受理成功仅代表退款请求已被中金接受(complete=false), 最终结果经 NoticeURL 异步通知。
@Data
@Accessors(chain = true)
public class CpcnRefundResp {

    /// 商户退款单号(透传 CpcnRefundReq.outRefundNo)
    private String outRefundNo;

    /// 是否已终态完成(受理恒 false, 等待异步通知)
    private Boolean complete;

    /// 原始响应明文(用于对账/排障)
    private String syncData;
}
