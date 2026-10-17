package cn.daxpay.open.channel.fubei.client.resp;

import lombok.Data;
import lombok.experimental.Accessors;

/// # 付呗通道退款响应(主应用侧)
///
/// 子应用调用付呗 fbpay.order.refund 后回传给主应用。
/// 付呗退款为同步受理(对齐参考实现: result_code=200 即退款成功, 无退款异步通知/退款查询接口)。
@Data
@Accessors(chain = true)
public class FubeiRefundResp {

    /// 商户退款单号(透传 FubeiRefundReq.outRefundNo)
    private String outRefundNo;

    /// 是否已终态完成(同步受理即成功, 恒 true)
    private Boolean complete;

    /// 原始响应(用于对账/排障)
    private String syncData;
}
