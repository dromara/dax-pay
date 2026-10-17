package cn.daxpay.open.channel.helipay.client.resp;

import lombok.Data;
import lombok.experimental.Accessors;

/// # 合利宝通道退款响应(子应用 → 主应用)
///
/// 合利宝退款为同步结果模式(retCode 0000/0001 即受理完成并同步返回退款流水号/金额),
/// `complete` 恒为 true。
@Data
@Accessors(chain = true)
public class HelipayRefundResp {

    /// 商户退款单号(透传 HelipayRefundReq.outRefundNo)
    private String outRefundNo;

    /// 合利宝退款流水号(rt7_serialNumber)
    private String outOrderNo;

    /// 退款金额(单位: 分)
    private Long refundAmount;

    /// 是否已终态成功(同步结果模式, 恒为 true)
    private Boolean complete;
}
