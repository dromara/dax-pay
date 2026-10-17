package cn.daxpay.open.channel.yisheng.client.resp;

import lombok.Data;
import lombok.experimental.Accessors;

/// # 易生通道退款响应
///
/// 子应用调用易生 easypay.merchant.refund 后回传给主应用。
/// 退款为同步终态: 受理成功(code=00)即视为退款成功(complete=true), 无退款异步通知与退款查询接口。
@Data
@Accessors(chain = true)
public class YishengRefundResp {

    /// 商户退款单号(透传 YishengRefundReq.outRefundNo)
    private String outRefundNo;

    /// 是否已终态完成(受理成功恒 true)
    private Boolean complete;

    /// 原始响应节点明文(用于对账/排障)
    private String syncData;
}
