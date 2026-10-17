package cn.daxpay.open.channel.baofu.client.resp;

import lombok.Data;
import lombok.experimental.Accessors;

/// # 宝付通道退款响应(主应用侧)
///
/// 宝付退款为同步终态: 受理成功(resultCode=SUCCESS)即退款成功(complete=true)。
@Data
@Accessors(chain = true)
public class BaofuRefundResp {

    /// 商户退款单号(透传)
    private String outRefundNo;

    /// 是否已终态完成(受理成功恒 true)
    private Boolean complete;

    /// 宝付退款交易号(tradeNo)
    private String outOrderNo;

    /// 退款金额(refundAmt, 单位: 分)
    private Long refundAmount;

    /// 原始响应明文(用于对账/排障)
    private String syncData;
}
