package cn.daxpay.open.channel.yizhifubj.client.resp;

import lombok.Data;
import lombok.experimental.Accessors;

/// # 一直付通道退款响应(主应用侧)
///
/// 退款为同步受理: status=SUCCESS 即退款成功(complete=true)。
@Data
@Accessors(chain = true)
public class YizhifubjRefundResp {

    /// 商户退款单号(透传)
    private String outRefundNo;

    /// 是否已终态完成(受理即成功, true)
    private Boolean complete;

    /// 退款通道流水号(serialNumber)
    private String outRefundOrderNo;

    /// 退款金额(单位: 分, 通道回执 amount)
    private Long refundAmount;

    /// 原始响应明文(用于对账/排障)
    private String syncData;
}
