package cn.daxpay.open.channel.llianpay.client.resp;

import lombok.Data;
import lombok.experimental.Accessors;

/// # 连连通道退款响应(主应用侧)
///
/// 受理成功仅代表退款请求已被连连接受(complete=false), 最终结果经异步通知。
@Data
@Accessors(chain = true)
public class LlianpayRefundResp {

    /// 商户退款单号(透传 LlianpayRefundReq.outRefundNo)
    private String outRefundNo;

    /// 是否已终态完成(连连退款受理恒 false, 等待异步通知)
    private Boolean complete;

    /// 连连平台退款流水号(platform_refundno)
    private String outRefundOrderNo;

    /// 原始响应(用于对账/排障)
    private String syncData;
}
