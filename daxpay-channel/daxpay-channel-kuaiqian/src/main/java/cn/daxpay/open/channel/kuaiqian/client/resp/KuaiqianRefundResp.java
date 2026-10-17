package cn.daxpay.open.channel.kuaiqian.client.resp;

import lombok.Data;
import lombok.experimental.Accessors;

/// # 快钱通道退款响应(主应用侧)
///
/// A7003 受理成功仅代表快钱已接受退款请求(complete=false), 最终结果经 tr3Url 异步通知。
@Data
@Accessors(chain = true)
public class KuaiqianRefundResp {

    /// 商户退款单号(透传)
    private String outRefundNo;

    /// 是否已终态完成(受理恒 false, 等待异步通知)
    private Boolean complete;

    /// 原始响应明文(用于对账/排障)
    private String syncData;
}
